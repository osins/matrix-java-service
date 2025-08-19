package club.hm.matrix.auth.oauth2.server.vo;

import club.hm.matrix.auth.oauth2.server.enums.ClientID;
import club.hm.matrix.auth.oauth2.server.enums.GrantType;
import club.hm.matrix.auth.oauth2.server.enums.ResponseContentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractGrantTypeRequest implements IGrantTypeRequest {
    private ClientID clientId;
    private String code;
    private String state;
    private String scope;
    private GrantType grantType;
    private String redirectUri;
    private ResponseContentType responseContentType;

    public void setClientId(String clientId){
        this.clientId = ClientID.of(clientId);
    }

    public void setGrantType(String grantType){
        this.grantType = GrantType.of(grantType);
    }
}
