package club.hm.matrix.auth.security.domain;

import club.hm.matrix.auth.security.enums.JWTokenType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JwtSetting {
    private String jti;
    private JWTokenType type;
    private String secretKey;
    private String keyId;
    private String zoneId;
    private int expiration;
    private CustomPrincipal subject;

    public static JwtSetting create(){
        return new JwtSetting();
    }

    public static JwtSetting base(String secretKey, int expiration, String keyId, String zoneId){
        return JwtSetting.create().setSecretKey(secretKey).setExpiration(expiration).setKeyId(keyId).setZoneId(zoneId);
    }
}
