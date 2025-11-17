package io.osins.matrix.auth.grpc.consumer.generated.config;
import io.grpc.ManagedChannel;
import io.osins.matrix.auth.grpc.SysPermissionServiceGrpc;
import io.osins.matrix.auth.grpc.SysPermissionServiceGrpc.SysPermissionServiceBlockingStub;
import io.osins.matrix.auth.grpc.SysPermissionServiceGrpc.SysPermissionServiceBlockingV2Stub;
import io.osins.matrix.auth.grpc.SysPermissionServiceGrpc.SysPermissionServiceFutureStub;
import io.osins.matrix.auth.grpc.SysPermissionServiceGrpc.SysPermissionServiceStub;
import io.osins.matrix.shared.grpc.client.StubUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SysPermissionServiceGrpcConfig {
    @Bean
    public Mono<SysPermissionServiceBlockingStub> reactorSysPermissionGrpcBlockingStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, SysPermissionServiceGrpc::newBlockingStub);
    }

    @Bean
    public Mono<SysPermissionServiceBlockingV2Stub> reactorSysPermissionGrpcBlockingV2Stub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, SysPermissionServiceGrpc::newBlockingV2Stub);
    }

    @Bean
    public Mono<SysPermissionServiceFutureStub> reactorSysPermissionGrpcFutureStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newFutureStub(reactorAuthGrpcChannel, SysPermissionServiceGrpc::newFutureStub);
    }

    @Bean
    public Mono<SysPermissionServiceStub> reactorSysPermissionGrpcStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newAsyncStub(reactorAuthGrpcChannel, SysPermissionServiceGrpc::newStub);
    }
}
