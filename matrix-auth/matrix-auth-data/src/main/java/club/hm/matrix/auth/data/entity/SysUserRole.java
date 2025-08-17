package club.hm.matrix.auth.data.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("sys_user_role")
public record SysUserRole(
        Long userId,
        Long roleId
) {
}
