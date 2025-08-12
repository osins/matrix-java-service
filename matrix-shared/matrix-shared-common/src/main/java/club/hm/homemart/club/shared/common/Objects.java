package club.hm.homemart.club.shared.common;

import java.util.function.Consumer;

public final class Objects {
    public static <V> void set(V value, Consumer<V> set){
        if(value != null)
            set.accept(value);
    }
}
