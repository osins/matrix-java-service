package club.hm.matrix.auth.data.converter;

import club.hm.matrix.auth.data.dto.RoleDTO;
import club.hm.matrix.auth.data.dto.UserDTO;
import club.hm.matrix.auth.data.entity.SysUser;
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
                .username(sysUser.username())
                .password(sysUser.password())
                .email(sysUser.email())
                .enabled(sysUser.enabled())
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

        return new SysUser(
                userDTO.getId(),
                userDTO.getUsername(),
               null, // password需要单独处理
                userDTO.getEmail(),
                userDTO.getEnabled(),
                userDTO.getCreatedAt(),
                userDTO.getUpdatedAt()
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

        return new SysUser(
                userDTO.getId(),
                userDTO.getUsername(),
                password,
                userDTO.getEmail(),
                userDTO.getEnabled(),
                userDTO.getCreatedAt(),
                userDTO.getUpdatedAt()
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

        // 这里假设SysUser有setter方法，如果是record则需要重新构造
        return new SysUser(
                target.id(), // 保持原ID
                source.getUsername(),
                target.password(), // 保持原密码
                source.getEmail(),
                source.getEnabled(),
                source.getCreatedAt(),
                source.getUpdatedAt()
        );
    }
}