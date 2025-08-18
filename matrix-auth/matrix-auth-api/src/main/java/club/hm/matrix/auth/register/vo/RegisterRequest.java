package club.hm.matrix.auth.register.vo;


import club.hm.matrix.auth.api.enums.RegisterAuthType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegisterRequest(

        Auth auth,

        @JsonProperty("device_id")
        String deviceId,

        @JsonProperty("initial_device_display_name")
        String initialDeviceDisplayName,

        String username,

        String password,

        @JsonProperty("inhibit_login")
        Boolean inhibitLogin
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Auth(
            RegisterAuthType type, // m.login.dummy, m.login.recaptcha, m.login.email.identity ...
            String session,
            String response // 比如 recaptcha 的验证码回答，可选
    ) {}
}