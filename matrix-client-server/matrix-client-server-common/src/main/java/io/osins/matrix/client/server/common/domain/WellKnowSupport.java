package io.osins.matrix.client.server.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class WellKnowSupport {
    private List<Contact> contacts;

    @JsonProperty("support_page")
    private String supportPage;

    @Data
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Contact {
        @JsonProperty("email_address")
        private String emailAddress;

        @JsonProperty("matrix_id")
        private String matrixId;

        private String role;
    }
}
