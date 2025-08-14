package club.hm.matrix.shared.grpc.client.discovery;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LocalReactiveGrpcServiceDiscovery extends ReactiveGrpcServiceDiscovery {

    private final ConcurrentHashMap<String, ManagedChannel> localChannels = new ConcurrentHashMap<>();

    // 本地服务端口映射
    private final Map<String, String> localServicePorts;

    public LocalReactiveGrpcServiceDiscovery(LocalGrpcProperties properties) {
        super(null); // 不需要 ReactiveDiscoveryClient
        this.localServicePorts = properties.getServicePorts();
        log.info("Loaded local gRPC service ports: {}", this.localServicePorts);
    }

    @Override
    public Mono<ManagedChannel> getChannel(String serviceName) {
        return Mono.fromCallable(() ->
                localChannels.computeIfAbsent(serviceName, this::createLocalChannel)
        );
    }

    private ManagedChannel createLocalChannel(String serviceName) {
        String target = localServicePorts.getOrDefault(serviceName, "localhost:9090");

        log.info("Creating local reactive gRPC channel: {} -> {}", serviceName, target);

        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .keepAliveWithoutCalls(true)
                .maxInboundMessageSize(4 * 1024 * 1024)
                .build();
    }

    @PreDestroy
    public void destroy() {
        log.info("Shutting down local reactive gRPC channels");
        localChannels.values().forEach(ManagedChannel::shutdown);
        localChannels.clear();
    }
}