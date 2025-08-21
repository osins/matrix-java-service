package club.hm.matrix.shared.sms.aliyun.config;

import club.hm.matrix.shared.sms.aliyun.service.AliyunSmsClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliyunSettings.class)
public class ModuleSmsConfig {
    @Bean
    public AliyunSmsClient aliyunSmsClient(AliyunSettings settings) {
        return AliyunSmsClient.create(settings)
                .orElseThrow(() -> new IllegalStateException("阿里云短信客户端初始化失败"));
    }
}
