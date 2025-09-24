package club.hm.matrix.client.server.auth.service;

import club.hm.matrix.client.server.auth.vo.RegisterAvailableResult;
import club.hm.matrix.client.server.auth.vo.TokenResponse;
import club.hm.matrix.client.server.auth.vo.RegisterRequest;
import club.hm.matrix.client.server.common.enums.RegisterAuthType;
import reactor.core.publisher.Mono;

public interface RegistrationService {
    Mono<TokenResponse> register(RegisterRequest request);

    Mono<RegisterAvailableResult> available(String username, RegisterAuthType type);
}