package com.humi.jetlinks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProtocolConfig {
    
    /**
     * 配置Alink协议相关全局参数.
     */
    @Bean(name = "alinkProtocolProperties")
    public Map<String, Object> alinkProtocolProperties() {
        Map<String, Object> properties = new HashMap<>();
        // Alink标准协议版本号
        properties.put("protocolVersion", "1.0");
        // 默认MQTT QoS级别
        properties.put("defaultQos", 1);
        // 消息编码字符集
        properties.put("encoding", "UTF-8");
        // 是否为忽米定制扩展协议
        properties.put("humiExtended", true);
        return properties;
    }
}