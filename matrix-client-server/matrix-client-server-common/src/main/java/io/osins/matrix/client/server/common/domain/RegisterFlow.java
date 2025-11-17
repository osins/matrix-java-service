package io.osins.matrix.client.server.common.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RegisterFlow {

    private List<Flow> flows;
    private Map<String, Map<String, String>> params;
    private String session;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Flow {
        private List<String> stages;
    }
}