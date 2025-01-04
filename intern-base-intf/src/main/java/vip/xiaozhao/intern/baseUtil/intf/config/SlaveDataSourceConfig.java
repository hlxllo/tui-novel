package vip.xiaozhao.intern.baseUtil.intf.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@Data
//@ConfigurationProperties(prefix = "spring.datasource.slaves")
public class SlaveDataSourceConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;

}
