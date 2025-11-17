package io.osins.matrix.auth.oauth2.server.vo;

import io.osins.matrix.auth.oauth2.server.enums.GrantType;
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
            GrantType type,
            String session,
            String response
    ) {}
}