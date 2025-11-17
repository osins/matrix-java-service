package io.osins.matrix.client.server.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Accessors(chain = true, fluent = false)
@AllArgsConstructor
@NoArgsConstructor
public class MatrixJoinResponse extends MatrixResponse{
    @JsonProperty("room_id")
    private String roomId;
}
