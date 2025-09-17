package club.hm.matrix.client.server.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SupportedVersions {

    @JsonProperty("unstable_features")
    private Map<String, Boolean> unstableFeatures;

    private List<String> versions;
}
