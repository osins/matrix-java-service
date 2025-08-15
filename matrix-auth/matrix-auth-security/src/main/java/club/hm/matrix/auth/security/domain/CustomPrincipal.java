package club.hm.matrix.auth.security.domain;

import club.hm.matrix.auth.security.constant.SecuritySettings;
import club.hm.matrix.auth.security.enums.ClientType;
import club.hm.matrix.auth.security.enums.JWTokenType;
import club.hm.matrix.auth.security.enums.PlatformType;
import club.hm.matrix.auth.security.enums.UserState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Data
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomPrincipal implements Serializable, UserDetails {
    private String jti;
    private JWTokenType type;
    private Long userId;
    private String username;
    private String mobile;
    private String nickname;
    private UserState status;
    private ClientType clientType;
    private PlatformType platformType;
    private JtiSet jtiSet;

    private boolean accountExpired;
    private boolean credentialsExpired;
    private boolean locked;
    private boolean kicked;

    private Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

    public static CustomPrincipal create() {
        return new CustomPrincipal();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities == null ? Collections.emptyList() : authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return status != null && status.isEnable();
    }

    public boolean isExpired() {
        return isAccountExpired() || isCredentialsNonExpired();
    }
}