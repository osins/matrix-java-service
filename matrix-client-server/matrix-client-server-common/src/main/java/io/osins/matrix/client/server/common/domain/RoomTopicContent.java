package io.osins.matrix.client.server.common.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomTopicContent {
    private String topic; // 房间话题
}