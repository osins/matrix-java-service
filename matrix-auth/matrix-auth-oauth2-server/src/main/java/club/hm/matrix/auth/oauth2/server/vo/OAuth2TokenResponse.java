package club.hm.matrix.auth.oauth2.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OAuth2TokenResponse implements Serializable {
    private String tokenType;
    private String accessToken;
    /**
     * 过期时间（单位：秒)
     */
    private int expiresIn;
    private String refreshToken;
    private int refreshExpiresIn;

    public static OAuth2TokenResponse builder(){
        return new OAuth2TokenResponse();
    }

    public static OAuth2TokenResponse of(String accessToken, String tokenType, int expiresIn, String refreshToken, int refreshExpiresIn) {
        return new OAuth2TokenResponse(accessToken, tokenType, expiresIn, refreshToken, refreshExpiresIn);
    }
}