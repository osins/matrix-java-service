package osins.matrix.auth.grpc.consumer.generated.config;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import osins.matrix.auth.grpc.SysRolePathServiceGrpc;
import osins.matrix.auth.grpc.SysRolePathServiceGrpc.SysRolePathServiceBlockingStub;
import osins.matrix.auth.grpc.SysRolePathServiceGrpc.SysRolePathServiceBlockingV2Stub;
import osins.matrix.auth.grpc.SysRolePathServiceGrpc.SysRolePathServiceFutureStub;
import osins.matrix.auth.grpc.SysRolePathServiceGrpc.SysRolePathServiceStub;
import osins.matrix.shared.grpc.client.StubUtils;
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
