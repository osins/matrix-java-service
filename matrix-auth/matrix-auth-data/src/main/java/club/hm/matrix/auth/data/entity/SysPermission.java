package club.hm.matrix.auth.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("sys_permission")
public record SysPermission(
        @Id Long id,
        String code,
        String description
) {
}
