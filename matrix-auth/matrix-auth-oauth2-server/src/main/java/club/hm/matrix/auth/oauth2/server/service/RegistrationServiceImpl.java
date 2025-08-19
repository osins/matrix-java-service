package club.hm.matrix.auth.oauth2.server.service;

import club.hm.matrix.auth.oauth2.server.enums.GrantType;
import club.hm.matrix.auth.oauth2.server.handler.RegisterHandler;
import club.hm.matrix.auth.oauth2.server.vo.RegisterRequest;
import club.hm.matrix.auth.oauth2.server.vo.TokenResponse;
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

    private final List<RegisterHandler> authHandlers; // Spring 自动注入所有 Handler

    private Map<GrantType, RegisterHandler> handlerMap;

    @PostConstruct
    public void init() {
        handlerMap = new EnumMap<>(GrantType.class);
        for (RegisterHandler handler : authHandlers) {
            handlerMap.put(handler.getType(), handler);
        }
    }

    @Override
    public Mono<TokenResponse> register(RegisterRequest req) {
        if (req == null || req.username() == null)
            return Mono.error(new IllegalArgumentException("username is null"));

        var authType = Optional.of(req).map(RegisterRequest::auth).map(RegisterRequest.Auth::type).orElse(GrantType.UNKNOWN);
        var handler = handlerMap.get(authType);
        if (handler == null) {
            return Mono.error(new IllegalArgumentException("Unsupported auth.type: " + authType));
        }

        return handler.handle(req);
    }
}
