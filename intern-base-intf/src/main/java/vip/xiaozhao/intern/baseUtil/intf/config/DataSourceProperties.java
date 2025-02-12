package vip.xiaozhao.intern.baseUtil.intf.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

    private DataSourceConfig master;
    private List<SlaveDataSourceConfig> slaves;

    // 内部静态类，用来映射主数据源配置
    @Data
    public static class DataSourceConfig {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private String type;
    }

    // 用来映射从数据源配置的类
    @Data
    public static class SlaveDataSourceConfig {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private String type;
    }
}
