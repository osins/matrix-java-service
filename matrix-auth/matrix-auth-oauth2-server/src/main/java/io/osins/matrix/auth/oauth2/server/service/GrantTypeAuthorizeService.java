package io.osins.matrix.auth.oauth2.server.service;

import io.osins.shared.common.uitls.Result;
import io.osins.matrix.auth.oauth2.server.enums.GrantType;
import io.osins.matrix.auth.oauth2.server.handler.authorize.IAuthorizeHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrantTypeAuthorizeService {
    private final List<IAuthorizeHandler<?>> handlers;
    private final Map<GrantType, IAuthorizeHandler<?>> maps = new EnumMap<>(GrantType.class);

    @PostConstruct
    public void init() {
        handlers.forEach(handler -> maps.put(handler.grantType(), handler));
    }

    public Mono<Result<?>> get(ServerHttpRequest request){
        var grantType = GrantType.of(request.getQueryParams().getFirst("grant_type"));
        if(grantType.isNone())
            return Mono.just(Result.error("无效的授权模式"));

        return maps.get(grantType)
                .handle(request)
                .map(Result::success);
    }
}
