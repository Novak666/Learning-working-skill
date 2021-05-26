package com.itheima;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/16 08:45
 * @description 标题
 * @package com.itheima
 */
@SpringBootApplication
public class SpringRabbitApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringRabbitApplication.class,args);
    }
    //创建队列
    @Bean
    public Queue createQueue(){
        return new Queue("queue_demo01");
    }
    //创建交换机
    @Bean
    public DirectExchange createExchange(){
        return new DirectExchange("exchange_direct_demo01");
    }
    //创建绑定
    @Bean
    public Binding createBinding(){
        return BindingBuilder.bind(createQueue()).to(createExchange()).with("item.insert");
    }

}
