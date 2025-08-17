package club.hm.matrix.auth.data.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("sys_role_permission")
public record SysRolePermission(
        Long roleId,
        Long permissionId
) {
}
