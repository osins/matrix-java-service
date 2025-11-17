package io.osins.matrix.client.server.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/_matrix/client/v3/thirdparty")
public class ThirdpartyProtocolsController {
    @GetMapping("/protocols")
    public String getProtocols() {
        return """
                {
                  "gitter": {
                    "field_types": {
                      "room": {
                        "placeholder": "matrix-org/matrix-doc",
                        "regexp": "[^\\\\s]+\\\\/[^\\\\s]+"
                      },
                      "username": {
                        "placeholder": "@username",
                        "regexp": "@[^\\\\s]+"
                      }
                    },
                    "instances": [
                      {
                        "desc": "Gitter",
                        "fields": {},
                        "icon": "mxc://example.org/zXyWvUt",
                        "instance_id": "gitter-gitter",
                        "network_id": "gitter"
                      }
                    ],
                    "location_fields": [
                      "room"
                    ],
                    "user_fields": [
                      "username"
                    ]
                  },
                  "irc": {
                    "field_types": {
                      "channel": {
                        "placeholder": "#foobar",
                        "regexp": "#[^\\\\s]+"
                      },
                      "network": {
                        "placeholder": "irc.example.org",
                        "regexp": "([a-z0-9]+\\\\.)*[a-z0-9]+"
                      },
                      "nickname": {
                        "placeholder": "username",
                        "regexp": "[^\\\\s]+"
                      }
                    },
                    "icon": "mxc://example.org/aBcDeFgH",
                    "instances": [
                      {
                        "desc": "Freenode",
                        "fields": {
                          "network": "freenode.net"
                        },
                        "icon": "mxc://example.org/JkLmNoPq",
                        "instance_id": "irc-freenode",
                        "network_id": "freenode"
                      }
                    ],
                    "location_fields": [
                      "network",
                      "channel"
                    ],
                    "user_fields": [
                      "network",
                      "nickname"
                    ]
                  }
                }
                """;
    }
}
