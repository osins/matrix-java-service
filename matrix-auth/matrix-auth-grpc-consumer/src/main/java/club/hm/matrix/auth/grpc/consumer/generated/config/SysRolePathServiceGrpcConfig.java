package club.hm.matrix.auth.grpc.consumer.generated.config;
import club.hm.matrix.auth.grpc.SysRolePathServiceGrpc;
import club.hm.matrix.auth.grpc.SysRolePathServiceGrpc.SysRolePathServiceBlockingStub;
import club.hm.matrix.auth.grpc.SysRolePathServiceGrpc.SysRolePathServiceBlockingV2Stub;
import club.hm.matrix.auth.grpc.SysRolePathServiceGrpc.SysRolePathServiceFutureStub;
import club.hm.matrix.auth.grpc.SysRolePathServiceGrpc.SysRolePathServiceStub;
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
public class SysRolePathServiceGrpcConfig {
    @Bean
    public Mono<SysRolePathServiceBlockingStub> reactorSysRolePathGrpcBlockingStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, SysRolePathServiceGrpc::newBlockingStub);
    }

    @Bean
    public Mono<SysRolePathServiceBlockingV2Stub> reactorSysRolePathGrpcBlockingV2Stub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newBlockingStub(reactorAuthGrpcChannel, SysRolePathServiceGrpc::newBlockingV2Stub);
    }

    @Bean
    public Mono<SysRolePathServiceFutureStub> reactorSysRolePathGrpcFutureStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newFutureStub(reactorAuthGrpcChannel, SysRolePathServiceGrpc::newFutureStub);
    }

    @Bean
    public Mono<SysRolePathServiceStub> reactorSysRolePathGrpcStub(Mono<ManagedChannel> reactorAuthGrpcChannel) {
        return StubUtils.newAsyncStub(reactorAuthGrpcChannel, SysRolePathServiceGrpc::newStub);
    }
}
