package osins.matrix.auth.grpc.consumer.generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import osins.matrix.auth.grpc.ChangePasswordByUsernameRequest;
import osins.matrix.auth.grpc.CreateUserRequest;
import osins.matrix.auth.grpc.GetUserPermissionsRequest;
import osins.matrix.auth.grpc.GetUserRolesRequest;
import osins.matrix.auth.grpc.HasPermissionRequest;
import osins.matrix.auth.grpc.HasRoleRequest;
import osins.matrix.auth.grpc.LoadUserByIdRequest;
import osins.matrix.auth.grpc.LoadUserByUsernameRequest;
import osins.matrix.auth.grpc.LoadUsersByIdsRequest;
import osins.matrix.auth.grpc.UpdateUserRequest;
import osins.matrix.shared.grpc.base.utils.Observer;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthorityGrpcClient {
    private final reactor.core.publisher.Mono<osins.matrix.auth.grpc.UserAuthorityServiceGrpc.UserAuthorityServiceStub> userAuthorityServiceStub;

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.ChangePasswordByUsernameResponse> changePasswordByUsername(ChangePasswordByUsernameRequest request) {
        return this.userAuthorityServiceStub.flatMap(stub -> Observer.mono(request, stub::changePasswordByUsername));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.UserResponse> createUser(CreateUserRequest request) {
        return this.userAuthorityServiceStub.flatMap(stub -> Observer.mono(request, stub::createUser));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.PermissionsResponse> getUserPermissions(GetUserPermissionsRequest request) {
        return this.userAuthorityServiceStub.flatMap(stub -> Observer.mono(request, stub::getUserPermissions));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.RolesResponse> getUserRoles(GetUserRolesRequest request) {
        return this.userAuthorityServiceStub.flatMap(stub -> Observer.mono(request, stub::getUserRoles));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.HasPermissionResponse> hasPermission(HasPermissionRequest request) {
        return this.userAuthorityServiceStub.flatMap(stub -> Observer.mono(request, stub::hasPermission));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.HasRoleResponse> hasRole(HasRoleRequest request) {
        return this.userAuthorityServiceStub.flatMap(stub -> Observer.mono(request, stub::hasRole));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.UserResponse> loadUserById(LoadUserByIdRequest request) {
        return this.userAuthorityServiceStub.flatMap(stub -> Observer.mono(request, stub::loadUserById));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.UserResponse> loadUserByUsername(LoadUserByUsernameRequest request) {
        return this.userAuthorityServiceStub.flatMap(stub -> Observer.mono(request, stub::loadUserByUsername));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.UserResponse> loadUsersByIds(LoadUsersByIdsRequest request) {
        return this.userAuthorityServiceStub.flatMap(stub -> Observer.mono(request, stub::loadUsersByIds));
    }

    public reactor.core.publisher.Mono<osins.matrix.auth.grpc.UserResponse> updateUser(UpdateUserRequest request) {
        return this.userAuthorityServiceStub.flatMap(stub -> Observer.mono(request, stub::updateUser));
    }
}
