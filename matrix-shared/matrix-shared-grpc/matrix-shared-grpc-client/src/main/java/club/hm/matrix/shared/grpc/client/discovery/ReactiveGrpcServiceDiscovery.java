package club.hm.matrix.shared.grpc.client.discovery;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.micrometer.core.instrument.binder.grpc.ObservationGrpcClientInterceptor;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@RequiredArgsConstructor
public class ReactiveGrpcServiceDiscovery {
    private final ObservationRegistry observationRegistry;
    private final ReactiveDiscoveryClient reactiveDiscoveryClient;
    private final ConcurrentHashMap<String, ManagedChannel> channelCache = new ConcurrentHashMap<>();

    private final Tracer tracer;         // 注入 Micrometer Tracer
    private final Propagator propagator; // 注入 Micrometer Propagator

    public Mono<ManagedChannel> getChannel(String serviceName) {
        // 先检查缓存
        var cachedChannel = channelCache.get(serviceName);
        if (cachedChannel != null && !cachedChannel.isShutdown()) {
            return Mono.just(cachedChannel);
        }

        // 异步创建新连接
        return createChannelAsync(serviceName)
                .doOnSuccess(channel -> channelCache.put(serviceName, channel))
                .doOnError(error -> log.error("Failed to create channel for service: {}", serviceName, error));
    }

    private Mono<ManagedChannel> createChannelAsync(String serviceName) {
        log.info("Creating reactive gRPC channel for service: {}", serviceName);

        return reactiveDiscoveryClient.getInstances(serviceName)
                .collectList()
                .timeout(Duration.ofSeconds(10))
                .map(this::selectInstance)
                .map(instance -> buildChannel(instance, serviceName))
                .onErrorResume(error -> {
                    log.error("Failed to discover service: {}, using fallback", serviceName, error);
                    return Mono.just(createFallbackChannel(serviceName));
                });
    }

    private ServiceInstance selectInstance(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            throw new IllegalStateException("No instances available");
        }

        // 简单的随机负载均衡
        return instances.get(ThreadLocalRandom.current().nextInt(instances.size()));
    }

    private ManagedChannel buildChannel(ServiceInstance instance, String serviceName) {
        int grpcPort = getGrpcPort(instance);
        String target = instance.getHost() + ":" + grpcPort;

        log.info("Building gRPC channel: {} -> {}", serviceName, target);

        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .intercept(new LoggingClientInterceptor(tracer, propagator))
                .intercept(new ObservationGrpcClientInterceptor(observationRegistry))
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(5, TimeUnit.SECONDS)
                .keepAliveWithoutCalls(true)
                .maxInboundMessageSize(4 * 1024 * 1024)
                .maxInboundMetadataSize(8192)
                .idleTimeout(5, TimeUnit.MINUTES)
                .build();
    }

    private ManagedChannel createFallbackChannel(String serviceName) {
        String target = "localhost:9090";
        log.info("Creating fallback gRPC channel: {} -> {}", serviceName, target);

        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .keepAliveWithoutCalls(true)
                .build();
    }

    private int getGrpcPort(ServiceInstance instance) {
        // 从metadata获取gRPC端口
        String grpcPortMeta = instance.getMetadata().get("grpc.port");
        if (grpcPortMeta != null && !grpcPortMeta.isEmpty()) {
            try {
                return Integer.parseInt(grpcPortMeta);
            } catch (NumberFormatException e) {
                log.warn("Invalid gRPC port in metadata: {}", grpcPortMeta);
            }
        }

        // 默认gRPC端口
        return 9090;
    }

    /**
     * 获取服务列表（响应式）
     */
    public Mono<List<String>> getServices() {
        return reactiveDiscoveryClient.getServices()
                .collectList()
                .timeout(Duration.ofSeconds(5))
                .doOnNext(services -> log.debug("Available services: {}", services))
                .onErrorReturn(List.of());
    }

    /**
     * 健康检查
     */
    public Mono<Boolean> isHealthy(String serviceName) {
        return reactiveDiscoveryClient.getInstances(serviceName)
                .hasElements()
                .timeout(Duration.ofSeconds(3))
                .onErrorReturn(false);
    }

    @PreDestroy
    public void destroy() {
        log.info("Shutting down reactive gRPC channels...");

        channelCache.values().parallelStream().forEach(channel -> {
            if (!channel.isShutdown()) {
                try {
                    log.debug("Shutting down channel: {}", channel);
                    channel.shutdown();
                    if (!channel.awaitTermination(5, TimeUnit.SECONDS)) {
                        log.warn("Channel did not terminate gracefully, forcing shutdown");
                        channel.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    channel.shutdownNow();
                    log.warn("Interrupted while shutting down channel", e);
                }
            }
        });

        channelCache.clear();
        log.info("All gRPC channels shut down successfully");
    }
}