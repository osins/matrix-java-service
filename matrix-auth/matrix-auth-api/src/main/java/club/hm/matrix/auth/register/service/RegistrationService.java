package club.hm.matrix.auth.register.service;

import club.hm.matrix.auth.register.vo.RegisterRequest;
import club.hm.matrix.auth.api.token.TokenResponse;
import reactor.core.publisher.Mono;

public interface RegistrationService {
    Mono<TokenResponse> register(RegisterRequest request);
}