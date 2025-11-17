package io.osins.matrix.auth.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String userId;        // Matrix用户ID，格式为@username:domain
    private String username;
    private String password;
    private String email;
    private String displayName;
    private String avatarUrl;
    private Boolean enabled;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    // 用户关联的角色
    private List<RoleDTO> roles;
}