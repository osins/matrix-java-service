package club.hm.martix.shared.sms.aliyun.config;

import club.hm.martix.shared.sms.aliyun.service.AliyunSmsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModuleSmsConfig {
    @Bean
    public AliyunSmsClient aliyunSmsClient() {
        return AliyunSmsClient.create()
                .orElseThrow(() -> new IllegalStateException("阿里云短信客户端初始化失败"));
    }
}
