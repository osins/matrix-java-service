package io.osins.matrix.auth.security.domain;

import io.osins.matrix.auth.security.enums.JWTokenType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JwtSetting {
    private String jti;
    private JWTokenType type;
    private String secretKey;
    private String zoneId;
    private CustomPrincipal subject;

    public static JwtSetting create(){
        return new JwtSetting();
    }

    public static JwtSetting base(String secretKey, String zoneId){
        return JwtSetting.create().setSecretKey(secretKey).setZoneId(zoneId);
    }
}
