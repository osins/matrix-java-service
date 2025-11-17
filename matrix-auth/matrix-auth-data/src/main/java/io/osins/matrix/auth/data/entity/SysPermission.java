package io.osins.matrix.auth.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("sys_permission")
public record SysPermission(
        @Id Long id,
        String code,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
