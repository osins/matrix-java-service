package io.osins.shared.common.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@ToString
public class KeyValue<K extends Serializable, V extends Serializable, D extends Serializable> implements Serializable {
    private K key;
    private V value;
    private D desc;
}
