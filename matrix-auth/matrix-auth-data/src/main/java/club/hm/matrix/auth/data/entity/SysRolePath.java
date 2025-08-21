package club.hm.matrix.auth.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("sys_role_path")
public record SysRolePath(
        @Id Long id,
        String roleName,
        String pathPattern,
        String httpMethod,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}