package club.hm.matrix.client.server.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EventType {
    NONE("m.room.none", "未知类型"),
    ROOM_MEMBER("m.room.member", "用户加入/退出房间"),
    ROOM_TOPIC("m.room.topic", "房间主题"),
    ROOM_MESSAGE("m.room.message", "房间消息"),
    ROOM_CREATE("m.room.create", "房间创建"),
    ;

    @JsonValue
    private String value;
    private String desc;

    @JsonCreator
    public static EventType fromValue(String val) {
        for (EventType value : values()) {
            if (value.value.equals(val)) {
                return value;
            }
        }
        log.warn("EventType not found: {}", val);
        return NONE;
    }

    public boolean isNone() {
        return this == NONE;
    }
}
