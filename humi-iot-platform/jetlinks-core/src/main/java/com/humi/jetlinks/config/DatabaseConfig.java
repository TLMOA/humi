package com.humi.jetlinks.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    
    /**
     * TDengine时序数据库数据源配置.
     * 专用于存储设备上报的时序数据，如传感器读数。
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.tdengine")
    public DataSource tdengineDataSource() {
        return new DruidDataSource();
    }
    
    /**
     * TDengine专用的JdbcTemplate.
     */
    @Bean(name = "tdengineJdbcTemplate")
    public NamedParameterJdbcTemplate tdengineJdbcTemplate() {
        return new NamedParameterJdbcTemplate(tdengineDataSource());
    }
}