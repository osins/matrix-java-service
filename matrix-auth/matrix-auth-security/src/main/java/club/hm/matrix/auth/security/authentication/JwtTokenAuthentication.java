package club.hm.matrix.auth.security.authentication;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * JWT 专用 Authentication，实现 Spring Security Authentication 接口
 */
@Data
public class JwtTokenAuthentication implements Authentication {

    private final String token; // 原始 JWT
    private boolean authenticated = false; // 是否已经认证
    private Object principal; // 用户身份信息
    private Object details;   // 可选附加信息
    private Collection<? extends GrantedAuthority> authorities; // 权限列表

    // 构造未认证的 token
    public JwtTokenAuthentication(String token) {
        this.token = token;
    }

    // 构造已认证的 token
    public JwtTokenAuthentication(Object principal,
                                  String token,
                                  Collection<? extends GrantedAuthority> authorities) {
        this.principal = principal;
        this.token = token;
        this.authorities = authorities;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        if (principal != null) {
            return principal.toString();
        }
        return null;
    }
}