package club.hm.matrix.client.server.auth.vo;

import club.hm.matrix.client.server.common.enums.LoginType;
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
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Identifier {
        private String type;
        private String user;
    }
}