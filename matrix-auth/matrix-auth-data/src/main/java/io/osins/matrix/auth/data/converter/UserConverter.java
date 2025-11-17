package io.osins.matrix.auth.data.converter;

import io.osins.matrix.auth.data.dto.RoleDTO;
import io.osins.matrix.auth.data.dto.UserDTO;
import io.osins.matrix.auth.data.entity.SysUser;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户实体与DTO转换器
 *
 * @author your-name
 */
@Component
public class UserConverter {

    /**
     * 将SysUser实体转换为UserDTO
     * 注意：此方法不会填充roles信息，需要额外处理
     *
     * @param sysUser 用户实体
     * @return UserDTO对象，如果输入为null则返回null
     */
    public UserDTO toDTO(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }

        return UserDTO.builder()
                .id(sysUser.id())
                .userId(sysUser.userId())
                .username(sysUser.username())
                .password(sysUser.password())
                .email(sysUser.email())
                .displayName(sysUser.displayName())
                .avatarUrl(sysUser.avatarUrl())
                .isActive(sysUser.isActive())
                .enabled(sysUser.enabled())
                .createdAt(sysUser.createdAt())
                .updatedAt(sysUser.updatedAt())
                .lastLoginAt(sysUser.lastLoginAt())
                .roles(Collections.emptyList())
                .build();
    }

    /**
     * 将SysUser实体转换为UserDTO，并设置角色信息
     *
     * @param sysUser 用户实体
     * @param roles   角色列表
     * @return UserDTO对象
     */
    public UserDTO toDTO(SysUser sysUser, List<RoleDTO> roles) {
        UserDTO userDTO = toDTO(sysUser);
        if (userDTO != null) {
            userDTO.setRoles(roles != null ? roles : Collections.emptyList());
        }
        return userDTO;
    }

    /**
     * 批量将SysUser实体列表转换为UserDTO列表
     *
     * @param sysUsers 用户实体列表
     * @return UserDTO列表
     */
    public List<UserDTO> toDTOList(List<SysUser> sysUsers) {
        if (CollectionUtils.isEmpty(sysUsers)) {
            return Collections.emptyList();
        }

        return sysUsers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将UserDTO转换为SysUser实体
     * 注意：password字段将被设置为null，需要额外处理
     *
     * @param userDTO 用户DTO
     * @return SysUser对象，如果输入为null则返回null
     */
    public SysUser toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        // 如果 userId 为空，生成一个默认的 userId
        String userId = userDTO.getUserId();
        if (userId == null && userDTO.getUsername() != null) {
            userId = "@" + userDTO.getUsername() + ":matrix.org";
        }

        return new SysUser(
                userDTO.getId(),
                userId,
                userDTO.getUsername(),
                null, // password需要单独处理
                userDTO.getEmail(),
                userDTO.getDisplayName(),
                userDTO.getAvatarUrl(),
                userDTO.getIsActive(),
                userDTO.getEnabled(),
                userDTO.getCreatedAt(),
                userDTO.getUpdatedAt(),
                userDTO.getLastLoginAt()
        );
    }

    /**
     * 将UserDTO转换为SysUser实体，并指定密码
     *
     * @param userDTO  用户DTO
     * @param password 密码（通常是加密后的密码）
     * @return SysUser对象
     */
    public SysUser toEntity(UserDTO userDTO, String password) {
        if (userDTO == null) {
            return null;
        }

        // 如果 userId 为空，生成一个默认的 userId
        String userId = userDTO.getUserId();
        if (userId == null && userDTO.getUsername() != null) {
            userId = "@" + userDTO.getUsername() + ":matrix.org";
        }

        return new SysUser(
                userDTO.getId(),
                userId,
                userDTO.getUsername(),
                password,
                userDTO.getEmail(),
                userDTO.getDisplayName(),
                userDTO.getAvatarUrl(),
                userDTO.getIsActive(),
                userDTO.getEnabled(),
                userDTO.getCreatedAt(),
                userDTO.getUpdatedAt(),
                userDTO.getLastLoginAt()
        );
    }

    /**
     * 批量将UserDTO列表转换为SysUser实体列表
     *
     * @param userDTOs 用户DTO列表
     * @return SysUser实体列表
     */
    public List<SysUser> toEntityList(List<UserDTO> userDTOs) {
        if (CollectionUtils.isEmpty(userDTOs)) {
            return Collections.emptyList();
        }

        return userDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * 更新实体对象的字段（排除id和密码）
     *
     * @param target 目标实体
     * @param source DTO数据源
     * @return 更新后的实体
     */
    public SysUser updateEntity(SysUser target, UserDTO source) {
        if (target == null || source == null) {
            return target;
        }

        // 重新构造实体，保留原始ID和密码
        // 如果 source 中的 userId 为空，保留原始 userId
        String userId = source.getUserId();
        if (userId == null) {
            userId = target.userId();
        }

        return new SysUser(
                target.id(),
                userId,
                source.getUsername(),
                target.password(), // 保持原密码
                source.getEmail(),
                source.getDisplayName(),
                source.getAvatarUrl(),
                source.getIsActive(),
                source.getEnabled(),
                target.createdAt(),
                source.getUpdatedAt(), // 更新时间
                source.getLastLoginAt()
        );
    }
}