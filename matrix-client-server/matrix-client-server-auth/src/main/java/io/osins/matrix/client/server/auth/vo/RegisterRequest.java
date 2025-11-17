package io.osins.matrix.client.server.auth.vo;

import io.osins.matrix.client.server.common.enums.RegisterAuthType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterRequest{

        Auth auth;

        @JsonProperty("device_id")
        String deviceId;

        @JsonProperty("initial_device_display_name")
        String initialDeviceDisplayName;

        String username;

        String password;

        @JsonProperty("inhibit_login")
        Boolean inhibitLogin;

    @Data
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Auth {
        RegisterAuthType type; // m.login.dummy, m.login.recaptcha, m.login.email.identity ...
        String session;
        String response; // 比如 recaptcha 的验证码回答，可选
    }
}