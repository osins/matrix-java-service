package club.hm.matrix.auth.security.api.register.service.impl;

import club.hm.matrix.auth.api.enums.RegisterAuthType;
import club.hm.matrix.auth.register.handler.RegisterAuthHandler;
import club.hm.matrix.auth.register.service.RegistrationService;
import club.hm.matrix.auth.register.vo.RegisterRequest;
import club.hm.matrix.auth.api.token.TokenResponse;
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

    private Map<RegisterAuthType, RegisterAuthHandler> handlerMap;

    @PostConstruct
    public void init() {
        handlerMap = new EnumMap<>(RegisterAuthType.class);
        for (RegisterAuthHandler handler : authHandlers) {
            handlerMap.put(handler.getAuthType(), handler);
        }
    }

    @Override
    public Mono<TokenResponse> register(RegisterRequest req) {
        if (req == null || req.username() == null)
            return Mono.error(new IllegalArgumentException("username is null"));

        var authType = Optional.of(req).map(RegisterRequest::auth).map(RegisterRequest.Auth::type).orElse(RegisterAuthType.UNKNOWN);
        var handler = handlerMap.get(authType);
        if (handler == null) {
            return Mono.error(new IllegalArgumentException("Unsupported auth.type: " + authType));
        }

        return handler.handle(req);
    }
}
