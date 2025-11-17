package io.osins.matrix.client.server.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/e")
public class OtherController {
    @PostMapping("/")
    public Mono<String> step(@RequestParam("ip") String ip,
                             @RequestParam("_") String timestamp,
                             @RequestParam("ver") String version,
                             @RequestParam("compression") String compression) {
        return Mono.just("other");
    }
}
