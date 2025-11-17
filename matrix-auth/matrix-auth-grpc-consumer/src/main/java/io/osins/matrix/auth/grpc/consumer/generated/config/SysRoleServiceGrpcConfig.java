package io.osins.matrix.auth.grpc.consumer.generated.config;
import io.grpc.ManagedChannel;
import io.osins.matrix.auth.grpc.SysRoleServiceGrpc;
import io.osins.matrix.auth.grpc.SysRoleServiceGrpc.SysRoleServiceBlockingStub;
import io.osins.matrix.auth.grpc.SysRoleServiceGrpc.SysRoleServiceBlockingV2Stub;
import io.osins.matrix.auth.grpc.SysRoleServiceGrpc.SysRoleServiceFutureStub;
import io.osins.matrix.auth.grpc.SysRoleServiceGrpc.SysRoleServiceStub;
import io.osins.matrix.shared.grpc.client.StubUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SysRoleServiceGrpcConfig {
    @Bean
    public Mono<SysRoleServiceBlockingStub> reactorSysRoleGrpcBlockingStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, SysRoleServiceGrpc::newBlockingStub);
    }

    @Bean
    public Mono<SysRoleServiceBlockingV2Stub> reactorSysRoleGrpcBlockingV2Stub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, SysRoleServiceGrpc::newBlockingV2Stub);
    }

    @Bean
    public Mono<SysRoleServiceFutureStub> reactorSysRoleGrpcFutureStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newFutureStub(reactorAuthGrpcChannel, SysRoleServiceGrpc::newFutureStub);
    }

    @Bean
    public Mono<SysRoleServiceStub> reactorSysRoleGrpcStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newAsyncStub(reactorAuthGrpcChannel, SysRoleServiceGrpc::newStub);
    }
}
