package io.osins.matrix.client.server.auth.controller;

import io.osins.matrix.client.server.common.domain.MatrixJoinRequest;
import io.osins.matrix.client.server.common.domain.MatrixJoinResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/_matrix/client/v3/join")
@RequiredArgsConstructor
public class ClientV3JoinController {
    @PostMapping("/{roomIdOrAlias}")
    public MatrixJoinResponse join(@PathVariable("roomIdOrAlias") String roomIdOrAlias, @RequestBody MatrixJoinRequest request) {
        return MatrixJoinResponse.builder()
                .roomId("!roomId:example.org")
                .build();
    }
}
