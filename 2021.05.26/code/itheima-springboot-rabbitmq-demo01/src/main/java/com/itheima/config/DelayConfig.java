package com.itheima.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DelayConfig {
    //正常的队列    接收死信队列转移过来的消息
    @Bean
    public Queue createQueue2(){
        return QueueBuilder.durable("queue_order_queue2").build();
    }

    //死信队列   --->将来消息发送到这里  这里不设置过期时间，我们应该在发送消息时设置某一个消息（某一个用户下单的）的过期时间
    @Bean
    public Queue createQueue1(){
        return QueueBuilder
                .durable("queue_order_queue1")
                .withArgument("x-dead-letter-exchange","exchange_order_delay")//设置死信交换机
                .withArgument("x-dead-letter-routing-key","item.order")//设置死信路由key
                .build();
    }

    //创建交换机
    @Bean
    public DirectExchange createOrderExchangeDelay(){
        return new DirectExchange("exchange_order_delay");
    }

    //创建绑定 将正常队列绑定到死信交换机上
    @Bean
    public Binding createBindingDelay(){
        return BindingBuilder.bind(createQueue2()).to(createOrderExchangeDelay()).with("item.order");
    }
}