package club.hm.matrix.auth.oauth2.server.enums;

import lombok.Getter;

@Getter
public enum PermissionTypeEnum {
    NONE(0, "无"),
    MENU(1, "菜单权限"),
    OPERATION(2, "操作权限"),
    DATA(3, "数据权限");

    private final Integer code;
    private final String description;

    PermissionTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PermissionTypeEnum of(Integer code) {
        for (PermissionTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return NONE;
    }
}