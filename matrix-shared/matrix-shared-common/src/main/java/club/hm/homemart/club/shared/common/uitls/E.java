package club.hm.homemart.club.shared.common.uitls;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class E {
    public static <T> T get(Supplier<T> supplier) {
        return get(supplier, () -> null);
    }

    public static <T> T get(Supplier<T> supplier, Supplier<T> errorSupplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.debug("get error: {}, {}", e.getMessage(), e.getCause(), e);
            return null;
        }
    }

    public static <T> T get(Supplier<T> supplier, Consumer<Exception> consumer) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.debug("get error: {}, {}", e.getMessage(), e.getCause(), e);
            consumer.accept(e);

            return null;
        }
    }

    public static <T> T get(Supplier<T> supplier, Function<Exception, T> exceptionTFunction) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.debug("get error: {}, {}", e.getMessage(), e.getCause(), e);
            return exceptionTFunction.apply(e);
        }
    }

    public static void run(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.debug("get error: {}, {}", e.getMessage(), e.getCause(), e);
        }
    }

    public static void run(Runnable runnable, Consumer<Exception> consumer) {
        try {
            runnable.run();
        } catch (Exception e) {
            consumer.accept(e);
        }
    }
}
