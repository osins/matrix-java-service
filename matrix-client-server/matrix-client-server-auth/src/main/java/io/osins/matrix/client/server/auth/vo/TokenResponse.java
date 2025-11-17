package io.osins.matrix.client.server.auth.vo;

import io.osins.matrix.client.server.common.domain.WellKnownClient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
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

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in_ms")
    private Integer expiresInMs;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("home_server")
    private String homeServer;

    // 新增 well_known 字段
    @JsonProperty("well_known")
    private WellKnownClient wellKnown;

    private String errcode;
    private String error;

    @JsonProperty("retry_after_ms")
    private String retryAfterMs;

    @JsonProperty("soft_logout")
    private Boolean softLogout;
}