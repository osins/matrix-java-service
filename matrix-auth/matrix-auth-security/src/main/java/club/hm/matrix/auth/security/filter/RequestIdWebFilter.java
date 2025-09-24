package club.hm.matrix.auth.security.filter;

import club.hm.homemart.club.shared.common.uitls.Strings;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static club.hm.homemart.club.shared.common.log.MDCContext.REQUEST_ID_KEY;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdWebFilter implements WebFilter {

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        log.debug("RequestIdWebFilter, request id: {}", RequestIdWebFilter.getRequestId(exchange));

        return chain.filter(exchange);
    }

    public static String getRequestId(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getAttributes().get(REQUEST_ID_KEY))
                .map(Object::toString)
                .filter(Strings::isNoneNullOrEmpty)
                .orElseGet(()->{
                    var newRequestId = UUID.randomUUID().toString();
                    exchange.getAttributes().put(REQUEST_ID_KEY, newRequestId);
                    exchange.getResponse().getHeaders().add(REQUEST_ID_KEY, newRequestId);

                    return newRequestId;
                });
    }
}
