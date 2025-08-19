package club.hm.matrix.auth.client.vo;

import club.hm.matrix.auth.client.enums.LoginType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {

    private LoginType type;

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