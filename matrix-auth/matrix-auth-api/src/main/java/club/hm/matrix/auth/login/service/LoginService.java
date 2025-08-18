package club.hm.matrix.auth.login.service;

import club.hm.matrix.auth.login.vo.LoginRequest;
import club.hm.matrix.auth.api.token.TokenResponse;
import reactor.core.publisher.Mono;

public interface LoginService {
    Mono<TokenResponse> login(LoginRequest request);
}
