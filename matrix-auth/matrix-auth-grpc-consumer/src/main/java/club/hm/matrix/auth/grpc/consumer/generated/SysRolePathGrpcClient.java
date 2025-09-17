package club.hm.matrix.auth.grpc.consumer.generated;
import club.hm.matrix.auth.grpc.CreateRolePathRequest;
import club.hm.matrix.auth.grpc.DeleteByIdRequest;
import club.hm.matrix.auth.grpc.FindByPathRequest;
import club.hm.matrix.auth.grpc.FindByRoleRequest;
import club.hm.matrix.auth.grpc.UpdateRolePathRequest;
import club.hm.matrix.shared.grpc.base.utils.Observer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRolePathGrpcClient {
    private final reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysRolePathServiceGrpc.SysRolePathServiceStub> sysRolePathServiceStub;

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysRolePathResponse> createRolePath(CreateRolePathRequest request) {
        return this.sysRolePathServiceStub.flatMap(stub -> Observer.mono(request, stub::createRolePath));
    }

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.DeleteResponse> deleteById(DeleteByIdRequest request) {
        return this.sysRolePathServiceStub.flatMap(stub -> Observer.mono(request, stub::deleteById));
    }

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysRolePathList> findAll() {
        return this.sysRolePathServiceStub.flatMap(stub -> Observer.mono(null, stub::findAll));
    }

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysRolePathList> findByPath(FindByPathRequest request) {
        return this.sysRolePathServiceStub.flatMap(stub -> Observer.mono(request, stub::findByPath));
    }

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysRolePathList> findByRole(FindByRoleRequest request) {
        return this.sysRolePathServiceStub.flatMap(stub -> Observer.mono(request, stub::findByRole));
    }

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysRolePathResponse> updateRolePath(UpdateRolePathRequest request) {
        return this.sysRolePathServiceStub.flatMap(stub -> Observer.mono(request, stub::updateRolePath));
    }
}
