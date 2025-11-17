package io.osins.matrix.client.server.auth.service.impl;

import io.osins.matrix.client.server.auth.vo.RegisterAvailableResult;
import io.osins.matrix.client.server.common.enums.RegisterAuthType;
import io.osins.matrix.client.server.auth.vo.TokenResponse;
import io.osins.matrix.client.server.auth.handler.RegisterAuthHandler;
import io.osins.matrix.client.server.auth.service.RegistrationService;
import io.osins.matrix.client.server.auth.vo.RegisterRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final List<RegisterAuthHandler> authHandlers; // Spring 自动注入所有 Handler

    private Map<RegisterAuthType, RegisterAuthHandler> handlers;

    @PostConstruct
    public void init() {
        handlers = new EnumMap<>(RegisterAuthType.class);
        for (RegisterAuthHandler handler : authHandlers) {
            handlers.put(handler.getAuthType(), handler);
        }
    }

    @Override
    public Mono<TokenResponse> register(RegisterRequest req) {
        if (req == null || req.getUsername() == null)
            return Mono.error(new IllegalArgumentException("username is null"));

        var authType = Optional.of(req).map(RegisterRequest::getAuth).map(RegisterRequest.Auth::getType).orElse(RegisterAuthType.PASSWORD);
        var handler = handlers.get(authType);
        if (handler == null) {
            return Mono.error(new IllegalArgumentException("Unsupported auth.type: " + authType));
        }

        return handler.handle(req);
    }

    @Override
    public Mono<RegisterAvailableResult> available(String username, RegisterAuthType type) {
        return handlers.get(type).available(username);
    }
}
