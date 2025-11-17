package io.osins.matrix.client.server.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_matrix/client/v3/capabilities")
public class CapabilitiesV3Controller {
    @GetMapping()
    public String getCapabilities()
    {
        return """
                {
                  "capabilities": {
                    "com.example.custom.ratelimit": {
                      "max_requests_per_hour": 600
                    },
                    "m.change_password": {
                      "enabled": false
                    },
                    "m.room_versions": {
                      "available": {
                        "1": "stable",
                        "2": "stable",
                        "3": "unstable",
                        "test-version": "unstable"
                      },
                      "default": "1"
                    }
                  }
                }
                """;
    }
}
