package club.hm.matrix.client.server.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/_matrix/media/v3/thumbnail")
public class ThumbnailV3Controller {
    @GetMapping("/{serverName}/{mediaId}")
    public String getThumbnail()
    {
        return """
                {
                  "errcode": "M_UNKNOWN",
                  "error": "Cannot generate thumbnails for the requested content"
                }
                """;
    }
}
