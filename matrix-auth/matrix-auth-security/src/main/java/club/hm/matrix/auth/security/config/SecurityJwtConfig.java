package club.hm.matrix.auth.security.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hm.security.jwt")
public class SecurityJwtConfig {
    private String secret;
    private Integer expiration;
    private String zoneId;
    private Long adminId;
    private Boolean roleCache;
}
