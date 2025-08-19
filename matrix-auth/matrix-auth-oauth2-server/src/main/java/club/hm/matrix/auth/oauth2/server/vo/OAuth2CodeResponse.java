package club.hm.matrix.auth.oauth2.server.vo;

import club.hm.matrix.auth.oauth2.server.enums.ClientID;
import club.hm.matrix.auth.oauth2.server.enums.ResponseContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2CodeResponse implements Serializable {
    private String code;
    private String state;
    private String clientId;
    private ResponseContentType responseContentType;
}
