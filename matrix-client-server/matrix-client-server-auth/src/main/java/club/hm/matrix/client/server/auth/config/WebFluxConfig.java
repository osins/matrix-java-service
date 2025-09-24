package club.hm.matrix.client.server.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        var codecs = configurer.defaultCodecs();
        codecs.jackson2JsonEncoder(
                new Jackson2JsonEncoder(new ObjectMapper(), MediaType.APPLICATION_JSON)
        );
        codecs.jackson2JsonDecoder(
                new Jackson2JsonDecoder(new ObjectMapper(), MediaType.APPLICATION_JSON)
        );
        codecs.enableLoggingRequestDetails(true);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")   // 匹配所有路径
                .allowedOriginPatterns("*") // Spring 5.3+ 推荐用这个代替 allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 如果需要携带 Cookie
    }
}
