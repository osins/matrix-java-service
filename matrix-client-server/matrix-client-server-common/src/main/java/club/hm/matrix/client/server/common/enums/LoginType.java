package club.hm.matrix.auth.client.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Matrix 登录认证方式类型枚举
 */
@Getter
public enum LoginType {
    UNKNOWN("", "未知"),
    /**
     * 用户名 + 密码 登录
     */
    PASSWORD("m.login.password", "用户名密码登录"),

    /**
     * ReCAPTCHA 验证
     */
    RECAPTCHA("m.login.recaptcha", "人机验证"),

    /**
     * 单点登录
     */
    SSO("m.login.sso", "单点登录"),

    /**
     * 邮箱身份验证
     */
    EMAIL_IDENTITY("m.login.email.identity", "邮箱身份验证"),

    /**
     * 手机号（MSISDN）身份验证
     */
    MSISDN("m.login.msisdn", "手机号身份验证"),

    /**
     * Dummy 验证（无需额外参数）
     */
    DUMMY("m.login.dummy", "简易验证"),

    /**
     * 注册令牌（registration_token）
     */
    REGISTRATION_TOKEN("m.login.registration_token", "注册令牌验证");

    private final String value;
    private final String desc;

    LoginType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 获取枚举对应的协议值，如 m.login.password
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * 根据协议值反解析枚举
     */
    @JsonCreator
    public static LoginType fromValue(String value) {
        if(value == null)
            return UNKNOWN;

        for (LoginType type : values()) {
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
