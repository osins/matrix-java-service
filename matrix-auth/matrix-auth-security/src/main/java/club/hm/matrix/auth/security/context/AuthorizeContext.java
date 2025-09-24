package club.hm.matrix.auth.security.context;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthorizeContext {
    /**
     * 设置用户ID到上下文
     */
    public static <T> Mono<T> withUserId(Mono<T> mono, String userId) {
        return mono.contextWrite(Context.of(ContextKeys.USER_ID, userId));
    }

    /**
     * 从上下文获取用户ID
     */
    public static Mono<String> getCurrentUserId() {
        return Mono.deferContextual(contextView ->
                Mono.justOrEmpty(contextView.getOrEmpty(ContextKeys.USER_ID)));
    }

    /**
     * 设置多个上下文值
     */
    public static <T> Mono<T> withContext(Mono<T> mono, String userId, String traceId) {
        return mono.contextWrite(Context.of(
                ContextKeys.USER_ID, userId,
                ContextKeys.REQUEST_ID, traceId,
                ContextKeys.REQUEST_TIME, System.currentTimeMillis()
        ));
    }

    /**
     * 获取完整上下文信息
     */
    public static Mono<Map<String, Object>> getCurrentContext() {
        return Mono.deferContextual(contextView -> {
            Map<String, Object> context = new HashMap<>();
            contextView.getOrEmpty(ContextKeys.USER_ID)
                    .ifPresent(value -> context.put("userId", value));
            contextView.getOrEmpty(ContextKeys.REQUEST_ID)
                    .ifPresent(value -> context.put("traceId", value));
            contextView.getOrEmpty(ContextKeys.REQUEST_TIME)
                    .ifPresent(value -> context.put("requestTime", value));
            return Mono.just(context);
        });
    }
}
