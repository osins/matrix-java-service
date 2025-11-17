package io.osins.matrix.shared.grpc.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "local.grpc")
public class LocalGrpcProperties {
    private Map<String, String> servicePorts;
}