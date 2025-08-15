package club.hm.matrix.auth.security.config;

import club.hm.matrix.auth.security.authentication.ReactiveAuthenticationJwtManager;
import club.hm.matrix.auth.security.authentication.ReactiveAccessDecisionManager;
import club.hm.matrix.auth.security.converter.AuthenticationJwtConverter;
import club.hm.matrix.auth.security.filter.CustomAuthenticationWebFilter;
import club.hm.matrix.auth.security.filter.CustomAuthorizationWebFilter;
import club.hm.matrix.auth.security.filter.CustomServerAuthenticationFailureHandler;
import club.hm.matrix.auth.security.handler.CustomAuthenticationSuccessHandler;
import club.hm.matrix.auth.security.handler.CustomServerAccessDeniedHandler;
import club.hm.matrix.auth.security.handler.ReactiveAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@ComponentScan(basePackages = "club.hm.matrix.auth.security")
public class SecurityConfig {

    private final ReactiveAuthenticationJwtManager jwtAuthenticationManager;
    private final ReactiveAccessDecisionManager accessDecisionManager;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomServerAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomServerAccessDeniedHandler accessDeniedHandler;
    private final ReactiveAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationJwtConverter bearerConverter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                // 禁用CSRF，因为我们使用JWT
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // 禁用表单登录
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // 禁用HTTP Basic认证
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                // 配置无状态会话管理
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                // 配置异常处理
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                // 配置授权规则
                .authorizeExchange(exchanges -> exchanges
                        // 公开端点
                        .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                        .pathMatchers("/auth/login", "/auth/register").permitAll()
                        .pathMatchers("/api/public/**").permitAll()
                        // API端点需要认证
                        .pathMatchers("/api/**").authenticated()
                        // 管理端点需要管理员权限
                        .pathMatchers("/admin/**").hasRole("ADMIN")
                        // WebSocket连接需要特殊处理
                        .pathMatchers("/ws/**").authenticated()
                        // 其他所有请求都需要认证
                        .anyExchange().authenticated()
                )
                // 添加JWT认证过滤器
                .addFilterBefore(jwtAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                // 添加自定义授权过滤器
                .addFilterAfter(authorizationWebFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .build();
    }

    @Bean
    public AuthenticationWebFilter jwtAuthenticationWebFilter() {
        var authenticationWebFilter = new CustomAuthenticationWebFilter(jwtAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(bearerConverter);
        authenticationWebFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationWebFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return authenticationWebFilter;
    }

    @Bean
    public AuthorizationWebFilter authorizationWebFilter() {
        return new CustomAuthorizationWebFilter(accessDecisionManager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}