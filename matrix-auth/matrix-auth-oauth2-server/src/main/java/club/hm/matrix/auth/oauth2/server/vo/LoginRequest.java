package club.hm.matrix.auth.oauth2.server.vo;

import club.hm.matrix.auth.oauth2.server.enums.ClientID;
import club.hm.matrix.auth.oauth2.server.enums.GrantType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest implements IGrantTypeRequest{
    @JsonProperty("grant_type")
    private GrantType grantType;

    /**
     * 授权类型 - 固定为 "code" (授权码模式)
     */
    @JsonProperty("response_type")
    private String responseType = "code";

    /**
     * 客户端ID
     */
    @JsonProperty("client_id")
    private ClientID clientId;

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

    private String code;

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