package club.hm.homemart.club.shared.common.uitls;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public final class Longs {
    public static boolean isZero(Long value) {
        return value == null || value == 0;
    }

    public static boolean isZero(String value) {
        try {
            return value == null || Long.parseLong(value) == 0;
        } catch (Exception e) {
            log.error("Longs.isZero error: {}", e.getMessage(), e);
            return false;
        }
    }

    public static boolean isNoneZero(Long value) {
        return !isZero(value);
    }

    public static boolean isNoneZero(String s) {
        return !isZero(s);
    }

    public static Optional<Long> ofNullable(String s) {
        try {
            return Optional.ofNullable(s)
                    .filter(Strings::isNoneNullOrEmpty)
                    .filter(Longs::isNoneZero)
                    .map(Long::valueOf);
        } catch (Exception e) {
            log.error("Longs.ofNullable error: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }
}
