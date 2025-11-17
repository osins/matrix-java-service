package io.osins.matrix.auth.grpc.consumer.generated;
import io.osins.matrix.auth.grpc.AssignRoleRequest;
import io.osins.matrix.auth.grpc.FindByUserIdRequest;
import io.osins.matrix.auth.grpc.RemoveRoleRequest;
import io.osins.matrix.shared.grpc.base.utils.Observer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserRoleGrpcClient {
    private final reactor.core.publisher.Mono<io.osins.matrix.auth.grpc.SysUserRoleServiceGrpc.SysUserRoleServiceStub> sysUserRoleServiceStub;

    public reactor.core.publisher.Mono<io.osins.matrix.auth.grpc.SysUserRoleResponse> assignRoleToUser(AssignRoleRequest request) {
        return this.sysUserRoleServiceStub.flatMap(stub -> Observer.mono(request, stub::assignRoleToUser));
    }

    public reactor.core.publisher.Mono<io.osins.matrix.auth.grpc.SysUserRoleList> findRolesByUserId(FindByUserIdRequest request) {
        return this.sysUserRoleServiceStub.flatMap(stub -> Observer.mono(request, stub::findRolesByUserId));
    }

    public reactor.core.publisher.Mono<io.osins.matrix.auth.grpc.DeleteResponse> removeRoleFromUser(RemoveRoleRequest request) {
        return this.sysUserRoleServiceStub.flatMap(stub -> Observer.mono(request, stub::removeRoleFromUser));
    }
}
