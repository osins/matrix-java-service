package club.hm.matrix.auth.data.dto;

import lombok.Data;

@Data
public class PermissionDTO {
    private Long id;
    private String code;   // 权限编码（如 user:create, user:delete）
    private String name;   // 权限名称（如 创建用户, 删除用户）
}