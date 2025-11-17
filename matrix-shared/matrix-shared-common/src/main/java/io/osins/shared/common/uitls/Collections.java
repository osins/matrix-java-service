package io.osins.shared.common.uitls;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Collections {
    /**
     * 字符串转Set<String>集合
     */
    public static Set<String> toSet(String s) {
        return java.util.Collections.singleton(s);
    }


    public static Set<String> toSet(List<String> s) {
        return ImmutableSet.copyOf(s);
    }

    public static Set<String> toSet(Object val) {
        if (val == null) {
            return Sets.newHashSet();
        }

        if (val instanceof Iterable) {
            return Sets.newHashSet((Iterable<?>) val)
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.toSet());
        }else if (val instanceof Object[]) {
            return Sets.newHashSet((Object[]) val)
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.toSet());
        }else if (val instanceof String) {
            return toSet(val);
        }

        return toSet(val.toString());
    }
}
