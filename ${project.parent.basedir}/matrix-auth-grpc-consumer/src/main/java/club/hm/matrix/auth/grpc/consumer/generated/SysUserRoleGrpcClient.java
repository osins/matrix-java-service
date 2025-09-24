package club.hm.matrix.auth.grpc.consumer.generated;
import club.hm.matrix.auth.grpc.AssignRoleRequest;
import club.hm.matrix.auth.grpc.FindByUserIdRequest;
import club.hm.matrix.auth.grpc.RemoveRoleRequest;
import club.hm.matrix.shared.grpc.base.utils.Observer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserRoleGrpcClient {
    private final reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysUserRoleServiceGrpc.SysUserRoleServiceStub> sysUserRoleServiceStub;

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysUserRoleResponse> assignRoleToUser(AssignRoleRequest request) {
        return this.sysUserRoleServiceStub.flatMap(stub -> Observer.mono(request, stub::assignRoleToUser));
    }

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.SysUserRoleList> findRolesByUserId(FindByUserIdRequest request) {
        return this.sysUserRoleServiceStub.flatMap(stub -> Observer.mono(request, stub::findRolesByUserId));
    }

    public reactor.core.publisher.Mono<club.hm.matrix.auth.grpc.DeleteResponse> removeRoleFromUser(RemoveRoleRequest request) {
        return this.sysUserRoleServiceStub.flatMap(stub -> Observer.mono(request, stub::removeRoleFromUser));
    }
}
