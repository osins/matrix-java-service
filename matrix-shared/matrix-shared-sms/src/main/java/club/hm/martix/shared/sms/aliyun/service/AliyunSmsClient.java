package club.hm.martix.shared.sms.aliyun.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class AliyunSmsClient extends Client {
    public AliyunSmsClient(Config config) throws Exception {
        super(config);
    }

    public static Optional<AliyunSmsClient> create() {
        try {
            var config = new Config()
                    // 配置 AccessKey ID，请确保代码运行环境配置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                    .setAccessKeyId(Optional.ofNullable(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID")).orElseThrow(() -> new RuntimeException("请配置阿里云 AccessKey ID")))
                    // 配置 AccessKey Secret，请确保代码运行环境配置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                    .setAccessKeySecret(Optional.ofNullable(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET")).orElseThrow(() -> new RuntimeException("请配置阿里云 AccessKey Secret")));

            // 配置 Endpoint。中国站请使用dysmsapi.aliyuncs.com
            config.endpoint = "dysmsapi.aliyuncs.com";
            return Optional.ofNullable(new AliyunSmsClient(config));
        } catch (Exception ex) {
            log.error("创建阿里云短信客户端失败: {}", ex.getMessage(), ex.getCause());
        }

        return Optional.empty();
    }
}
