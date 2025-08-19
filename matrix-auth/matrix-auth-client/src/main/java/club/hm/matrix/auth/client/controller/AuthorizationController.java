package club.hm.matrix.auth.client.controller;

import club.hm.matrix.auth.client.vo.LoginRequest;
import club.hm.matrix.auth.client.service.RegistrationService;
import club.hm.matrix.auth.client.vo.RegisterRequest;
import club.hm.matrix.auth.client.vo.TokenResponse;
import club.hm.matrix.auth.api.service.LoginService;
import club.hm.matrix.shared.tracing.Tracing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/_matrix/client/v3")
public class AuthorizationController {
    private final Tracing tracing;
    private final RegistrationService registrationService;
    private final LoginService<LoginRequest, TokenResponse> loginService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TokenResponse> register(@RequestBody Mono<RegisterRequest> request) {
        return request.flatMap(req -> tracing.observe("matrix-auth-register", "user.mobile", req.username(), () -> registrationService.register(req)))
                .doOnError(throwable -> log.error("register error: {}", throwable.getMessage()));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TokenResponse> login(@RequestBody Mono<LoginRequest> request) {
        return request.flatMap(loginService::login);
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> logout(@RequestBody Mono<RegisterRequest> request) {
        return Mono.empty();
    }

    @PostMapping(value = "/version", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> version() {
        return Mono.empty();
    }
}
