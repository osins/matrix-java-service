package io.osins.shared.common.uitls;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
public class Dates {
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final ZoneId ZONE_SHANGHAI = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static Date parse(String dateStr, String pattern) {
        try {
            if (Strings.isNullOrEmpty(dateStr)) {
                return null;
            }

            if (pattern == null) {
                pattern = DEFAULT_PATTERN;
            }

            var formatter = DateTimeFormatter.ofPattern(pattern);
            // 使用上海时区
            var localDateTime = LocalDateTime.parse(dateStr, formatter);
            return Date.from(localDateTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
        } catch (DateTimeParseException e) {
            log.error("日期转换错误：{}", e.getMessage());
            // 日志可选
            return null;
        }
    }

    public static Date parse(String dateStr) {
        return parse(dateStr, DEFAULT_PATTERN);
    }

    public static long getDaysBetween(Date start, Date end) {
        var startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        var endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

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
