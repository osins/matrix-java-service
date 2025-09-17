package club.hm.matrix.client.server.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = false, fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class MxResponse implements Serializable {
    public String errcode;
    public String error;
}
