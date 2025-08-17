package club.hm.matrix.auth.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("sys_role")
public record SysRole(
        @Id Long id,
        String name,
        String description
) {
}
