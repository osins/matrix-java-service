package club.hm.matrix.auth.oauth2.server.enums;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Accessors(chain = true)
public enum RoleStatus {
    NONE(0, "无"),
    NORMAL(1, "正常"),
    DISABLED(2, "禁用"),
    DELETED(3, "删除"),
    ;

    private final Integer code;
    private final String desc;

    RoleStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RoleStatus of(Integer code) {
        for (RoleStatus item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return NONE;
    }

    public boolean isDeleted() {
        return this.code.equals(DELETED.code);
    }

    public boolean isNormal() {
        return this.code.equals(NORMAL.code);
    }

    public boolean isDisabled() {
        return this.code.equals(DISABLED.code);
    }

    public boolean isNone() {
        return this.code.equals(NONE.code);
    }
}
