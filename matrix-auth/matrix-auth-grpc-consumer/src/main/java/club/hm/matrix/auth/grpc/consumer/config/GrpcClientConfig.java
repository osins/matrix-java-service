package club.hm.matrix.auth.grpc.consumer.config;

import club.hm.matrix.auth.grpc.ReactorUserAuthorityServiceGrpc;
import club.hm.matrix.shared.grpc.client.discovery.ReactiveGrpcServiceDiscovery;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@ComponentScan(basePackages = "club.hm.matrix")
@RequiredArgsConstructor
public class GrpcClientConfig {

    private final ReactiveGrpcServiceDiscovery reactiveGrpcServiceDiscovery;

    /**
     * 响应式获取用户服务的gRPC通道
     */
    @Bean
    public Mono<ManagedChannel> userServiceChannel() {
        return reactiveGrpcServiceDiscovery.getChannel("matrix-auth")
                .doOnNext(channel -> log.info("User service gRPC channel created successfully: {}", channel))
                .doOnError(error -> log.error("Failed to create user service gRPC channel", error))
                .cache(); // 缓存结果，避免重复创建
    }

    /**
     * 创建用户服务的响应式 stub（Reactor gRPC）
     */
    @Bean
    public Mono<ReactorUserAuthorityServiceGrpc.ReactorUserAuthorityServiceStub> reactorUserAuthorityServiceGrpcStub() {
        return userServiceChannel()
                .map(channel -> ReactorUserAuthorityServiceGrpc.newReactorStub(channel).withWaitForReady())
                .cache();
    }
}