package com.itheima.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DlxConfig {


    //创建队列 1  这个接收转发过来的死信 queue1
    @Bean
    public Queue createqueuetdlq(){
        return QueueBuilder.durable("queue_demo03").build();
    }

    //创建队列 2  queue2 用来接收生产者发送过来的消息 然后要过期 变成死信 转发给了queue1
    @Bean
    public Queue createqueuetdelq2(){
        return QueueBuilder
            .durable("queue_demo03_deq")
            .withArgument("x-max-length",1)//设置队列的长度
            .withArgument("x-message-ttl",10000)//设置队列的消息过期时间 10S
            .withArgument("x-dead-letter-exchange","exchange_direct_demo03_dlx")//设置死信交换机名称
            .withArgument("x-dead-letter-routing-key","item.dlx")//设置死信路由key  item.dlx 就是routingkey
            .build();
    }

    //创建死信交换机

    @Bean
    public DirectExchange createExchangedel(){
        return new DirectExchange("exchange_direct_demo03_dlx");
    }

    // queue1 绑定给 死信交换机  routingkey 和 队列转发消息时指定的死信routingkey 要一致
    @Bean
    public Binding createBindingdel(){
        return BindingBuilder.bind(createqueuetdlq()).to(createExchangedel()).with("item.dlx");
    }

}