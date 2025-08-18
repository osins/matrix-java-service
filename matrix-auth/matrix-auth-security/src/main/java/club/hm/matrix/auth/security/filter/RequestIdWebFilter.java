package club.hm.matrix.auth.security.filter;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.UUID;

import static club.hm.homemart.club.shared.common.log.MDCContext.REQUEST_ID_KEY;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdWebFilter implements WebFilter {

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange,@NonNull WebFilterChain chain) {
        // 获取客户端传来的 requestId，或者生成一个新的
        var requestId = exchange.getRequest().getHeaders().getFirst(REQUEST_ID_KEY);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        var lastRequestId = requestId;

        // 存储到 exchange attributes 中
        exchange.getAttributes().put(REQUEST_ID_KEY, requestId);

        // 添加到响应头
        exchange.getResponse().getHeaders().add(REQUEST_ID_KEY, requestId);

        return chain.filter(exchange);
    }

    public static String getRequestId(ServerWebExchange exchange) {
        return (String) exchange.getAttributes().get(REQUEST_ID_KEY);
    }
}
