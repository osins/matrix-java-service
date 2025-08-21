package club.hm.matrix.auth.data.entity;

import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("sys_role_permission")
public record SysRolePermission(
        Long roleId,
        Long permissionId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
