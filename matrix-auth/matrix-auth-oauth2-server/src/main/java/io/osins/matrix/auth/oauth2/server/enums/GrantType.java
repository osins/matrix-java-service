package io.osins.matrix.auth.oauth2.server.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum GrantType {
    UNKNOWN("none", "不支持的法授权模式"),
    AUTHORIZATION_CODE("authorization_code", "授权码模式"),
    PASSWORD("password", "密码模式"),
    CLIENT_CREDENTIALS("client_credentials", "客户端模式"),
    REFRESH_TOKEN("refresh_token", "刷新令牌模式"),
    KICK_TOKEN("kick_token", "剔除令牌模式"),
    DEVICE_CODE("device_code", "设备模式"),
    SMS_CODE("sms_code", "短信模式"),
    EMAIL_CODE("email_code", "邮箱模式"),
    CHANGE_PASSWORD("change_password", "修改密码模式"),
    ;

    @JsonValue
    private final String type;
    private final String description;

    GrantType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public boolean isNone() {
        return UNKNOWN.equals(this);
    }

    public boolean isPassword(){
        return PASSWORD.equals(this);
    }

    public boolean isAuthorizationCode(){
        return AUTHORIZATION_CODE.equals(this);
    }

    @JsonCreator
    public static GrantType of(String type){
        for (GrantType value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }

        return UNKNOWN;
    }
}
