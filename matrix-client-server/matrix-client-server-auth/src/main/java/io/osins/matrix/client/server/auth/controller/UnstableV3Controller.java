package io.osins.matrix.client.server.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/_matrix/client/unstable")
public class UnstableV3Controller {
    @GetMapping("{serverName}/dehydrated_device")
    public String getDehydratedDevice()
    {
        return """
                {"errcode":"M_UNRECOGNIZED","error":"Unrecognized request"}""";
    }
}
