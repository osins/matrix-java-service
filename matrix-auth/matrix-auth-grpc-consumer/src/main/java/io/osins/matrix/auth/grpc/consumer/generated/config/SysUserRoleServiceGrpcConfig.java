package io.osins.matrix.auth.grpc.consumer.generated.config;
import io.grpc.ManagedChannel;
import io.osins.matrix.auth.grpc.SysUserRoleServiceGrpc;
import io.osins.matrix.auth.grpc.SysUserRoleServiceGrpc.SysUserRoleServiceBlockingStub;
import io.osins.matrix.auth.grpc.SysUserRoleServiceGrpc.SysUserRoleServiceBlockingV2Stub;
import io.osins.matrix.auth.grpc.SysUserRoleServiceGrpc.SysUserRoleServiceFutureStub;
import io.osins.matrix.auth.grpc.SysUserRoleServiceGrpc.SysUserRoleServiceStub;
import io.osins.matrix.shared.grpc.client.StubUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SysUserRoleServiceGrpcConfig {
    @Bean
    public Mono<SysUserRoleServiceBlockingStub> reactorSysUserRoleGrpcBlockingStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, SysUserRoleServiceGrpc::newBlockingStub);
    }

    @Bean
    public Mono<SysUserRoleServiceBlockingV2Stub> reactorSysUserRoleGrpcBlockingV2Stub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, SysUserRoleServiceGrpc::newBlockingV2Stub);
    }

    @Bean
    public Mono<SysUserRoleServiceFutureStub> reactorSysUserRoleGrpcFutureStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newFutureStub(reactorAuthGrpcChannel, SysUserRoleServiceGrpc::newFutureStub);
    }

    @Bean
    public Mono<SysUserRoleServiceStub> reactorSysUserRoleGrpcStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newAsyncStub(reactorAuthGrpcChannel, SysUserRoleServiceGrpc::newStub);
    }
}
