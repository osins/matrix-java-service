package club.hm.matrix.client.server.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = false)
@AllArgsConstructor
@NoArgsConstructor
public class WellKnownClient {
    @JsonProperty("m.homeserver")
    private Homeserver homeserver;

    @JsonProperty("m.identity_server")
    private IdentityServer identityServer;

    @JsonProperty("org.example.custom.property")
    private CustomProperty customProperty;

    @Data
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Homeserver {
        @JsonProperty("base_url")
        private String baseUrl;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdentityServer {
        @JsonProperty("base_url")
        private String baseUrl;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomProperty {
        @JsonProperty("app_url")
        private String appUrl;
    }
}