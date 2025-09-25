package osins.matrix.auth.grpc.consumer.generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import osins.matrix.auth.grpc.CreatePermissionRequest;
import osins.matrix.auth.grpc.DeletePermissionRequest;
import osins.matrix.auth.grpc.FindByIdRequest;
import osins.matrix.shared.grpc.base.utils.Observer;
@Slf4j
@Service
@RequiredArgsConstructor
public class SysPermissionGrpcClient {
    private final reactor.core.publisher.Mono<osins.matrix.auth.grpc.SysPermissionServiceGrpc.SysPermissionServiceStub> sysPermissionServiceStub;

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.SysPermissionResponse> createPermission(CreatePermissionRequest request) {
        return this.sysPermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::createPermission));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.DeleteResponse> deletePermission(DeletePermissionRequest request) {
        return this.sysPermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::deletePermission));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.SysPermissionList> findAll() {
        return this.sysPermissionServiceStub.flatMap(stub -> Observer.mono(null, stub::findAll));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.SysPermissionResponse> findById(FindByIdRequest request) {
        return this.sysPermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::findById));
    }
}
