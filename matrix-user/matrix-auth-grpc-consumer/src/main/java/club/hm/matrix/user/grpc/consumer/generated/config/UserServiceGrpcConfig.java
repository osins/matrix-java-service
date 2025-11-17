package io.osins.matrix.user.grpc.consumer.generated.config;
import io.osins.matrix.shared.grpc.client.StubUtils;
import io.osins.matrix.user.grpc.proto.UserServiceGrpc;
import io.osins.matrix.user.grpc.proto.UserServiceGrpc.UserServiceBlockingStub;
import io.osins.matrix.user.grpc.proto.UserServiceGrpc.UserServiceBlockingV2Stub;
import io.osins.matrix.user.grpc.proto.UserServiceGrpc.UserServiceFutureStub;
import io.osins.matrix.user.grpc.proto.UserServiceGrpc.UserServiceStub;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserServiceGrpcConfig {
    @Bean
    public Mono<UserServiceBlockingStub> reactorUserGrpcBlockingStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, UserServiceGrpc::newBlockingStub);
    }

    @Bean
    public Mono<UserServiceBlockingV2Stub> reactorUserGrpcBlockingV2Stub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, UserServiceGrpc::newBlockingV2Stub);
    }

    @Bean
    public Mono<UserServiceFutureStub> reactorUserGrpcFutureStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newFutureStub(reactorAuthGrpcChannel, UserServiceGrpc::newFutureStub);
    }

    @Bean
    public Mono<UserServiceStub> reactorUserGrpcStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newAsyncStub(reactorAuthGrpcChannel, UserServiceGrpc::newStub);
    }
}
