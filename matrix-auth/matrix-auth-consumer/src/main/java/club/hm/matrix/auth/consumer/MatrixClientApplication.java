package club.hm.matrix.auth.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "club.hm.matrix")
public class MatrixClientApplication {
    public static void main(String[] args) {
        var app = SpringApplication.run(MatrixClientApplication.class, args);
        var env = app.getEnvironment();
        System.out.printf("""
                ====================================================
                启动完成: %s    port: %s
                ====================================================
                """, env.getProperty("spring.application.name"), env.getProperty("server.port"));
    }
}
