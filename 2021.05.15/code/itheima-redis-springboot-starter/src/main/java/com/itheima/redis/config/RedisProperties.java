package com.itheima.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 11:54
 * @description 标题
 * @package com.itheima.redis.config
 */
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {
    private String host = "localhost";
    private Integer port = 6379;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
