package club.hm.matrix.auth.grpc.consumer.config;

import club.hm.matrix.auth.grpc.UserAuthorityServiceGrpc;
import club.hm.matrix.shared.grpc.client.ReactiveGrpcServiceDiscovery;
import io.grpc.ManagedChannel;
import io.grpc.stub.AbstractStub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@Configuration
@ComponentScan(basePackages = "club.hm.matrix")
@RequiredArgsConstructor
public class GrpcClientConfig {
    private final ReactiveGrpcServiceDiscovery reactiveGrpcServiceDiscovery;

    private <S extends AbstractStub<S>> Mono<S> createStub(Mono<ManagedChannel> channel, Function<ManagedChannel, AbstractStub<S>> mapper){
        return channel
                .map(c -> mapper.apply(c).withWaitForReady())
                .doOnError(throwable -> log.error("createStub error: {}", throwable.getMessage(), throwable))
                .doOnSuccess(s->log.info("createStub success: {}", s))
                .cache();
    }

    @Bean
    public Mono<ManagedChannel> channel() {
        log.debug("Creating gRPC channel...");
        return reactiveGrpcServiceDiscovery.getChannel("matrix-auth")
                .doOnNext(channel -> log.info("Create gRPC channel successfully: {}", channel))
                .doOnError(error -> log.error("Failed to create user service gRPC channel", error))
                .cache(); // 缓存结果，避免重复创建
    }

    @Bean
    public Mono<UserAuthorityServiceGrpc.UserAuthorityServiceStub> reactorUserAuthorityServiceGrpcStub(Mono<ManagedChannel> channel) {
        return createStub(channel, UserAuthorityServiceGrpc::newStub);
    }

//    @Bean
//    public Mono<ReactorSysRoleServiceGrpc.ReactorSysRoleServiceStub> reactorSysRoleServiceStubMono(Mono<ManagedChannel> channel) {
//        return createStub(channel, ReactorSysRoleServiceGrpc::newReactorStub);
//    }
}