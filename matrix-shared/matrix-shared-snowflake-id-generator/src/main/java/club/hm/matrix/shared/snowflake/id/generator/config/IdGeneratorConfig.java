package club.hm.matrix.shared.snowflake.id.generator.config;

import club.hm.matrix.shared.snowflake.id.generator.BufferedIdService;
import club.hm.matrix.shared.snowflake.id.generator.IdService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * ID生成器配置类
 */
@Configuration
@ComponentScan(basePackages = "club.hm.matrix.shared.snowflake.id.generator")
public class IdGeneratorConfig {

    @Value("${snowflake-id.generator.datacenter-id:-1}")
    private long datacenterId;

    @Value("${snowflake-id.generator.machine-id:-1}")
    private long machineId;

    @Value("${snowflake-id.generator.buffer-size:1000}")
    private int bufferSize;

    @Bean
    @ConditionalOnMissingBean
    public IdService idService() {
        if (datacenterId >= 0 && machineId >= 0) {
            return new IdService(datacenterId, machineId);
        } else if (datacenterId >= 0) {
            return new IdService(datacenterId);
        } else {
            return new IdService();
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public BufferedIdService bufferedIdService() {
        if (datacenterId >= 0 && machineId >= 0) {
            return new BufferedIdService(datacenterId, machineId, bufferSize);
        } else if (datacenterId >= 0) {
            return new BufferedIdService(datacenterId, bufferSize);
        } else {
            return new BufferedIdService(bufferSize);
        }
    }
}