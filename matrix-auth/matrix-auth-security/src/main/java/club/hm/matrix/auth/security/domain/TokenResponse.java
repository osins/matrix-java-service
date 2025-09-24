package club.hm.matrix.auth.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = false)
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    public String accessToken;
    public String refreshToken;
    public Integer expiresInMs;
}
