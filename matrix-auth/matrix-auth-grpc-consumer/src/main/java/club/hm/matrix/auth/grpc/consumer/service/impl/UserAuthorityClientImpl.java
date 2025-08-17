package club.hm.matrix.auth.grpc.consumer.service.impl;

import club.hm.matrix.auth.grpc.*;
import club.hm.matrix.auth.grpc.api.service.UserAuthorityGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthorityClientImpl implements UserAuthorityGrpc {

    private final Mono<ReactorUserAuthorityServiceGrpc.ReactorUserAuthorityServiceStub> reactorStub;

    @Override
    public Mono<User> loadUserById(long userId) {
        return reactorStub.flatMap(stub ->
                stub.loadUserById(LoadUserByIdRequest.newBuilder()
                                .setUserId(userId)
                                .build())
                        .flatMap(resp -> resp.hasUser() ? Mono.just(resp.getUser())
                                : Mono.error(new RuntimeException(resp.getError())))
        );
    }

    @Override
    public Mono<User> loadUserByUsername(String username) {
        return reactorStub.flatMap(stub ->
                stub.loadUserByUsername(LoadUserByUsernameRequest.newBuilder()
                                .setUsername(username)
                                .build())
                        .flatMap(resp -> resp.hasUser() ? Mono.just(resp.getUser())
                                : Mono.error(new RuntimeException(resp.getError())))
        );
    }

    @Override
    public Mono<PermissionsList> getUserPermissions(long userId) {
        return reactorStub.flatMap(stub ->
                stub.getUserPermissions(GetUserPermissionsRequest.newBuilder()
                                .setUserId(userId)
                                .build())
                        .flatMap(resp -> resp.hasPermissions() ? Mono.just(resp.getPermissions())
                                : Mono.error(new RuntimeException(resp.getError())))
        );
    }

    @Override
    public Mono<Boolean> hasPermission(long userId, String permissionCode) {
        return reactorStub.flatMap(stub ->
                stub.hasPermission(HasPermissionRequest.newBuilder()
                                .setUserId(userId)
                                .setPermissionCode(permissionCode)
                                .build())
                        .map(HasPermissionResponse::getHasPermission)
        );
    }

    @Override
    public Mono<RolesList> getUserRoles(long userId) {
        return reactorStub.flatMap(stub ->
                stub.getUserRoles(GetUserRolesRequest.newBuilder()
                                .setUserId(userId)
                                .build())
                        .flatMap(resp -> resp.hasRoles() ? Mono.just(resp.getRoles())
                                : Mono.error(new RuntimeException(resp.getError())))
        );
    }

    @Override
    public Mono<Boolean> hasRole(long userId, String roleCode) {
        return reactorStub.flatMap(stub ->
                stub.hasRole(HasRoleRequest.newBuilder()
                                .setUserId(userId)
                                .setRoleCode(roleCode)
                                .build())
                        .map(HasRoleResponse::getHasRole)
        );
    }

    @Override
    public Flux<User> loadUsersByIds(Iterable<Long> userIds) {
        return reactorStub.flatMapMany(stub ->
                stub.loadUsersByIds(LoadUsersByIdsRequest.newBuilder()
                                .addAllUserIds((List<Long>) userIds)
                                .build())
                        .map(UserResponse::getUser)
        );
    }

    @Override
    public Mono<UserResponse> createUser(CreateUserRequest request) {
        return reactorStub.flatMap(stub ->
                stub.createUser(CreateUserRequest.newBuilder()
                                .setUser(request.getUser())
                                .build())
                        .doOnError(throwable -> log.error("createUser error: {}, \nuser: {}", throwable.getMessage(), request.getUser(), throwable))
        );
    }

    @Override
    public Mono<User> updateUser(long userId, User user) {
        return reactorStub.flatMap(stub ->
                stub.updateUser(UpdateUserRequest.newBuilder()
                                .setUserId(userId)
                                .setUser(user)
                                .build())
                        .flatMap(resp -> resp.hasUser() ? Mono.just(resp.getUser())
                                : Mono.error(new RuntimeException(resp.getError())))
        );
    }
}
