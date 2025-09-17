package club.hm.matrix.auth.client.vo;

import club.hm.matrix.auth.client.enums.LoginType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {

    private LoginType type;

    private String address;

    @JsonProperty("device_id")
    private String deviceId;

    private Identifier identifier;

    @JsonProperty("initial_device_display_name")
    private String initialDeviceDisplayName;

    private String medium;
    private String password;

    @JsonProperty("refresh_token")
    private boolean refreshToken;

    private String token;
    private String user;

    @JsonProperty("inhibit_login")
    private boolean inhibitLogin;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Identifier {
        private String type;
        private String user;
    }
}