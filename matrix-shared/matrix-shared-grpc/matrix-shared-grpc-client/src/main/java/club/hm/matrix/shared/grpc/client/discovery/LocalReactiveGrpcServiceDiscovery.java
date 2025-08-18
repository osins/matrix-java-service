package club.hm.matrix.shared.grpc.client.discovery;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.micrometer.core.instrument.binder.grpc.ObservationGrpcClientInterceptor;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LocalReactiveGrpcServiceDiscovery extends ReactiveGrpcServiceDiscovery {
    private final ObservationRegistry observationRegistry;

    private final ConcurrentHashMap<String, ManagedChannel> localChannels = new ConcurrentHashMap<>();

    // 本地服务端口映射
    private final Map<String, String> localServicePorts;

    public LocalReactiveGrpcServiceDiscovery(ObservationRegistry observationRegistry, LocalGrpcProperties properties, Tracer tracer, Propagator propagator) {
        super(observationRegistry, null, tracer, propagator);
        this.observationRegistry = observationRegistry;
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
        if (localServicePorts == null) {
            log.error("localServicePorts settings is null, service name: {}", serviceName);
            throw new RuntimeException("localServicePorts settings is null, service name: " + serviceName);
        }

        var target = localServicePorts.getOrDefault(serviceName, "localhost:9090");

        log.info("Creating local reactive gRPC channel: {} -> {}", serviceName, target);

        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .intercept(new LoggingClientInterceptor(super.getTracer(), super.getPropagator()))
                .intercept(new ObservationGrpcClientInterceptor(observationRegistry))
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