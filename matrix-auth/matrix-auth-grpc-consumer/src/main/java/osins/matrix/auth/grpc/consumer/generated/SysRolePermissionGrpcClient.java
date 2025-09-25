package osins.matrix.auth.grpc.consumer.generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import osins.matrix.auth.grpc.AssignPermissionRequest;
import osins.matrix.auth.grpc.FindByRoleIdRequest;
import osins.matrix.auth.grpc.RemovePermissionRequest;
import osins.matrix.shared.grpc.base.utils.Observer;
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRolePermissionGrpcClient {
    private final reactor.core.publisher.Mono<osins.matrix.auth.grpc.SysRolePermissionServiceGrpc.SysRolePermissionServiceStub> sysRolePermissionServiceStub;

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.SysRolePermissionResponse> assignPermissionToRole(AssignPermissionRequest request) {
        return this.sysRolePermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::assignPermissionToRole));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.SysRolePermissionList> findPermissionsByRoleId(FindByRoleIdRequest request) {
        return this.sysRolePermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::findPermissionsByRoleId));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.DeleteResponse> removePermissionFromRole(RemovePermissionRequest request) {
        return this.sysRolePermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::removePermissionFromRole));
    }
}
