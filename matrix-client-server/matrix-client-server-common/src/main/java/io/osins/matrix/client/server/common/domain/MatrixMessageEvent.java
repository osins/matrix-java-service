package io.osins.matrix.client.server.common.domain;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuperBuilder
@NoArgsConstructor
public class MatrixMessageEvent extends MatrixEvent<RoomMessageContent>{
}
