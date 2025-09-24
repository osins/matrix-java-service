package club.hm.matrix.client.server.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/_matrix/client/v3/keys")
public class KeysV3Controller {
    @PostMapping("/query")
    public String getKeysQuery()
    {
        return """
                {
                  "device_keys": {
                    "@alice:example.com": {
                      "JLAFKJWSCS": {
                        "algorithms": [
                          "m.olm.v1.curve25519-aes-sha2",
                          "m.megolm.v1.aes-sha2"
                        ],
                        "device_id": "JLAFKJWSCS",
                        "keys": {
                          "curve25519:JLAFKJWSCS": "3C5BFWi2Y8MaVvjM8M22DBmh24PmgR0nPvJOIArzgyI",
                          "ed25519:JLAFKJWSCS": "lEuiRJBit0IG6nUf5pUzWTUEsRVVe/HJkoKuEww9ULI"
                        },
                        "signatures": {
                          "@alice:example.com": {
                            "ed25519:JLAFKJWSCS": "dSO80A01XiigH3uBiDVx/EjzaoycHcjq9lfQX0uWsqxl2giMIiSPR8a4d291W1ihKJL/a+myXS367WT6NAIcBA"
                          }
                        },
                        "unsigned": {
                          "device_display_name": "Alice's mobile phone"
                        },
                        "user_id": "@alice:example.com"
                      }
                    }
                  },
                  "master_keys": {
                    "@alice:example.com": {
                      "keys": {
                        "ed25519:base64+master+public+key": "base64+master+public+key"
                      },
                      "usage": [
                        "master"
                      ],
                      "user_id": "@alice:example.com"
                    }
                  },
                  "self_signing_keys": {
                    "@alice:example.com": {
                      "keys": {
                        "ed25519:base64+self+signing+public+key": "base64+self+signing+master+public+key"
                      },
                      "signatures": {
                        "@alice:example.com": {
                          "ed25519:base64+master+public+key": "signature+of+self+signing+key"
                        }
                      },
                      "usage": [
                        "self_signing"
                      ],
                      "user_id": "@alice:example.com"
                    }
                  },
                  "user_signing_keys": {
                    "@alice:example.com": {
                      "keys": {
                        "ed25519:base64+user+signing+public+key": "base64+user+signing+master+public+key"
                      },
                      "signatures": {
                        "@alice:example.com": {
                          "ed25519:base64+master+public+key": "signature+of+user+signing+key"
                        }
                      },
                      "usage": [
                        "user_signing"
                      ],
                      "user_id": "@alice:example.com"
                    }
                  }
                }
                """;
    }

    @PostMapping("/upload")
    public String uploadKeys()
    {
        return """
                {
                  "one_time_key_counts": {
                    "signed_curve25519": 20
                  }
                }
                """;
    }
}
