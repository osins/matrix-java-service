package club.hm.matrix.auth.oauth2.server.vo;

import club.hm.matrix.auth.oauth2.server.enums.ClientID;
import club.hm.matrix.auth.oauth2.server.enums.GrantType;
import club.hm.matrix.auth.oauth2.server.enums.ResponseContentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2SendSmsCodeRequest implements Serializable {
    private GrantType grantType;
    private ClientID clientId;
    private String state;
    private String mobile;
    private String redirectUri;
    private ResponseContentType responseContentType;

    public void setGrantType(String grantType) {
        this.grantType = GrantType.of(grantType);
    }

    public void setClientId(String clientId) {
        this.clientId = ClientID.of(clientId);
    }

    public void setResponseContentType(String responseType) {
        this.responseContentType = ResponseContentType.of(responseType);
    }
}
