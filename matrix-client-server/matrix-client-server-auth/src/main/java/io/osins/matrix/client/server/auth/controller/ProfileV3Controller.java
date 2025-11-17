package io.osins.matrix.client.server.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/_matrix/client/v3/profile")
public class ProfileV3Controller {
    @GetMapping("/{userId}")
    public String getProfile()
    {
        return """
                {
                  "avatar_url": "mxc://matrix.org/SDGdghriugerRg",
                  "displayname": "Alice Margatroid"
                }
                """;
    }

    @GetMapping("/{userId}/avatar_url")
    public String updateAvatarUrl()
    {
        return """
                {
                  "avatar_url": "mxc://matrix.org/SDGdghriugerRg"
                }
                """;
    }
}
