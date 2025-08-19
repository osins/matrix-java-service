package club.hm.matrix.auth.oauth2.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2 授权请求参数
 * 遵循 RFC 6749 标准
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2AuthorizationRequest {
    /**
     * 授权类型 - 固定为 "code" (授权码模式)
     */
    @JsonProperty("response_type")
    private String responseType = "code";

    /**
     * 客户端ID
     */
    @JsonProperty("client_id")
    private String clientId;

    /**
     * 重定向URI
     */
    @JsonProperty("redirect_uri")
    private String redirectUri;

    /**
     * 授权范围
     */
    @JsonProperty("scope")
    private String scope;

    /**
     * 状态参数
     */
    @JsonProperty("state")
    private String state;

    /**
     * 额外参数
     */
    private final Map<String, Serializable> additionalParameters = new HashMap<>();

    /**
     * 添加额外参数
     */
    public void addAdditionalParameter(String key, Serializable value) {
        if (key != null && !key.isBlank()) {
            additionalParameters.put(key, value);
        }
    }
}