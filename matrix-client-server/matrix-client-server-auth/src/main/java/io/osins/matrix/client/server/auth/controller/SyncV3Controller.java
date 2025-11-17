package io.osins.matrix.client.server.auth.controller;

import io.osins.matrix.auth.security.domain.CustomPrincipal;
import io.osins.matrix.auth.security.filter.RequestIdWebFilter;
import io.osins.matrix.client.server.auth.service.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/_matrix/client/v3/sync")
@RequiredArgsConstructor
public class SyncV3Controller {
    private final SyncService syncService;

    @GetMapping()
    public Mono<Map<String, Object>> getSync(
            ServerWebExchange exchange,
            @AuthenticationPrincipal Mono<CustomPrincipal> principalMono,
            @RequestParam(value = "since", required = false) String since,
            @RequestParam(value = "timeout", defaultValue = "30000") long timeout,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "full_state", defaultValue = "false") boolean fullState,
            @RequestParam(value = "set_presence", defaultValue = "online") String setPresence)
    {
        return principalMono.flatMap(principal->{
            log.debug("getSync, request id: {}, user: {}", RequestIdWebFilter.getRequestId(exchange), principal.getUsername());
            log.debug("authorities: {}", principal.getAuthorities());

            return syncService.getSync(principal, since, timeout, fullState, filter);
        });
    }
}
