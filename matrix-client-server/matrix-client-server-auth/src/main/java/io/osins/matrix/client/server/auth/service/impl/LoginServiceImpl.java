package io.osins.matrix.client.server.auth.service.impl;

import io.osins.matrix.client.server.common.enums.LoginType;
import io.osins.matrix.client.server.auth.handler.LoginHandler;
import io.osins.matrix.client.server.auth.vo.TokenResponse;
import io.osins.matrix.client.server.auth.vo.LoginRequest;
import io.osins.matrix.auth.api.service.LoginService;
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
public class LoginServiceImpl implements LoginService<LoginRequest, TokenResponse> {

    private final List<LoginHandler> handlers; // Spring 自动注入所有 Handler

    private Map<LoginType, LoginHandler> maps;

    @PostConstruct
    public void init() {
        maps = new EnumMap<>(LoginType.class);
        for (LoginHandler handler : handlers) {
            maps.put(handler.getType(), handler);
        }
    }

    @Override
    public Mono<TokenResponse> login(LoginRequest req) {
        if (req == null || req.getIdentifier().getUser() == null)
            return Mono.error(new IllegalArgumentException("username is null"));

        var authType = Optional.of(req).map(LoginRequest::getType).orElse(LoginType.UNKNOWN);
        var handler = maps.get(authType);
        if (handler == null) {
            return Mono.error(new IllegalArgumentException("Unsupported auth.type: " + authType));
        }

        return handler.handle(req);
    }
}
