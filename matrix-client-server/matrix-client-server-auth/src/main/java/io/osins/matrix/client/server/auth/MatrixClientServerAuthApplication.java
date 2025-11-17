package io.osins.matrix.client.server.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Slf4j
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "io.osins.matrix")
@EnableR2dbcRepositories(basePackages = "io.osins.matrix.client.server.data.repos")
public class MatrixClientServerAuthApplication {
    public static void main(String[] args) {
        var app = SpringApplication.run(MatrixClientServerAuthApplication.class, args);
        var env = app.getEnvironment();
        System.out.printf("""
                ====================================================
                启动完成: %s    port: %s
                ====================================================
                """, env.getProperty("spring.application.name"), env.getProperty("server.port"));
    }
}
