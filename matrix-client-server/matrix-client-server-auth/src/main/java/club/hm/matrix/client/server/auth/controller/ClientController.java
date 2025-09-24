package club.hm.matrix.client.server.auth.controller;

import club.hm.matrix.client.server.common.domain.SupportedVersions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/_matrix/client")
public class ClientController {

    @GetMapping("/versions")
    public SupportedVersions getVersions() {
        return SupportedVersions.builder()
                .unstableFeatures(Map.of("org.example.my_feature", true))
                .versions(List.of("r0.0.1", "v1.1"))
                .build();
    }

    @GetMapping("/unstable/org.matrix.msc2965/auth_metadata")
    public String getAuthMetadata() {
        return """
                {
                  "authorization_endpoint": "http://localhost:8008/oauth2/auth",
                  "code_challenge_methods_supported": ["S256"],
                  "grant_types_supported": ["authorization_code", "refresh_token"],
                  "issuer": "http://localhost:8008/",
                  "registration_endpoint": "http://localhost:8008/_matrix/client/v3/register",
                  "response_modes_supported": ["query", "fragment"],
                  "response_types_supported": ["code"],
                  "revocation_endpoint": "http://localhost:8008/oauth2/revoke",
                  "token_endpoint": "http://localhost:8008/oauth2/token"
                }
                """;
    }
}
