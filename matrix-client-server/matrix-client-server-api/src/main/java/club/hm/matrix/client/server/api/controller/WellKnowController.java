package club.hm.matrix.client.server.api.controller;

import club.hm.matrix.client.server.common.domain.WellKnowSupport;
import club.hm.matrix.client.server.common.domain.WellKnownClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/.well-known/matrix")
public class WellKnowController {
    @GetMapping("/client")
    public WellKnownClient getWellKnow() {
        return WellKnownClient.builder()
                .homeserver(WellKnownClient.Homeserver.builder()
                        .baseUrl("https://matrix.example.com")
                        .build())
                .identityServer(WellKnownClient.IdentityServer.builder()
                        .baseUrl("https://identity.example.com")
                        .build())
                .customProperty(WellKnownClient.CustomProperty.builder()
                        .appUrl("https://custom.app.example.org")
                        .build())
                .build();
    }

    @GetMapping("/support")
    public WellKnowSupport getSupportInfo() {
        return WellKnowSupport.builder()
                .contacts(List.of(
                        WellKnowSupport.Contact.builder()
                                .emailAddress("admin@example.org")
                                .matrixId("@admin:example.org")
                                .role("m.role.admin")
                                .build(),
                        WellKnowSupport.Contact.builder()
                                .emailAddress("security@example.org")
                                .role("m.role.security")
                                .build()
                ))
                .supportPage("https://example.org/support.html")
                .build();
    }
}
