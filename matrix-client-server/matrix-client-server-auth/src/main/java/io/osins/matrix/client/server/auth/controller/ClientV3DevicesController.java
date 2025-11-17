package io.osins.matrix.client.server.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_matrix/client/v3/devices")
public class ClientV3DevicesController {
    @GetMapping("")
    public String getDevices(){
        return "[]";
    }
}
