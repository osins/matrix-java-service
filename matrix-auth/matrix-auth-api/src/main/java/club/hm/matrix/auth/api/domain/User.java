package club.hm.matrix.auth.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
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
