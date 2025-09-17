package club.hm.homemart.club.shared.common.uitls;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class JSON {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJSONString(Object object) {
        return toOptional(object).orElse(null);
    }

    public static Optional<String> toOptional(Object object) {
        try {
            return Optional.ofNullable(objectMapper.writeValueAsString(object));
        } catch (Exception e) {
            log.error("JSON.toJSONString error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static <T> Optional<T> toOptional(String object, Class<T> clazz) {
        try {
            return Optional.ofNullable(objectMapper.readValue(object, clazz));
        } catch (Exception e) {
            log.error("JSON.toJSONString error: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
