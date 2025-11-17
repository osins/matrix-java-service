package io.osins.matrix.shared.tracing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class TracingWebClientCfg {
    @Bean
    WebClient webClient(WebClient.Builder builder) { return builder.build(); }
}