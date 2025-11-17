package io.osins.matrix.client.server.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Data
@Builder
@Accessors(chain = false)
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAvailableResult implements Serializable {
    private boolean available;
}