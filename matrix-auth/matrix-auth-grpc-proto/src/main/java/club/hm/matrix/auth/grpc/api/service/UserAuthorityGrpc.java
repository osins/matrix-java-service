package club.hm.matrix.auth.grpc.api.service;

import club.hm.matrix.auth.grpc.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * gRPC Reactive 客户端接口
 */
public interface UserAuthorityGrpc {

    // 根据用户ID加载用户
    Mono<User> loadUserById(long userId);

    // 根据用户名加载用户
    Mono<User> loadUserByUsername(String username);

    // 获取用户权限列表
    Mono<PermissionsList> getUserPermissions(long userId);

    // 检查用户是否有指定权限
    Mono<Boolean> hasPermission(long userId, String permissionCode);

    // 获取用户角色列表
    Mono<RolesList> getUserRoles(long userId);

    // 检查用户是否有指定角色
    Mono<Boolean> hasRole(long userId, String roleCode);

    // 批量加载用户信息
    Flux<User> loadUsersByIds(Iterable<Long> userIds);

    // 创建新用户
    Mono<UserResponse> createUser(CreateUserRequest request);

    // 更新用户信息
    Mono<User> updateUser(long userId, User user);
}