package club.hm.matrix.auth.security.enums;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Accessors(chain = true)
public enum JWTokenType implements Serializable {
    NONE("none", "无令牌"),
    ACCESS_TOKEN("access", "权限令牌"),
    REFRESH_TOKEN("refresh", "刷新令牌");

    private final String code;
    private final String desc;

    JWTokenType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static JWTokenType of(String code) {
        for (JWTokenType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return NONE;
    }

    public boolean isNone() {
        return this == NONE;
    }

    public boolean isAccess() {
        return this == ACCESS_TOKEN;
    }

    public boolean isRefresh() {
        return this == REFRESH_TOKEN;
    }

    @Override
    public String toString(){
        return code;
    }
}
