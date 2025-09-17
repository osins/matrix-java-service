package club.hm.matrix.client.server.common.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomMemberContent {
    private String membership;   // join/leave/invite
    private String displayname;  // 显示名称
}
