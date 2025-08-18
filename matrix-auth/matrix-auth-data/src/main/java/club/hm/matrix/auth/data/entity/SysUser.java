package club.hm.matrix.auth.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("sys_user")
public record SysUser(
        @Id Long id,
        String username,
        String password,
        Boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

