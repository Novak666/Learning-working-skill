package com.itheima.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TtlConfig {

    //创建过期队列 过期时间（队列的过期时间 10S）
    @Bean
    public Queue createqueuettl1(){
        //设置队列过期时间为10000 10S钟
        //设置参数 x-message-ttl 设置过期时间的参数名
        // 值 10000 单位是毫秒
        return QueueBuilder.durable("queue_demo02").withArgument("x-message-ttl",10000).build();
    }

    //创建交换机

    @Bean
    public DirectExchange createExchangettl(){
        return new DirectExchange("exchange_direct_demo02");
    }

    //创建绑定
    @Bean
    public Binding createBindingttl(){
        return BindingBuilder.bind(createqueuettl1()).to(createExchangettl()).with("item.ttl");
    }
}