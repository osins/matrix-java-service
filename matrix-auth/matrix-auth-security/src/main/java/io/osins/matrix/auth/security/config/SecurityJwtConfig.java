package io.osins.matrix.auth.security.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hm.security.jwt")
public class SecurityJwtConfig {
    private String keyId;
    private String secret;
    private Long expiration;
    private Long refreshExpiration;
    private String zoneId;
    private Long adminId;
    private Boolean roleCache;
}
