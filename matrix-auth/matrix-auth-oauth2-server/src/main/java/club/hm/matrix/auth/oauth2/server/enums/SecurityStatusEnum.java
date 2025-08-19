package club.hm.matrix.auth.oauth2.server.enums;

import lombok.Getter;

@Getter
public enum SecurityStatusEnum {
    DISABLED(0, "禁用"),
    ENABLED(1, "启用");

    private final Integer code;
    private final String description;

    SecurityStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SecurityStatusEnum getByCode(Integer code) {
        for (var value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return DISABLED;
    }
}
