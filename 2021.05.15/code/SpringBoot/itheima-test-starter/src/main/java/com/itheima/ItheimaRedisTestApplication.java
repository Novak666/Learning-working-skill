package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.Jedis;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 11:48
 * @description 标题
 * @package com.itheima
 */
@SpringBootApplication
public class ItheimaRedisTestApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ItheimaRedisTestApplication.class, args);
        //Object jedis = context.getBean("jedis");
        //System.out.println(jedis);
    }

    @Bean
    public Jedis jedis(){
        System.out.println("hahaha");
        return new Jedis("localhost",666);
    }



}
