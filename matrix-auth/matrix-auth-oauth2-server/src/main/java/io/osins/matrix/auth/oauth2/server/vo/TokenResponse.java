package io.osins.matrix.auth.oauth2.server.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@Accessors(chain = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in_ms")
    private Integer expiresInMs;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("home_server")
    private String homeServer;
}