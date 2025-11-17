package io.osins.matrix.client.server.auth.service;

import io.osins.matrix.client.server.auth.vo.RegisterAvailableResult;
import io.osins.matrix.client.server.auth.vo.TokenResponse;
import io.osins.matrix.client.server.auth.vo.RegisterRequest;
import io.osins.matrix.client.server.common.enums.RegisterAuthType;
import reactor.core.publisher.Mono;

public interface RegistrationService {
    Mono<TokenResponse> register(RegisterRequest request);

    Mono<RegisterAvailableResult> available(String username, RegisterAuthType type);
}