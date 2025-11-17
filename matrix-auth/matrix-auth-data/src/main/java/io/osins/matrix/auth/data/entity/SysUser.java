package io.osins.matrix.auth.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("matrix.user")
public record SysUser(
        @Id Long id,
        String userId,        // Matrix用户ID，格式为@username:domain
        String username,
        String password,
        String email,
        String displayName,
        String avatarUrl,
        Boolean isActive,
        Boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLoginAt
) {}

