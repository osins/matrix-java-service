package io.osins.matrix.auth.data.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoleDTO {
    private Long id;
    private String code;  // 角色编码（如 ROLE_ADMIN, ROLE_USER）
    private String name;  // 角色名称（如 管理员, 普通用户）
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    // 角色拥有的权限
    private List<PermissionDTO> permissions;
}