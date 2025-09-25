package osins.matrix.client.server.auth.service;

import osins.matrix.client.server.auth.vo.RegisterAvailableResult;
import osins.matrix.client.server.auth.vo.TokenResponse;
import osins.matrix.client.server.auth.vo.RegisterRequest;
import osins.matrix.client.server.common.enums.RegisterAuthType;
import reactor.core.publisher.Mono;

public interface RegistrationService {
    Mono<TokenResponse> register(RegisterRequest request);

    Mono<RegisterAvailableResult> available(String username, RegisterAuthType type);
}