package club.hm.homemart.club.shared.common;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class V {
    public static <T, X extends Throwable> void ifNull(Supplier<T> supplier, Supplier<? extends X> exceptionSupplier) throws X {
        Optional.ofNullable(supplier.get()).orElseThrow(exceptionSupplier);
    }

    public static <T> void ifNull(Supplier<T> supplier, Runnable runnable){
        if(supplier.get() == null)
            runnable.run();
    }

    public static <T> void ifNull(Supplier<T> supplier, Consumer<T> consumer, T defaultValue){
        if(supplier.get() == null)
            consumer.accept(defaultValue);
    }

    public static void ifZero(Supplier<Long> supplier, Consumer<Long> consumer, Long defaultValue){
        if(supplier.get() == null)
            consumer.accept(defaultValue);
    }
}
