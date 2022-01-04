package com.changgou;

import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

import java.net.UnknownHostException;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/12/22 14:54
 **/
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.changgou.seckill.dao")
@EnableScheduling//启用定时任务spring task
@EnableAsync//启用多线程 使用注解的方式开启多线程
public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //设置key的序列化机制 为字符串
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,3);
    }


}
