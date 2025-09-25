package io.grpc.override;

import io.grpc.Context;
import reactor.core.publisher.Mono;
import java.util.Optional;

/**
 * 适用于Java 21 + Spring Boot WebFlux的gRPC Context存储实现
 *
 * 特点：
 * 1. 与Reactor Context深度集成
 * 2. 支持响应式流的Context传播
 * 3. 线程安全且内存高效
 * 4. 支持Virtual Threads
 */
public class ContextStorageOverride extends Context.Storage {

    private static final String GRPC_CONTEXT_KEY = "grpc.context";

    // 作为fallback的ThreadLocal，主要用于非响应式代码路径
    private final ThreadLocal<Context> fallbackStorage = new ThreadLocal<Context>() {
        @Override
        protected Context initialValue() {
            return Context.ROOT;
        }
    };

    @Override
    public Context doAttach(Context attach) {
        if (attach == null) {
            throw new IllegalArgumentException("Context to attach cannot be null");
        }

        // 尝试从Reactor Context获取当前值
        Context previous = getCurrentFromReactor().orElseGet(() -> {
            // 如果不在响应式流中，使用ThreadLocal作为fallback
            return fallbackStorage.get();
        });

        // 设置到ThreadLocal（用于fallback和同步代码）
        fallbackStorage.set(attach);

        return previous;
    }

    @Override
    public void detach(Context detach, Context restore) {
        if (restore == null || restore == Context.ROOT) {
            fallbackStorage.remove();
        } else {
            fallbackStorage.set(restore);
        }
    }

    @Override
    public Context current() {
        // 优先从Reactor Context获取
        return getCurrentFromReactor()
                .orElseGet(() -> {
                    // fallback到ThreadLocal
                    Context ctx = fallbackStorage.get();
                    return ctx != null ? ctx : Context.ROOT;
                });
    }

    /**
     * 从Reactor Context中获取gRPC Context
     */
    private Optional<Context> getCurrentFromReactor() {
        try {
            return Mono.deferContextual(contextView ->
                            Mono.just(contextView.getOrDefault(GRPC_CONTEXT_KEY, Context.ROOT))
                    ).cast(Context.class)
                    .blockOptional();
        } catch (Exception e) {
            // 不在响应式流中或获取失败，返回空
            return Optional.empty();
        }
    }

    /**
     * 将gRPC Context放入Reactor Context的工具方法
     * 在WebFlux Controller或Service中使用
     */
    public static <T> Mono<T> withContext(Mono<T> mono, Context grpcContext) {
        return mono.contextWrite(ctx -> ctx.put(GRPC_CONTEXT_KEY, grpcContext));
    }

    /**
     * 从Reactor Context提取gRPC Context的工具方法
     */
    public static Mono<Context> getGrpcContext() {
        return Mono.deferContextual(ctx ->
                Mono.just(ctx.getOrDefault(GRPC_CONTEXT_KEY, Context.ROOT))
        );
    }
}
