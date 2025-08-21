package club.hm.matrix.shared.sms.aliyun.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hm.sms.aliyun")
public class AliyunSettings {
    private String accessKeyId;
    private String accessKeySecret;
}
