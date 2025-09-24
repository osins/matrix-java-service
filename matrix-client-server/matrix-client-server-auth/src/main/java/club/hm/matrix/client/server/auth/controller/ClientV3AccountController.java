package club.hm.matrix.client.server.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_matrix/client/v3/account")
public class ClientV3AccountController {
    @GetMapping("/3pid")
    public String get3pid()
    {
        return "{}";
    }
}
