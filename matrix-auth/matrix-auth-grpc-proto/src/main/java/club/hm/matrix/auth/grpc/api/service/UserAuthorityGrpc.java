package club.hm.matrix.auth.grpc.api.service;

import club.hm.matrix.auth.grpc.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * gRPC Reactive 客户端接口
 */
public interface UserAuthorityGrpc {

    Mono<UserResponse> loadUserById(LoadUserByIdRequest request);

    Mono<UserResponse> loadUserById(Mono<LoadUserByIdRequest> request);

    Mono<UserResponse> loadUserByUsername(LoadUserByUsernameRequest request);

    Mono<UserResponse> loadUserByUsername(Mono<LoadUserByUsernameRequest> request);

    Mono<PermissionsResponse> getUserPermissions(GetUserPermissionsRequest request);

    Mono<PermissionsResponse> getUserPermissions(Mono<GetUserPermissionsRequest> request);

    Mono<HasPermissionResponse> hasPermission(HasPermissionRequest request);

    Mono<HasPermissionResponse> hasPermission(Mono<HasPermissionRequest> request);

    Mono<RolesResponse> getUserRoles(GetUserRolesRequest request);

    Mono<RolesResponse> getUserRoles(Mono<GetUserRolesRequest> request);

    Mono<HasRoleResponse> hasRole(HasRoleRequest request);

    Mono<HasRoleResponse> hasRole(Mono<HasRoleRequest> request);

    Flux<UserResponse> loadUsersByIds(LoadUsersByIdsRequest request);

    Flux<UserResponse> loadUsersByIds(Mono<LoadUsersByIdsRequest> request);

    /**
     * 新增：创建用户
     */
    Mono<UserResponse> createUser(CreateUserRequest request);

    Mono<UserResponse> createUser(Mono<CreateUserRequest> request);

    /**
     * 新增：更新用户信息
     */
    Mono<UserResponse> updateUser(UpdateUserRequest request);

    Mono<UserResponse> updateUser(Mono<UpdateUserRequest> request);
}