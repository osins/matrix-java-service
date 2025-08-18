package club.hm.matrix.auth.security.api.login.service;

import club.hm.matrix.auth.api.enums.LoginAuthType;
import club.hm.matrix.auth.api.enums.RegisterAuthType;
import club.hm.matrix.auth.api.token.TokenResponse;
import club.hm.matrix.auth.login.handler.LoginAuthHandler;
import club.hm.matrix.auth.login.service.LoginService;
import club.hm.matrix.auth.login.vo.LoginRequest;
import club.hm.matrix.auth.register.vo.RegisterRequest;
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
public class LoginServiceImpl implements LoginService {

    private final List<LoginAuthHandler> authHandlers; // Spring 自动注入所有 Handler

    private Map<LoginAuthType, LoginAuthHandler> handlerMap;

    @PostConstruct
    public void init() {
        handlerMap = new EnumMap<>(LoginAuthType.class);
        for (LoginAuthHandler handler : authHandlers) {
            handlerMap.put(handler.getAuthType(), handler);
        }
    }

    @Override
    public Mono<TokenResponse> login(LoginRequest req) {
        if (req == null || req.getIdentifier().getUser() == null)
            return Mono.error(new IllegalArgumentException("username is null"));

        var authType = Optional.of(req).map(LoginRequest::getType).orElse(LoginAuthType.UNKNOWN);
        var handler = handlerMap.get(authType);
        if (handler == null) {
            return Mono.error(new IllegalArgumentException("Unsupported auth.type: " + authType));
        }

        return handler.handle(req);
    }
}
