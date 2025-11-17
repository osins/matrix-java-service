package io.osins.matrix.user.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@Builder
@Table("matrix.user")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private Long id;

    private String userId; // Matrix用户ID，格式为@username:domain

    private String username;

    private String password;

    private String email;

    private String displayName;

    private String avatarUrl;

    private Boolean isActive;

    private Boolean enabled; // 用于认证

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt; // 添加更新时间字段

    private LocalDateTime lastLoginAt;
}