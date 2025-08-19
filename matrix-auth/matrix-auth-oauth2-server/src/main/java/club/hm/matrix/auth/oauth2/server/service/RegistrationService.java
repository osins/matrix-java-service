package club.hm.matrix.auth.oauth2.server.service;

import club.hm.matrix.auth.oauth2.server.vo.RegisterRequest;
import club.hm.matrix.auth.oauth2.server.vo.TokenResponse;
import reactor.core.publisher.Mono;

public interface RegistrationService {
    Mono<TokenResponse> register(RegisterRequest request);
}