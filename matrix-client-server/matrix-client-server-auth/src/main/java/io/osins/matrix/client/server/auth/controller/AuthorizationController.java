package io.osins.matrix.client.server.auth.controller;

import io.osins.matrix.auth.security.jwt.JwtTokenProvider;
import io.osins.matrix.client.server.auth.vo.*;
import io.osins.matrix.client.server.auth.service.RegistrationService;
import io.osins.matrix.auth.api.service.LoginService;
import io.osins.matrix.client.server.common.domain.RegisterFlow;
import io.osins.matrix.client.server.common.enums.RegisterAuthType;
import io.osins.matrix.client.server.data.repos.RegisterSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/_matrix/client/v3")
public class AuthorizationController {
    private final static String REGISTER_AUTHORIZE_SESSION_KEY = "register_session";

    private final RegistrationService registrationService;
    private final LoginService<LoginRequest, TokenResponse> loginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RegisterSettingRepository registerSettingRepository;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> register(@RequestParam(value = "kind", required = false) String kind, @RequestBody Mono<RegisterRequest> request, WebSession session) {
        if (Optional.ofNullable(kind).filter("guest"::equals).isPresent()) {
            return registerSettingRepository.findAll().next()
                    .map(setting -> {
                        // 将 RegisterSetting 实体转换为 RegisterSettingResponse DTO
                        RegisterSettingResponse response = RegisterSettingResponse.builder()
                                .clientName(setting.clientName())
                                .clientNameEnUS(setting.clientNameEnUs())
                                .clientNameEnGB(setting.clientNameEnGb())
                                .clientNameFr(setting.clientNameFr())
                                .tosUri(setting.tosUri())
                                .tosUriFr(setting.tosUriFr())
                                .policyUri(setting.policyUri())
                                .policyUriFr(setting.policyUriFr())
                                .build();
                        return ResponseEntity.ok((Object) response);
                    });
        }

        return request.flatMap(req -> {
                    if (Optional.ofNullable(req.getAuth()).isEmpty()) {
                        return getRegisterAuthorize(session);
                    }

                    log.debug("session: {} = {}", req.getAuth().getSession(), session.getAttributes().get(REGISTER_AUTHORIZE_SESSION_KEY));

                    if(Optional.ofNullable(session.getAttributes().get(REGISTER_AUTHORIZE_SESSION_KEY))
                            .filter(uuid->uuid.equals(req.getAuth().getSession()))
                            .isEmpty()){
                        return getRegisterAuthorize(session);
                    }

                    log.debug("password: {}/{}", req.getUsername(), req.getPassword());
                    return registrationService.register(req)
                            .flatMap(response -> Mono.just(ResponseEntity.status(HttpStatus.OK).body(response)));
                });
    }

    private static Mono<ResponseEntity<Object>> getRegisterAuthorize(WebSession session) {
        // 创建 flows 列表
        var flow1 = RegisterFlow.Flow.builder()
                .stages(List.of("m.login.password"))
                .build();

        // 创建 params
        var params = Map.<String, Map<String, String>>of();

        var uuid = Optional.ofNullable(session.getAttributes().get(REGISTER_AUTHORIZE_SESSION_KEY))
                .map(Object::toString)
                .orElseGet(()->{
                    var id = UUID.randomUUID().toString();
                    session.getAttributes().put(REGISTER_AUTHORIZE_SESSION_KEY, id);
                    return id;
                });

        log.debug("session: {}", uuid);

        // 创建 RegisterFlow 对象
        var registerFlow = RegisterFlow.builder()
                .flows(List.of(flow1))
                .params(params)
                .session(uuid)
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(registerFlow));
    }

    @GetMapping(value = "/register/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<RegisterAvailableResult> registerAvailable(@RequestParam("username") String username) {
            return registrationService.available(username, RegisterAuthType.PASSWORD);
    }

    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public String loginGet() {
        return """
                {
                  "flows": [
                    {
                      "type": "m.login.password"
                    },
                    {
                      "get_login_token": true,
                      "type": "m.login.token"
                    }
                  ]
                }
                """;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TokenResponse> login(@RequestBody Mono<LoginRequest> request) {
        return request.flatMap(loginService::login);
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> logout() {
        return Mono.just("{}");
    }

    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TokenResponse> refresh(@RequestBody Mono<RefreshTokenRequest> request) {
        return request.map(req -> {
            if(!jwtTokenProvider.validateToken(req.getRefreshToken()))
                return TokenResponse.builder()
                        .errcode("M_UNKNOWN_TOKEN")
                        .softLogout(false)
                        .build();

            var response =jwtTokenProvider.generateToken(req.getRefreshToken());
            return TokenResponse.builder()
                    .accessToken(response.getAccessToken())
                    .refreshToken(response.getRefreshToken())
                    .expiresInMs(response.getExpiresInMs())
                    .build();
        });
    }

    @PostMapping(value = "/version", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> version() {
        return Mono.empty();
    }
}
