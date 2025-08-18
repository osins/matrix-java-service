package club.hm.matrix.auth.login.vo;

import club.hm.matrix.auth.api.enums.LoginAuthType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {

    private LoginAuthType type;

    private String password;

    private Identifier identifier;

    @JsonProperty("initial_device_display_name")
    private String initialDeviceDisplayName;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Identifier {
        private String type;
        private String user;
    }
}