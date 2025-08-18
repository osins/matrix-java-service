package club.hm.matrix.auth.grpc.consumer.config;

import club.hm.matrix.auth.grpc.ReactorUserAuthorityServiceGrpc;
import club.hm.matrix.shared.grpc.client.discovery.ReactiveGrpcServiceDiscovery;
import club.hm.matrix.shared.tracing.utils.Tracing;
import io.grpc.ClientInterceptor;
import io.micrometer.observation.ObservationRegistry;
import io.grpc.ManagedChannel;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@ComponentScan(basePackages = "club.hm.matrix")
@RequiredArgsConstructor
public class GrpcClientConfig {
    /**
     * 响应式获取用户服务的gRPC通道
     */
    @Bean
    public Mono<ManagedChannel> userServiceChannel(ReactiveGrpcServiceDiscovery reactiveGrpcServiceDiscovery, Tracer tracer) {
        return reactiveGrpcServiceDiscovery.getChannel("matrix-auth")
                .doOnNext(channel -> log.info("User service gRPC channel created successfully: {}", channel))
                .doOnError(error -> log.error("Failed to create user service gRPC channel", error))
                .cache(); // 缓存结果，避免重复创建
    }

    /**
     * 创建用户服务的响应式 stub（Reactor gRPC）
     */
    @Bean
    public Mono<ReactorUserAuthorityServiceGrpc.ReactorUserAuthorityServiceStub> reactorUserAuthorityServiceGrpcStub(ReactiveGrpcServiceDiscovery reactiveGrpcServiceDiscovery,Tracer tracer) {
        return userServiceChannel(reactiveGrpcServiceDiscovery, tracer)
                .map(channel -> ReactorUserAuthorityServiceGrpc.newReactorStub(channel).withWaitForReady())
                .cache();
    }
}