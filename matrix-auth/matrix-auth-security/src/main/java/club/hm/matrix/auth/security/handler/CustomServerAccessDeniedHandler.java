package club.hm.matrix.auth.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomServerAccessDeniedHandler implements ServerAccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.warn("Access denied: {}", denied.getMessage());

        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        var errorResponse = new HashMap<String, Object>();
        errorResponse.put("error", "Access Denied");
        errorResponse.put("message", "您没有权限访问此资源");
        errorResponse.put("status", HttpStatus.FORBIDDEN.value());
        errorResponse.put("timestamp", System.currentTimeMillis());

        try {
            var responseBody = objectMapper.writeValueAsString(errorResponse);
            var buffer = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Error writing access denied response", e);
            return response.setComplete();
        }
    }
}
