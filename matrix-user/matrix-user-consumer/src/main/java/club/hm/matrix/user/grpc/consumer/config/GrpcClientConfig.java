package club.hm.matrix.user.grpc.consumer.config;

import club.hm.matrix.shared.grpc.client.discovery.ReactiveGrpcServiceDiscovery;
import club.hm.matrix.user.grpc.proto.UserServiceGrpc;
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
        return reactiveGrpcServiceDiscovery.getChannel("matrix-user")
                .doOnNext(channel -> log.info("User service gRPC channel created successfully"))
                .doOnError(error -> log.error("Failed to create user service gRPC channel", error))
                .cache(); // 缓存结果，避免重复创建
    }

    /**
     * 创建用户服务的同步stub（用于阻塞调用）
     */
    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub() {
        return userServiceChannel()
                .map(channel -> UserServiceGrpc.newBlockingStub(channel).withWaitForReady())
                .block(); // 在配置阶段阻塞获取
    }

    /**
     * 创建用户服务的异步stub（用于响应式调用）
     */
    @Bean
    public Mono<UserServiceGrpc.UserServiceStub> userServiceStub() {
        return userServiceChannel()
                .map(channel -> UserServiceGrpc.newStub(channel).withWaitForReady())
                .cache();
    }

    /**
     * 创建用户服务的Future stub（用于异步但非响应式调用）
     */
    @Bean
    public Mono<UserServiceGrpc.UserServiceFutureStub> userServiceFutureStub() {
        return userServiceChannel()
                .map(channel -> UserServiceGrpc.newFutureStub(channel).withWaitForReady())
                .cache();
    }
}