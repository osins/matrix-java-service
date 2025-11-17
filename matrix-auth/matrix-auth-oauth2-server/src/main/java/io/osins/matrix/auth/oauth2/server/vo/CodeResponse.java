package io.osins.matrix.auth.oauth2.server.vo;

import io.osins.matrix.auth.oauth2.server.enums.ResponseContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CodeResponse implements Serializable {
    private String code;
    private String state;
    private String clientId;
    private ResponseContentType responseContentType;
}
