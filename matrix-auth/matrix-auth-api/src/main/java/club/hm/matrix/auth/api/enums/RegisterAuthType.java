package club.hm.matrix.auth.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RegisterAuthType {
    UNKNOWN("", "未知"),
    DUMMY("m.login.dummy", "测试/开发"),
    PASSWORD("m.login.password", "用户密码认证"),
    RECAPTCHA("m.login.recaptcha", "验证码"),
    EMAIL_IDENTITY("m.login.email.identity", "邮箱验证"),
    MSISDN("m.login.msisdn", "手机号验证"),
    TOKEN("m.login.token", "token 登录");

    private final String value;
    private final String desc;

    RegisterAuthType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RegisterAuthType fromValue(String value) {
        if(value==null)
            return UNKNOWN;

        for (RegisterAuthType type : RegisterAuthType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        return UNKNOWN;
    }

    public boolean isUnknown() {
        return this == UNKNOWN;
    }
}