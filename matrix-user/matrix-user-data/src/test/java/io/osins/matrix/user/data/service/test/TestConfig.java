package io.osins.matrix.user.data.service.test;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "io.osins.matrix.user.data")
@EnableR2dbcRepositories(basePackages = "io.osins.matrix.user.data.repository")
public class TestConfig {
}
