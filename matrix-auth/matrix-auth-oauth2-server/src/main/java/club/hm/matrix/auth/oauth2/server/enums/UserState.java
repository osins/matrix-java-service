package club.hm.matrix.auth.oauth2.server.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
public enum UserState {
    NONE(0, "未知"),
    ENABLED(1, "正常"),
    DISABLED(2, "禁用"),
    ;

    private final Integer code;
    private final String desc;

    UserState(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserState of(Integer code) {
        for (UserState value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return NONE;
    }

    public boolean isEnable() {
        return this == ENABLED;
    }

    public boolean isDisable() {
        return this == DISABLED;
    }

    public boolean isNone() {
        return this == NONE;
    }
}
