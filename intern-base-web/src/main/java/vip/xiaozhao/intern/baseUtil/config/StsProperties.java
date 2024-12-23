package vip.xiaozhao.intern.baseUtil.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.sts")
@Data
public class StsProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String roleArn;
    private String roleSessionName;
}
