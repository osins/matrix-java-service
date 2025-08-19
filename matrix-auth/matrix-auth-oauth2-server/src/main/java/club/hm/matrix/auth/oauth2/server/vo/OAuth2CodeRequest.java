package club.hm.matrix.auth.oauth2.server.vo;

import club.hm.matrix.auth.oauth2.server.enums.ClientID;
import club.hm.matrix.auth.oauth2.server.enums.GrantType;
import club.hm.matrix.auth.oauth2.server.enums.ResponseContentType;
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
public class OAuth2CodeRequest implements IGrantTypeRequest {
    @JsonProperty("grant_type")
    private GrantType grantType;
    @JsonProperty("client_id")
    private ClientID clientId;
    private String state;
    @JsonProperty("redirect_uri")
    private String redirectUri;
    @JsonProperty("response_type")
    private ResponseContentType responseContentType;
}
