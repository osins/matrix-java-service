package io.grpc.override;

import reactor.util.context.ContextView;

public class ReactorContextHolder {

    private static final ThreadLocal<ContextView> REACTOR_CTX = new ThreadLocal<>();

    public static void setReactorContext(ContextView ctx) {
        REACTOR_CTX.set(ctx);
    }

    public static ContextView currentReactorContext() {
        return REACTOR_CTX.get();
    }

    public static void clear() {
        REACTOR_CTX.remove();
    }
}
