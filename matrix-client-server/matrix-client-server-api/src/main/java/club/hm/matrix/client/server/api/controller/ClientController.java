package club.hm.matrix.client.server.api.controller;

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
}
