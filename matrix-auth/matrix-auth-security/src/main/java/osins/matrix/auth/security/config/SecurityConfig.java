package osins.matrix.auth.security.config;

import osins.matrix.auth.security.authentication.CustomReactiveAuthenticationManager;
import osins.matrix.auth.security.converter.CustomServerAuthenticationConverter;
import osins.matrix.auth.security.converter.JwtCustomizer;
import osins.matrix.auth.security.filter.CustomAuthenticationWebFilter;
import osins.matrix.auth.security.filter.CustomServerAuthenticationFailureHandler;
import osins.matrix.auth.security.filter.RequestIdWebFilter;
import osins.matrix.auth.security.handler.CustomAuthenticationSuccessHandler;
import osins.matrix.auth.security.handler.CustomServerAccessDeniedHandler;
import osins.matrix.auth.security.handler.BearerTokenAuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@ComponentScan(basePackages = "osins.matrix.auth.security")
public class SecurityConfig {

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            CustomReactiveAuthenticationManager authenticationManager,
                                                            CustomServerAuthenticationConverter converter,
                                                            CustomAuthenticationSuccessHandler successHandler,
                                                            CustomServerAuthenticationFailureHandler failureHandler,
                                                            CustomServerAccessDeniedHandler deniedHandler,
                                                            BearerTokenAuthenticationEntryPoint entryPoint,
                                                            JwtCustomizer customizer) {
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
                        .accessDeniedHandler(deniedHandler)
                        .authenticationEntryPoint(entryPoint)
                )
                // 配置授权规则
                .authorizeExchange(exchanges -> exchanges
                        // 公开端点
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                        .pathMatchers("/_matrix/client/v3/register", "/auth/register").permitAll()
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
                .oauth2ResourceServer(oauth2-> oauth2
                        .authenticationEntryPoint(entryPoint)
                        .jwt(customizer))
                .addFilterBefore(new RequestIdWebFilter(), SecurityWebFiltersOrder.FIRST)
                // 添加JWT认证过滤器
                .addFilterBefore(jwtAuthenticationWebFilter(authenticationManager, converter, successHandler, failureHandler), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public AuthenticationWebFilter jwtAuthenticationWebFilter(CustomReactiveAuthenticationManager manager,
                                                              CustomServerAuthenticationConverter converter,
                                                              CustomAuthenticationSuccessHandler successHandler,
                                                              CustomServerAuthenticationFailureHandler failureHandler) {
        var authenticationWebFilter = new CustomAuthenticationWebFilter(manager);
        authenticationWebFilter.setServerAuthenticationConverter(converter);
        authenticationWebFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationWebFilter.setAuthenticationFailureHandler(failureHandler);
        return authenticationWebFilter;
    }

//    @Bean
//    public AuthorizationWebFilter authorizationWebFilter(CustomReactiveAuthorizationManager manager) {
//        return new CustomAuthorizationWebFilter(manager);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}