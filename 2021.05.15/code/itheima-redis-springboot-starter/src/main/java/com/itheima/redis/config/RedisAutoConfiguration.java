package com.itheima.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * 配置类 创建一个jedis 对象给到容器中
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 11:46
 * @description 标题
 * @package com.itheima.redis.config
 */
@Configuration
//启用 POJO 和配置yaml的映射关系 自动进行映射
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnClass(Jedis.class)
public class RedisAutoConfiguration {

    @Bean//默认的情况下 不配置bean的名字 使用方法的名字
    //容器中没有名字为jedis的bean的时候 才执行以下的方法
    @ConditionalOnMissingBean(name = "jedis")
    public Jedis jedis(RedisProperties redisProperties){
        System.out.println("host:"+redisProperties.getHost()+";port:"+redisProperties.getPort());
        return new Jedis(redisProperties.getHost(),redisProperties.getPort());
    }

}
