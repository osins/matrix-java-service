package io.osins.matrix.auth.grpc.consumer.generated;
import io.osins.matrix.auth.grpc.CreatePermissionRequest;
import io.osins.matrix.auth.grpc.DeletePermissionRequest;
import io.osins.matrix.auth.grpc.FindByIdRequest;
import io.osins.matrix.shared.grpc.base.utils.Observer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class SysPermissionGrpcClient {
    private final reactor.core.publisher.Mono<io.osins.matrix.auth.grpc.SysPermissionServiceGrpc.SysPermissionServiceStub> sysPermissionServiceStub;

    public reactor.core.publisher.Mono<io.osins.matrix.auth.grpc.SysPermissionResponse> createPermission(CreatePermissionRequest request) {
        return this.sysPermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::createPermission));
    }

    public reactor.core.publisher.Mono<io.osins.matrix.auth.grpc.DeleteResponse> deletePermission(DeletePermissionRequest request) {
        return this.sysPermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::deletePermission));
    }

    public reactor.core.publisher.Mono<io.osins.matrix.auth.grpc.SysPermissionList> findAll() {
        return this.sysPermissionServiceStub.flatMap(stub -> Observer.mono(null, stub::findAll));
    }

    public reactor.core.publisher.Mono<io.osins.matrix.auth.grpc.SysPermissionResponse> findById(FindByIdRequest request) {
        return this.sysPermissionServiceStub.flatMap(stub -> Observer.mono(request, stub::findById));
    }
}
