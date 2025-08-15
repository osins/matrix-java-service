package club.hm.homemart.club.shared.common;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class Objects {
    public static boolean anyNull(Object... args){
        return Stream.of(args).anyMatch(java.util.Objects::isNull);
    }
    public static <V> void set(V value, Consumer<V> set){
        if(value != null)
            set.accept(value);
    }
}
