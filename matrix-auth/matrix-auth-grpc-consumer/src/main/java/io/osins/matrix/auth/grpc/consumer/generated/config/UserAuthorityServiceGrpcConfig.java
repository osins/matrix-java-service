package io.osins.matrix.auth.grpc.consumer.generated.config;
import io.grpc.ManagedChannel;
import io.osins.matrix.auth.grpc.UserAuthorityServiceGrpc;
import io.osins.matrix.auth.grpc.UserAuthorityServiceGrpc.UserAuthorityServiceBlockingStub;
import io.osins.matrix.auth.grpc.UserAuthorityServiceGrpc.UserAuthorityServiceBlockingV2Stub;
import io.osins.matrix.auth.grpc.UserAuthorityServiceGrpc.UserAuthorityServiceFutureStub;
import io.osins.matrix.auth.grpc.UserAuthorityServiceGrpc.UserAuthorityServiceStub;
import io.osins.matrix.shared.grpc.client.StubUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserAuthorityServiceGrpcConfig {
    @Bean
    public Mono<UserAuthorityServiceBlockingStub> reactorUserAuthorityGrpcBlockingStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, UserAuthorityServiceGrpc::newBlockingStub);
    }

    @Bean
    public Mono<UserAuthorityServiceBlockingV2Stub> reactorUserAuthorityGrpcBlockingV2Stub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, UserAuthorityServiceGrpc::newBlockingV2Stub);
    }

    @Bean
    public Mono<UserAuthorityServiceFutureStub> reactorUserAuthorityGrpcFutureStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newFutureStub(reactorAuthGrpcChannel, UserAuthorityServiceGrpc::newFutureStub);
    }

    @Bean
    public Mono<UserAuthorityServiceStub> reactorUserAuthorityGrpcStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newAsyncStub(reactorAuthGrpcChannel, UserAuthorityServiceGrpc::newStub);
    }
}
