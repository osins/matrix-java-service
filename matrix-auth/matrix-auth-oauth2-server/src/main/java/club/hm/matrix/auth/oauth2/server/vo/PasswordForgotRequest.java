package club.hm.matrix.auth.oauth2.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordForgotRequest {
    private String code;
    private String mobile;
    private String newPassword;
}
