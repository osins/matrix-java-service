package club.hm.matrix.client.server.api.controller;

import club.hm.matrix.client.server.common.domain.MatrixEvent;
import club.hm.matrix.client.server.common.domain.RoomMessageContent;
import club.hm.matrix.client.server.api.service.EventService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final EventService<RoomMessageContent> eventService;

    public RoomController(EventService<RoomMessageContent> eventService) {
        this.eventService = eventService;
    }

    // 发送消息
    @PostMapping("/{roomId}/send")
    public void sendMessage(@PathVariable String roomId,
                            @RequestParam String sender,
                            @RequestParam String content) {
        eventService.pushEvent(MatrixEvent.<RoomMessageContent>builder()
                .type("m.room.message")
                .roomId(roomId)
                .sender(sender)
                .content(RoomMessageContent.builder()
                        .body(content)
                        .build())
                .build());
    }

    // 订阅消息
    @GetMapping(value = "/{roomId}/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MatrixEvent<RoomMessageContent>> subscribe(@PathVariable String roomId) {
        return eventService.subscribe(roomId);
    }
}