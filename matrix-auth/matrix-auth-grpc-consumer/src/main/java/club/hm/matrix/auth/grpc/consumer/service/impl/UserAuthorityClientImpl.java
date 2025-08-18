package club.hm.matrix.auth.grpc.consumer.service.impl;

import club.hm.matrix.auth.grpc.*;
import club.hm.matrix.auth.grpc.api.service.UserAuthorityGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthorityClientImpl implements UserAuthorityGrpc {

    private final Mono<ReactorUserAuthorityServiceGrpc.ReactorUserAuthorityServiceStub> reactorStub;

    @Override
    public Mono<UserResponse> loadUserById(LoadUserByIdRequest request) {
        return null;
    }

    @Override
    public Mono<UserResponse> loadUserById(Mono<LoadUserByIdRequest> request) {
        return null;
    }

    @Override
    public Mono<UserResponse> loadUserByUsername(LoadUserByUsernameRequest request) {
        return reactorStub.flatMap(stub ->
                stub.loadUserByUsername(request)
                        .doOnError(throwable -> log.error("loadUserByUsername error", throwable))
        );
    }

    @Override
    public Mono<UserResponse> loadUserByUsername(Mono<LoadUserByUsernameRequest> request) {
        return null;
    }

    @Override
    public Mono<PermissionsResponse> getUserPermissions(GetUserPermissionsRequest request) {
        return null;
    }

    @Override
    public Mono<PermissionsResponse> getUserPermissions(Mono<GetUserPermissionsRequest> request) {
        return null;
    }

    @Override
    public Mono<HasPermissionResponse> hasPermission(HasPermissionRequest request) {
        return null;
    }

    @Override
    public Mono<HasPermissionResponse> hasPermission(Mono<HasPermissionRequest> request) {
        return null;
    }

    @Override
    public Mono<RolesResponse> getUserRoles(GetUserRolesRequest request) {
        return null;
    }

    @Override
    public Mono<RolesResponse> getUserRoles(Mono<GetUserRolesRequest> request) {
        return null;
    }

    @Override
    public Mono<HasRoleResponse> hasRole(HasRoleRequest request) {
        return null;
    }

    @Override
    public Mono<HasRoleResponse> hasRole(Mono<HasRoleRequest> request) {
        return null;
    }

    @Override
    public Flux<UserResponse> loadUsersByIds(LoadUsersByIdsRequest request) {
        return null;
    }

    @Override
    public Flux<UserResponse> loadUsersByIds(Mono<LoadUsersByIdsRequest> request) {
        return null;
    }

    @Override
    public Mono<UserResponse> createUser(CreateUserRequest request) {
        return Mono.deferContextual(ctx -> {
                    return reactorStub.flatMap(stub ->
                            stub.createUser(CreateUserRequest.newBuilder()
                                            .setUser(request.getUser())
                                            .build())
                                    .doOnError(throwable -> log.error("createUser error: {}, \nuser: {}", throwable.getMessage(), request.getUser(), throwable))
                    );
                }
        );
    }

    @Override
    public Mono<UserResponse> createUser(Mono<CreateUserRequest> request) {
        return null;
    }

    @Override
    public Mono<UserResponse> updateUser(UpdateUserRequest request) {
        return null;
    }

    @Override
    public Mono<UserResponse> updateUser(Mono<UpdateUserRequest> request) {
        return null;
    }
}
