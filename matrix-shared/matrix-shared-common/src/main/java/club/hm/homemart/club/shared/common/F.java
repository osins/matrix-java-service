package club.hm.homemart.club.shared.common;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class F {
    public static <T> T consumer(T value, Consumer<T> call) {
        call.accept(value);

        return value;
    }


    public static <T> T supplier(Supplier<T> fun, Runnable call) {
        var result = fun.get();
        call.run();

        return result;
    }


    public static <T> T supplier(Supplier<T> fun, Consumer<T> call) {
        return consumer(fun.get(), call);
    }

    public static <T, R> R map(Supplier<T> fun, Function<T, R> call) {
        return call.apply(fun.get());
    }

    public static <T, R> R apply(T value, Function<T, R> call) {
        return call.apply(value);
    }

    public static <R> R orElse(boolean success, Supplier<R> successCall, Supplier<R> call){
        return success ? successCall.get() : call.get();
    }
}
