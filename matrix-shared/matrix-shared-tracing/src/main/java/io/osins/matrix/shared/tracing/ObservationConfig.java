package io.osins.matrix.shared.tracing;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservationConfig {

    @Bean
    public ObservationRegistry observationRegistry(MeterRegistry meterRegistry) {

        var registry = ObservationRegistry.create();

        // 添加指标处理
        registry.observationConfig()
                .observationHandler(new DefaultMeterObservationHandler(meterRegistry));

        return registry;
    }
}