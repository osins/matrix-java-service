package osins.matrix.user.grpc.consumer.config;

import osins.matrix.shared.grpc.client.ReactiveGrpcServiceDiscovery;
import osins.matrix.user.grpc.proto.UserServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@ComponentScan(basePackages = "osins.matrix")
@RequiredArgsConstructor
public class GrpcClientConfig {

    private final ReactiveGrpcServiceDiscovery reactiveGrpcServiceDiscovery;

    /**
     * 响应式获取用户服务的gRPC通道
     */
    @Bean
    public Mono<ManagedChannel> userServiceChannel() {
        return reactiveGrpcServiceDiscovery.getChannel("matrix-user")
                .doOnNext(channel -> log.info("User service gRPC channel created successfully"))
                .doOnError(error -> log.error("Failed to create user service gRPC channel", error))
                .cache(); // 缓存结果，避免重复创建
    }

    /**
     * 创建用户服务的响应式 stub（Reactor gRPC）
     */
    @Bean
    public Mono<UserServiceGrpc.UserServiceStub> reactorUserServiceStub() {
        return userServiceChannel()
                .map(channel -> UserServiceGrpc.newStub(channel).withWaitForReady())
                .cache();
    }
}