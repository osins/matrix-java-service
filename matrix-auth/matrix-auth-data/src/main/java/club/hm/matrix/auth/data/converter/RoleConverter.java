package club.hm.matrix.auth.data.converter;

import club.hm.matrix.auth.data.dto.RoleDTO;
import club.hm.matrix.auth.data.entity.SysRole;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色转换器
 * 用于 SysRole 实体和 RoleDTO 之间的转换
 */
@Component
public class RoleConverter {

    /**
     * 将 SysRole 实体转换为 RoleDTO
     *
     * @param sysRole SysRole 实体
     * @return RoleDTO
     */
    public RoleDTO toDTO(SysRole sysRole) {
        if (sysRole == null) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(sysRole.id());
        roleDTO.setName(sysRole.name());
        // 注意：SysRole 中没有 code 字段，这里可能需要根据业务逻辑设置
        // roleDTO.setCode(generateRoleCode(sysRole));

        // permissions 需要单独设置，因为 SysRole 中没有包含权限信息
        // roleDTO.setPermissions(permissions);

        return roleDTO;
    }

    /**
     * 将 SysRole 实体列表转换为 RoleDTO 列表
     *
     * @param sysRoles SysRole 实体列表
     * @return RoleDTO 列表
     */
    public List<RoleDTO> toDTOList(List<SysRole> sysRoles) {
        if (sysRoles == null) {
            return null;
        }

        return sysRoles.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将 RoleDTO 转换为 SysRole 实体
     *
     * @param roleDTO RoleDTO
     * @return SysRole 实体
     */
    public SysRole toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }

        return new SysRole(
                roleDTO.getId(),
                roleDTO.getName(),
                roleDTO.getDescription(),
                roleDTO.getCreatedAt(),
                roleDTO.getUpdatedAt()
        );
    }

    /**
     * 将 RoleDTO 列表转换为 SysRole 实体列表
     *
     * @param roleDTOs RoleDTO 列表
     * @return SysRole 实体列表
     */
    public List<SysRole> toEntityList(List<RoleDTO> roleDTOs) {
        if (roleDTOs == null) {
            return null;
        }

        return roleDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * 根据 SysRole 生成角色编码的示例方法
     * 实际业务中可能需要根据具体需求来实现
     *
     * @param sysRole SysRole 实体
     * @return 角色编码
     */
    private String generateRoleCode(SysRole sysRole) {
        if (sysRole.name() == null) {
            return null;
        }
        // 简单示例：将角色名称转换为大写并添加 ROLE_ 前缀
        return "ROLE_" + sysRole.name().toUpperCase().replace(" ", "_");
    }
}