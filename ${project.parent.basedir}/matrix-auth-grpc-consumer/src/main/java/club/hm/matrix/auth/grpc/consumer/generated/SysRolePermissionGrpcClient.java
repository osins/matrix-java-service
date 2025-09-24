package club.hm.matrix.auth.grpc.consumer.generated;
import club.hm.matrix.auth.grpc.AssignPermissionRequest;
import club.hm.matrix.auth.grpc.FindByRoleIdRequest;
import club.hm.matrix.auth.grpc.RemovePermissionRequest;
import club.hm.matrix.shared.grpc.base.utils.Observer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRolePermissionGrpcClient {
    private final reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysRolePermissionServiceGrpc.SysRolePermissionServiceStub> sysRolePermissionServiceStub;

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysRolePermissionResponse> assignPermissionToRole(AssignPermissionRequest request) {
        return this.sysRolePermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::assignPermissionToRole));
    }

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysRolePermissionList> findPermissionsByRoleId(FindByRoleIdRequest request) {
        return this.sysRolePermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::findPermissionsByRoleId));
    }

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.DeleteResponse> removePermissionFromRole(RemovePermissionRequest request) {
        return this.sysRolePermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::removePermissionFromRole));
    }
}
