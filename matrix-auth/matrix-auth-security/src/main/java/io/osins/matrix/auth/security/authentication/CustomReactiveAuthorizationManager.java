package io.osins.matrix.auth.security.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 矩阵权限管理
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomReactiveAuthorizationManager implements ReactiveAuthorizationManager<ServerWebExchange> {
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, ServerWebExchange context) {
        var path = context.getRequest().getURI().getPath();
        var method = context.getRequest().getMethod().name();

        return authentication
                .filter(Authentication::isAuthenticated)
                .flatMap(auth -> checkPermission(auth, path, method))
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private Mono<Boolean> checkPermission(Authentication authentication, String path, String method) {
        // 实现具体的权限检查逻辑
        // 可以根据用户角色、资源路径、HTTP方法等进行权限验证

        if (path.startsWith("/admin/")) {
            return Mono.just(hasRole(authentication, "ADMIN"));
        }

        if (path.startsWith("/api/user/")) {
            return Mono.just(hasRole(authentication, "USER") || hasRole(authentication, "ADMIN"));
        }

        // 默认允许已认证用户访问
        return Mono.just(true);
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_" + role));
    }
}