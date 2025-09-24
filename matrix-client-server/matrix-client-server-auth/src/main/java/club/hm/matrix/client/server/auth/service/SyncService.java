package club.hm.matrix.client.server.auth.service;

import club.hm.matrix.auth.security.domain.CustomPrincipal;
import club.hm.matrix.client.server.common.domain.MatrixEvent;
import club.hm.matrix.client.server.common.manager.SyncTokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncService {

    private final SyncTokenManager tokenManager;

    /**
     * 模拟事件存储
     */
    private final List<MatrixEvent<Map<String, String>>> events = List.of(
            new MatrixEvent<Map<String, String>>()
                    .setType("m.room.message")
                    .setSender("@alice:example.org")
                    .setRoomId("!abcdef:example.org")
                    .setContent(Map.of("body", "Hello World!")),

            new MatrixEvent<Map<String, String>>()
                    .setType("m.presence")
                    .setSender("@bob:example.org")
                    .setContent(Map.of("presence", "online"))
    );

    public Mono<Map<String, Object>> getSync(CustomPrincipal principal,
                                             String since,
                                             long timeout,
                                             boolean fullState,
                                             String filter) {

        return Mono.delay(Duration.ofMillis(Math.min(timeout, 30000)))
                .flatMap(ignore -> {
                    Map<String, Object> response = new HashMap<>();

                    // account_data
                    Map<String, Object> accountData = new HashMap<>();
                    accountData.put("events", List.of(
                            Map.of(
                                    "type", "org.example.custom.config",
                                    "content", Map.of("custom_config_key", "custom_config_value")
                            )
                    ));
                    response.put("account_data", accountData);

                    // next_batch
                    response.put("next_batch", "s72595_4483_1934");

                    // presence
                    Map<String, Object> presence = new HashMap<>();
                    presence.put("events", List.of(
                            Map.of(
                                    "type", "m.presence",
                                    "sender", "@example:localhost",
                                    "content", Map.of(
                                            "avatar_url", "mxc://localhost/wefuiwegh8742w",
                                            "currently_active", false,
                                            "last_active_ago", 2478593,
                                            "presence", "online",
                                            "status_msg", "Making cupcakes"
                                    )
                            )
                    ));
                    response.put("presence", presence);

                    // rooms
                    Map<String, Object> rooms = new HashMap<>();

                    // invite
                    Map<String, Object> inviteRoom = new HashMap<>();
                    inviteRoom.put("invite_state", Map.of(
                            "events", List.of(
                                    Map.of(
                                            "type", "m.room.name",
                                            "sender", "@alice:example.com",
                                            "state_key", "",
                                            "content", Map.of("name", "My Room Name")
                                    ),
                                    Map.of(
                                            "type", "m.room.member",
                                            "sender", "@alice:example.com",
                                            "state_key", "@bob:example.com",
                                            "content", Map.of("membership", "invite")
                                    )
                            )
                    ));
                    rooms.put("invite", Map.of("!696r7674:example.com", inviteRoom));

                    // join
                    Map<String, Object> joinRoom = new HashMap<>();
                    joinRoom.put("account_data", Map.of(
                            "events", List.of(
                                    Map.of(
                                            "type", "m.tag",
                                            "content", Map.of("tags", Map.of("u.work", Map.of("order", 0.9)))
                                    ),
                                    Map.of(
                                            "type", "org.example.custom.room.config",
                                            "content", Map.of("custom_config_key", "custom_config_value")
                                    )
                            )
                    ));
                    joinRoom.put("timeline", Map.of(
                            "events", List.of(
                                    Map.of(
                                            "type", "m.room.member",
                                            "event_id", "$143273582443PhrSn:example.org",
                                            "origin_server_ts", 1432735824653L,
                                            "sender", "@alice:example.org",
                                            "state_key", "@alice:example.org",
                                            "content", Map.of(
                                                    "avatar_url", "mxc://example.org/SEsfnsuifSDFSSEF",
                                                    "displayname", "Alice Margatroid",
                                                    "membership", "join",
                                                    "reason", "Looking for support"
                                            ),
                                            "unsigned", Map.of("age", 1234, "membership", "join")
                                    ),
                                    Map.of(
                                            "type", "m.room.message",
                                            "event_id", "$143273582443PhrSn:example.org",
                                            "origin_server_ts", 1432735824653L,
                                            "sender", "@example:example.org",
                                            "content", Map.of(
                                                    "body", "This is an example text message",
                                                    "format", "org.matrix.custom.html",
                                                    "formatted_body", "<b>This is an example text message</b>",
                                                    "msgtype", "m.text"
                                            ),
                                            "unsigned", Map.of("age", 1234, "membership", "join")
                                    )
                            ),
                            "limited", true,
                            "prev_batch", "t34-23535_0_0"
                    ));
                    rooms.put("join", Map.of("!726s6s6q:example.com", joinRoom));

                    // knock
                    Map<String, Object> knockRoom = new HashMap<>();
                    knockRoom.put("knock_state", Map.of(
                            "events", List.of(
                                    Map.of(
                                            "type", "m.room.name",
                                            "sender", "@alice:example.com",
                                            "state_key", "",
                                            "content", Map.of("name", "My Room Name")
                                    ),
                                    Map.of(
                                            "type", "m.room.member",
                                            "sender", "@bob:example.com",
                                            "state_key", "@bob:example.com",
                                            "content", Map.of("membership", "knock")
                                    )
                            )
                    ));
                    rooms.put("knock", Map.of("!223asd456:example.com", knockRoom));

                    rooms.put("leave", Map.of());
                    response.put("rooms", rooms);

                    return Mono.just(response);
                });
    }
}