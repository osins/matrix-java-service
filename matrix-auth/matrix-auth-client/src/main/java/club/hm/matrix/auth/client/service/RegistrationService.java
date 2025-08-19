package club.hm.matrix.auth.client.service;

import club.hm.matrix.auth.client.vo.TokenResponse;
import club.hm.matrix.auth.client.vo.RegisterRequest;
import reactor.core.publisher.Mono;

public interface RegistrationService {
    Mono<TokenResponse> register(RegisterRequest request);
}