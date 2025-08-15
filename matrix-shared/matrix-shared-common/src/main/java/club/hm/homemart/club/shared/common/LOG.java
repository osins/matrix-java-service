package club.hm.homemart.club.shared.common;

import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class LOG {
    public static <R> R supplier(Supplier<R> supplier, Logger log) {
        return E.get(supplier, ex -> {
            log.error(ex.getMessage(), ex);
        });
    }

    public static <R> R supplier(Supplier<R> supplier, Logger log, String format, Object... arguments) {
        return E.get(supplier, ex -> {
            log.error(format, arguments);
        });
    }

    public static void run(Runnable runnable, Logger log, String format, Object... arguments) {
        E.run(runnable, ex -> {
            log.error(format, arguments);
        });
    }

    public static <R> R consumer(Supplier<R> supplier, Consumer<R> consumer, Logger log, String format, Object... arguments) {
        var returnValue = E.get(supplier, ex -> {
            log.error(format, arguments);
        });

        consumer.accept(returnValue);

        return returnValue;
    }

    public static <R> R apply(Supplier<R> supplier, Function<R, R> apply, Logger log, String format, Object... arguments) {
        return apply.apply(E.get(supplier, ex -> {
            log.error(format, arguments);
        }));
    }
}
