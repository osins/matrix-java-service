package io.osins.matrix.user.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
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

    @Column("user_id")
    private String userId; // Matrix用户ID，格式为@username:domain

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("email")
    private String email;

    @Column("display_name")
    private String displayName;

    @Column("avatar_url")
    private String avatarUrl;

    @Column("is_active")
    private Boolean isActive;

    @Column("enabled")
    private Boolean enabled; // 用于认证

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt; // 添加更新时间字段

    @Column("last_login_at")
    private LocalDateTime lastLoginAt;
}