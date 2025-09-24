package club.hm.matrix.auth.grpc.consumer.generated.config;
import club.hm.matrix.auth.grpc.SysRolePermissionServiceGrpc;
import club.hm.matrix.auth.grpc.SysRolePermissionServiceGrpc.SysRolePermissionServiceBlockingStub;
import club.hm.matrix.auth.grpc.SysRolePermissionServiceGrpc.SysRolePermissionServiceBlockingV2Stub;
import club.hm.matrix.auth.grpc.SysRolePermissionServiceGrpc.SysRolePermissionServiceFutureStub;
import club.hm.matrix.auth.grpc.SysRolePermissionServiceGrpc.SysRolePermissionServiceStub;
import club.hm.matrix.shared.grpc.client.StubUtils;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SysRolePermissionServiceGrpcConfig {
    @Bean
    public Mono<SysRolePermissionServiceBlockingStub> reactorSysRolePermissionGrpcBlockingStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, SysRolePermissionServiceGrpc::newBlockingStub);
    }

    @Bean
    public Mono<SysRolePermissionServiceBlockingV2Stub> reactorSysRolePermissionGrpcBlockingV2Stub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, SysRolePermissionServiceGrpc::newBlockingV2Stub);
    }

    @Bean
    public Mono<SysRolePermissionServiceFutureStub> reactorSysRolePermissionGrpcFutureStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newFutureStub(reactorAuthGrpcChannel, SysRolePermissionServiceGrpc::newFutureStub);
    }

    @Bean
    public Mono<SysRolePermissionServiceStub> reactorSysRolePermissionGrpcStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newAsyncStub(reactorAuthGrpcChannel, SysRolePermissionServiceGrpc::newStub);
    }
}
