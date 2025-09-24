package club.hm.matrix.client.server.auth.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/_matrix/client/v3/user")
public class ClientV3UserController {

    @PostMapping("/{userId}/filter")
    public Mono<String> getUserFilter(@PathVariable("userId") String userId)
    {
        return Mono.just("""
                {
                  "event_fields": ["type", "content", "sender"],
                  "event_format": "client",
                  "presence": {
                    "not_senders": ["@alice:example.com"],
                    "types": ["m.presence"]
                  },
                  "room": {
                    "ephemeral": {
                      "not_rooms": ["!726s6s6q:example.com"],
                      "not_senders": ["@spam:example.com"],
                      "types": ["m.receipt", "m.typing"]
                    },
                    "state": {
                      "not_rooms": ["!726s6s6q:example.com"],
                      "types": ["m.room.*"]
                    },
                    "timeline": {
                      "limit": 10,
                      "not_rooms": ["!726s6s6q:example.com"],
                      "not_senders": ["@spam:example.com"],
                      "types": ["m.room.message"]
                    }
                  }
                }
                """);
    }

    @PutMapping("/{userId}/account_data/{type}")
    public Mono<String> setUserAccountData(@PathVariable("userId") String userId, @PathVariable("type") String type)
    {
        return Mono.just("{}");
    }

    @GetMapping("/{userId}/account_data/{type}")
    public Mono<String> getAccountData(@PathVariable("userId") String userId, @PathVariable("type") String type)
    {
        return Mono.just("{}");
    }
}
