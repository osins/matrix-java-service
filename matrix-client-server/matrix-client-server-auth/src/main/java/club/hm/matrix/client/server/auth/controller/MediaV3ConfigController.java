package club.hm.matrix.client.server.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_matrix/media/v3")
public class MediaV3ConfigController {
    @GetMapping("/config")
    public String getConfig()
    {
        return "{}";
    }
}
