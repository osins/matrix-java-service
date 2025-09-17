package club.hm.matrix.client.server.common.domain;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Matrix 协议标准事件实体
 */
@Data // 自动生成 getter/setter、equals、hashCode、toString
@SuperBuilder
@Accessors(chain = true, fluent = false)
@AllArgsConstructor
@NoArgsConstructor
public class MatrixEvent<T> {

    /** 事件类型，如 m.room.message */
    private String type;

    /** 房间 ID，如 !abcdef:example.org */
    @JsonProperty("room_id")
    private String roomId;

    /** 事件 ID，如 $143273582443PhrSn:example.org */
    @JsonProperty("event_id")
    private String eventId;

    /** 发送者，如 @alice:example.org */
    private String sender;

    /** 服务器时间戳（毫秒） */
    @JsonProperty("origin_server_ts")
    private long originServerTs;

    /** 事件内容 */
    private T content;

    /** 非签名字段 */
    private Map<String, Object> unsigned = new HashMap<>();

    /** 其他未知字段 */
    private Map<String, Object> extra = new HashMap<>();

    @JsonAnySetter
    public void addExtra(String key, Object value) {
        if(this.extra==null)
            this.extra = new HashMap<>();

        this.extra.put(key, value);
    }
}
