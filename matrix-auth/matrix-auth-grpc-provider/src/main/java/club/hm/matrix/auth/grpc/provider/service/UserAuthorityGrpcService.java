package club.hm.matrix.auth.grpc.provider.service;

import club.hm.matrix.auth.data.service.UserAuthorityService;
import club.hm.matrix.auth.grpc.*;
import club.hm.matrix.auth.grpc.api.service.UserAuthorityGrpc;
import club.hm.matrix.auth.grpc.provider.converter.UserAuthorityGrpcConverter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserAuthorityGrpcService extends ReactorUserAuthorityServiceGrpc.UserAuthorityServiceImplBase implements UserAuthorityGrpc {

    private final UserAuthorityService userAuthorityService;
    private final UserAuthorityGrpcConverter converter;

    @PostConstruct
    public void init() {
        log.info("UserAuthorityGrpcService init");
    }

    @Override
    public Mono<User> loadUserById(long userId) {
        return null;
    }

    @Override
    public Mono<User> loadUserByUsername(String username) {
        return null;
    }

    @Override
    public Mono<PermissionsList> getUserPermissions(long userId) {
        return null;
    }

    @Override
    public Mono<Boolean> hasPermission(long userId, String permissionCode) {
        return null;
    }

    @Override
    public Mono<RolesList> getUserRoles(long userId) {
        return null;
    }

    @Override
    public Mono<Boolean> hasRole(long userId, String roleCode) {
        return null;
    }

    @Override
    public Flux<User> loadUsersByIds(Iterable<Long> userIds) {
        return null;
    }

    @Override
    public Mono<UserResponse> createUser(CreateUserRequest request) {
        return userAuthorityService.createUser(converter.fromGrpcUser(request.getUser()))
                .map(converter::toGrpcUser)
                .doOnError(t -> log.error("创建用户失败: {}, {}", request, t.getMessage(), t));
    }

    @Override
    public Mono<User> updateUser(long userId, User user) {
        return null;
    }
}