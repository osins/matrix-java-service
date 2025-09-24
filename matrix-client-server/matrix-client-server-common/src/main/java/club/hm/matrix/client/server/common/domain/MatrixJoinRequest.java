package club.hm.matrix.client.server.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatrixJoinRequest {

    private String reason;

    @JsonProperty("third_party_signed")
    private ThirdPartySigned thirdPartySigned;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThirdPartySigned {
        private String mxid;
        private String sender;
        private Map<String, Map<String, String>> signatures; // "example.org" -> {"ed25519:0": "some9signature"}
        private String token;
    }
}
