package osins.matrix.auth.grpc.consumer.generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import osins.matrix.auth.grpc.CreateRoleRequest;
import osins.matrix.auth.grpc.DeleteRoleRequest;
import osins.matrix.auth.grpc.FindByIdRequest;
import osins.matrix.shared.grpc.base.utils.Observer;
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleGrpcClient {
    private final reactor.core.publisher.Mono<osins.matrix.auth.grpc.SysRoleServiceGrpc.SysRoleServiceStub> sysRoleServiceStub;

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.SysRoleResponse> createRole(CreateRoleRequest request) {
        return this.sysRoleServiceStub.flatMap(stub -> Observer.mono(request, stub::createRole));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.DeleteResponse> deleteRole(DeleteRoleRequest request) {
        return this.sysRoleServiceStub.flatMap(stub -> Observer.mono(request, stub::deleteRole));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.SysRoleList> findAll() {
        return this.sysRoleServiceStub.flatMap(stub -> Observer.mono(null, stub::findAll));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.SysRoleResponse> findById(FindByIdRequest request) {
        return this.sysRoleServiceStub.flatMap(stub -> Observer.mono(request, stub::findById));
    }
}
