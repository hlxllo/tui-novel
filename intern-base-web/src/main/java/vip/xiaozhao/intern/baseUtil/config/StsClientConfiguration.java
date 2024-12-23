package vip.xiaozhao.intern.baseUtil.config;

import com.aliyun.sts20150401.Client;
import com.aliyun.teaopenapi.models.Config;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StsClientConfiguration {

    @Resource
    private StsProperties stsProperties;

    @Bean
    public Client stsClient() {
        // 当您在初始化凭据客户端不传入任何参数时，Credentials工具会使用默认凭据链方式初始化客户端。
        // 详情请参考：https://help.aliyun.com/zh/sdk/developer-reference/manage-access-credentials
        Config config = new Config();
        // Endpoint 请参考 https://api.aliyun.com/product/Sts
        config.endpoint = "sts.cn-hangzhou.aliyuncs.com";
        com.aliyun.credentials.models.Config credentialConfig = new com.aliyun.credentials.models.Config();
        credentialConfig.setType("access_key");
        credentialConfig.setAccessKeyId(stsProperties.getAccessKeyId());
        credentialConfig.setAccessKeySecret(stsProperties.getAccessKeySecret());
        try {
            com.aliyun.credentials.Client credentials = new com.aliyun.credentials.Client(credentialConfig);
            config.setCredential(credentials);
            return new Client(config);
        } catch (Exception e) {
            throw new IllegalStateException("出错啦: " + e.getMessage(), e);
        }
    }
}
