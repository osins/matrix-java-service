package club.hm.matrix.user.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@Table("matrix_user")
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

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;
}