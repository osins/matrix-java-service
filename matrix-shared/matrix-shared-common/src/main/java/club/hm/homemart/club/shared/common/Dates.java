package club.hm.homemart.club.shared.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Consumer;

public final class Dates {
    private static final ZoneId ZONE_SHANGHAI = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static void set(LocalDateTime value, Consumer<String> set) {
        Optional.ofNullable(value).map(dt -> dt.atZone(ZONE_SHANGHAI).format(FORMATTER)).ifPresent(set);
    }

    public static void set(String value, Consumer<LocalDateTime> set) {
        Optional.ofNullable(value)
                .filter(v -> !v.trim().isEmpty())
                .map(v -> ZonedDateTime.parse(v, FORMATTER).withZoneSameInstant(ZONE_SHANGHAI).toLocalDateTime()).ifPresent(set);
    }

    // 可选：添加一个安全的解析方法供直接使用
    public static LocalDateTime parseToLocalDateTime(String value) {
        return Optional.ofNullable(value)
                .filter(v -> !v.trim().isEmpty())
                .map(v -> ZonedDateTime.parse(v, FORMATTER)
                        .withZoneSameInstant(ZONE_SHANGHAI)
                        .toLocalDateTime())
                .orElse(null);
    }

    // 可选：添加一个安全的格式化方法
    public static String formatFromLocalDateTime(LocalDateTime value) {
        return Optional.ofNullable(value)
                .map(dt -> dt.atZone(ZONE_SHANGHAI).format(FORMATTER))
                .orElse(null);
    }
}
