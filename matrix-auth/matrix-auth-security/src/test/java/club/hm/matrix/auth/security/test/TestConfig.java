package club.hm.matrix.auth.security.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "club.hm.matrix")
public class TestConfig {
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
