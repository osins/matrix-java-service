package io.osins.matrix.client.server.auth.controller;

import io.osins.shared.common.uitls.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {
    @RequestMapping("")
    public Result<Void> index() {
        return Result.success();
    }
}
