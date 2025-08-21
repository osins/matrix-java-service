package club.hm.matrix.auth.data.service;

import club.hm.matrix.auth.data.converter.PermissionConverter;
import club.hm.matrix.auth.data.converter.RoleConverter;
import club.hm.matrix.auth.data.converter.UserConverter;
import club.hm.matrix.auth.data.dto.UserDTO;
import club.hm.matrix.auth.data.dto.RoleDTO;
import club.hm.matrix.auth.data.dto.PermissionDTO;
import club.hm.matrix.auth.data.entity.SysUser;
import club.hm.matrix.auth.data.entity.SysRole;
import club.hm.matrix.auth.data.entity.SysPermission;
import club.hm.matrix.auth.data.entity.SysUserRole;
import club.hm.matrix.shared.data.exception.DuplicateException;
import club.hm.matrix.shared.data.exception.NotFoundException;
import club.hm.matrix.auth.data.repos.*;
import club.hm.matrix.shared.snowflake.id.generator.BufferedIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.DuplicateFormatFlagsException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthorityService {
    private final R2dbcEntityTemplate template;
    private final SysUserRepository userRepo;
    private final SysUserRoleRepository userRoleRepo;
    private final SysRoleRepository roleRepo;
    private final SysRolePermissionRepository rolePermissionRepo;
    private final SysPermissionRepository permRepo;
    private final UserConverter userConverter;
    private final RoleConverter roleConverter;
    private final PermissionConverter permConverter;
    private final BufferedIdService bufferedIdService;

    /**
     * 创建新用户
     *
     * @param userDTO 用户信息DTO
     * @return 创建后的用户DTO
     */
    public Mono<UserDTO> createUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUsername() == null)
            return Mono.error(new IllegalArgumentException("用户名不能为空"));

        return userRepo.findByUsername(userDTO.getUsername())
                .flatMap(user -> Mono.<UserDTO>error(new DuplicateException("用户名已存在: " + userDTO.getUsername())))
                .switchIfEmpty(Mono.defer(() -> {
                    var user = userConverter.toEntity(userDTO.setId(bufferedIdService.getNextId()), userDTO.getPassword());
                    log.debug("Create user: {}", user);

                    return template.insert(user)
                            .flatMap(savedUser -> {
                                // 如果有角色信息，保存用户角色关联
                                if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
                                    return Flux.fromIterable(userDTO.getRoles())
                                            .flatMap(roleDTO -> userRoleRepo.save(new SysUserRole(savedUser.id(), roleDTO.getId(), LocalDateTime.now(), LocalDateTime.now())))
                                            .then(Mono.just(savedUser));
                                }
                                return Mono.just(savedUser);
                            })
                            .flatMap(this::loadUserWithRolesAndPermissions); // 返回完整DTO
                }));
    }

    public Mono<Integer> changePasswordByUsername(String username, String password) {
        return userRepo.existsByUsername(username)
                .flatMap(exists -> exists ? userRepo.changePasswordByUsername(username, password)
                        .doOnNext(count -> log.info("更新用户密码成功，mobile: {}", username))
                        .doOnError(throwable -> log.error("更新用户密码失败，mobile: {}, 异常: {}", username, throwable.getMessage(), throwable))
                        : Mono.error(new NotFoundException("用户不存在")));
    }

    /**
     * 更新已有用户信息
     *
     * @param userId  用户ID
     * @param userDTO 用户信息DTO（可部分更新）
     * @return 更新后的用户DTO
     */
    public Mono<UserDTO> updateUser(Long userId, UserDTO userDTO) {
        return userRepo.findById(userId)
                .flatMap(existingUser -> {
                    // 使用已有值，如果 DTO 有值则覆盖
                    var updatedUser = userConverter.toEntity(userDTO);
                    return userRepo.save(updatedUser);
                })
                .flatMap(this::loadUserWithRolesAndPermissions);
    }

    /**
     * 根据用户ID加载用户及其角色权限信息
     *
     * @param userId 用户ID
     * @return 包含角色和权限信息的用户DTO
     */
    public Mono<UserDTO> loadUserById(Long userId) {
        return userRepo.findById(userId)
                .flatMap(this::loadUserWithRolesAndPermissions);
    }

    /**
     * 根据用户名查找用户ID，然后加载完整信息
     *
     * @param username 用户名
     * @return 包含角色和权限信息的用户DTO
     */
    public Mono<UserDTO> loadUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .flatMap(this::loadUserWithRolesAndPermissions);
    }

    /**
     * 加载用户的角色和权限信息
     */
    private Mono<UserDTO> loadUserWithRolesAndPermissions(SysUser user) {
        return loadUserRoles(user.id())
                .flatMap(this::loadRolesWithPermissions)
                .collectList()
                .map(roles -> {
                    UserDTO userDTO = userConverter.toDTO(user);
                    userDTO.setRoles(roles);
                    return userDTO;
                });
    }

    /**
     * 加载用户的角色列表
     */
    private Flux<SysRole> loadUserRoles(Long userId) {
        return userRoleRepo.findByUserId(userId)
                .flatMap(userRole -> roleRepo.findById(userRole.roleId()));
    }

    /**
     * 为角色加载权限信息
     */
    private Mono<RoleDTO> loadRolesWithPermissions(SysRole role) {
        return loadRolePermissions(role.id())
                .collectList()
                .map(permissions -> {
                    RoleDTO roleDTO = roleConverter.toDTO(role);
                    List<PermissionDTO> permissionDTOs = permConverter.toDTOList(permissions);
                    roleDTO.setPermissions(permissionDTOs);
                    return roleDTO;
                });
    }

    /**
     * 加载角色的权限列表
     */
    private Flux<SysPermission> loadRolePermissions(Long roleId) {
        return rolePermissionRepo.findByRoleId(roleId)
                .flatMap(rolePermission -> permRepo.findById(rolePermission.permissionId()));
    }

    /**
     * 根据用户ID获取用户的所有权限（去重）
     *
     * @param userId 用户ID
     * @return 用户所有权限的去重列表
     */
    public Mono<List<PermissionDTO>> getUserPermissions(Long userId) {
        return loadUserById(userId)
                .map(userDTO -> userDTO.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .distinct() // 假设PermissionDTO重写了equals和hashCode
                        .toList()
                );
    }

    /**
     * 检查用户是否具有特定权限
     *
     * @param userId         用户ID
     * @param permissionCode 权限编码
     * @return 是否具有该权限
     */
    public Mono<Boolean> hasPermission(Long userId, String permissionCode) {
        return getUserPermissions(userId)
                .map(permissions -> permissions.stream()
                        .anyMatch(perm -> permissionCode.equals(perm.getCode()))
                );
    }

    /**
     * 获取用户的角色列表
     *
     * @param userId 用户ID
     * @return 用户角色DTO列表
     */
    public Mono<List<RoleDTO>> getUserRoles(Long userId) {
        return loadUserRoles(userId)
                .flatMap(this::loadRolesWithPermissions)
                .collectList();
    }

    /**
     * 检查用户是否具有特定角色
     *
     * @param userId   用户ID
     * @param roleCode 角色编码
     * @return 是否具有该角色
     */
    public Mono<Boolean> hasRole(Long userId, String roleCode) {
        return getUserRoles(userId)
                .map(roles -> roles.stream()
                        .anyMatch(role -> roleCode.equals(role.getCode()))
                );
    }

    /**
     * 批量加载用户信息（包含角色权限）
     *
     * @param userIds 用户ID列表
     * @return 用户DTO列表
     */
    public Flux<UserDTO> loadUsersByIds(List<Long> userIds) {
        return Flux.fromIterable(userIds)
                .flatMap(this::loadUserById);
    }
}