package club.hm.matrix.user.data.service.test;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "club.hm.matrix.user.data")
@EnableR2dbcRepositories(basePackages = "club.hm.matrix.user.data.repository")
public class TestConfig {
}
