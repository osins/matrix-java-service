package io.osins.matrix.client.server.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_matrix/client/v3/voip")
public class VoipV3Controller {
    @GetMapping("/turnServer")
    public String getTurnServer(){
        return """
                {
                  "password": "JlKfBy1QwLrO20385QyAtEyIv0=",
                  "ttl": 86400,
                  "uris": [
                    "turn:turn.example.com:3478?transport=udp",
                    "turn:10.20.30.40:3478?transport=tcp",
                    "turns:10.20.30.40:443?transport=tcp"
                  ],
                  "username": "1443779631:@user:example.com"
                }
                """;
    }
}
