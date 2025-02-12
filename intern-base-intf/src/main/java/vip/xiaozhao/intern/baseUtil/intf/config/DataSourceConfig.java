package vip.xiaozhao.intern.baseUtil.intf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import vip.xiaozhao.intern.baseUtil.intf.constant.SlaveConstant;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    // 配置主数据源
    @Bean(name = "masterDataSource")
    @Primary
    public DataSource masterDataSource() throws ClassNotFoundException {
        DataSourceProperties.DataSourceConfig masterConfig = dataSourceProperties.getMaster();
        // 获取驱动信息
        String type = masterConfig.getType();
        Class<? extends DataSource> driver = (Class<? extends DataSource>) Class.forName(type);
        return DataSourceBuilder.create()
                .url(masterConfig.getUrl())
                .username(masterConfig.getUsername())
                .password(masterConfig.getPassword())
                .driverClassName(masterConfig.getDriverClassName())
                .type(driver)
                .build();
    }

    // 配置从数据源
    @Bean
    public DynamicDataSource dataSource(@Autowired DataSource masterDataSource) throws ClassNotFoundException {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", masterDataSource);

        List<DataSourceProperties.SlaveDataSourceConfig> slaveConfigs = dataSourceProperties.getSlaves();
        for (int i = 0; i < slaveConfigs.size(); i++) {
            DataSourceProperties.SlaveDataSourceConfig slaveConfig = slaveConfigs.get(i);
            // 获取驱动信息
            String type = slaveConfig.getType();
            Class<? extends DataSource> driver = (Class<? extends DataSource>) Class.forName(type);
            DataSource slaveDataSource = DataSourceBuilder.create()
                    .url(slaveConfig.getUrl())
                    .username(slaveConfig.getUsername())
                    .password(slaveConfig.getPassword())
                    .driverClassName(slaveConfig.getDriverClassName())
                    .type(driver)
                    .build();
            SlaveConstant.slaveNum++;
            targetDataSources.put("slave" + (i + 1), slaveDataSource);
        }

        return new DynamicDataSource(masterDataSource, targetDataSources);
    }
}
