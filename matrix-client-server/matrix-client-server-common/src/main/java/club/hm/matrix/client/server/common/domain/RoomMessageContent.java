package club.hm.matrix.client.server.common.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomMessageContent {
    private String msgtype; // 消息类型，例如 m.text
    private String body;    // 消息正文
}
