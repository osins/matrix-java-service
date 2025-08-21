package club.hm.homemart.club.shared.common.uitls;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;

@Slf4j
public final class S {
    public static <T, K> Map<K, List<T>> groupBy(List<T> records, Function<T, K> groupingBy) {
        return records.stream()
                .collect(Collectors.groupingBy(groupingBy));
    }

    public static <T, K> Map<K, T> toMap(List<T> records, Function<T, K> groupingBy) {
        return records.stream().collect(Collectors.toMap(groupingBy,
                item -> item,
                (existing, duplicate) -> existing));
    }

    public static <T, V, K> Map<K, List<T>> groupBy(List<T> records, Function<T, V> k, Function<V, K> toKey) {
        return records.stream()
                .collect(Collectors.groupingBy(v -> toKey.apply(k.apply(v))));
    }

    public static <T, K> List<T> groupBy(List<T> records, Function<T, K> toKey, BiConsumer<K, List<T>> call) {
        var maps = groupBy(records, toKey);
        maps.forEach(call);

        return records;
    }

    public static <T, V, K> Map<K, List<T>> groupBy(List<T> records, Predicate<? super T> filter,  Function<T, V> k, Function<V, K> toKey) {
        return records.stream()
                .filter(filter)
                .collect(Collectors.groupingBy(v -> toKey.apply(k.apply(v))));
    }

    public static <T, V, K> List<T> forEach(List<T> records, Predicate<? super T> filter,  Function<T, V> k, Function<V, K> toKey, BiConsumer<T, Map<K, T>> call) {
        var items =  records.stream()
                .filter(filter)
                .collect(Collectors.toList());

        var maps = toMap(items, v-> toKey.apply(k.apply(v)));
        items.forEach(v -> {
           call.accept(v, maps);
        });

        return items;
    }

    public static <T, K> List<T> forEach(List<T> records, Predicate<? super T> filter,  Function<T, K> toKey, BiConsumer<T, Map<K, T>> call) {
        var items =  records.stream()
                .filter(filter)
                .collect(Collectors.toList());

        var maps = toMap(items, toKey);
        items.forEach(v -> {
            call.accept(v, maps);
        });

        return items;
    }

    public static <T, K> List<T> forEachByIf(List<T> records, Predicate<? super T> filter,  Function<T, K> toKey, BiConsumer<T, T> call) {
        var items =  records.stream()
                .filter(filter)
                .collect(Collectors.toList());

        var maps = toMap(items, toKey);
        items.forEach(v -> {
            Optional.ofNullable(maps.get(toKey.apply(v))).ifPresent(v2->{
                call.accept(v, v2);
            });
        });

        return items;
    }

    public static <T, K> List<T> forEach(List<T> records, Function<T, K> toKey, BiConsumer<T, Map<K, T>> call) {
        var maps = toMap(records, toKey);
        records.forEach(v -> {
            call.accept(v, maps);
        });

        return records;
    }

    public static <T> void forEach(List<T> records, Consumer<T> function) {
        records.forEach(item -> E.run(() -> function.accept(item), ex -> log.error("循环处理数据异常: {}, {}", ex.getMessage(), item, ex)));
    }

    public static <K, T> void forEach(Map<K, T> records, Predicate<? super T> filter,  Consumer<T> function) {
        records.values().stream().filter(filter).forEach(item -> E.run(() -> function.accept(item), ex -> log.error("循环处理数据异常: {}, {}", ex.getMessage(), item, ex)));
    }

    public static <K, V> void forEach(Map<K, V> records, Consumer<V> function) {
        records.forEach((k, v) -> E.run(() -> function.accept(v), ex -> log.error("循环处理数据异常[{}]: {}, {}", k, ex.getMessage(), v, ex)));
    }

    public static <K, V> void forEach(Map<K, V> records, Consumer<V> function, String format) {
        records.forEach((k, v) -> E.run(() -> function.accept(v), ex -> {
            log.error("{}, 循环处理数据异常[{}]: {}, {}", format, k, ex.getMessage(), v, ex);
        }));
    }

    public static  <T> void forEach(List<T> records, Predicate<? super T> filter, Consumer<T> function) {
        records.stream().filter(filter)
                .forEach(v -> E.run(() -> function.accept(v)));
    }
}
