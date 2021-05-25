package com.itheima;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/15 12:05
 * @description 标题
 * @package com.itheima
 */


@SpringBootApplication
public class RabbitmqApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitmqApplication.class,args);
    }


    //创建队列
    @Bean
    public Queue queue(){
        return new Queue("springboot_topic_queue");
    }

    //创建交换机
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("springboot_topic_exchange");
    }

    //创建绑定
    @Bean
    public Binding createBinding(){
        //将队列绑定给指定的交换机 并设置一个routingkey
        return BindingBuilder.bind(queue()).to(exchange()).with("order.*");
    }

    //发送消息  订单创建了 再发送消息 发送一个主题模式的消息

    @RequestMapping("/order")
    @RestController
    class OrderController{

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @GetMapping("/add")
        public String addOrder(){
            //1.模拟下单
            System.out.println("=======下单中=======");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("=======下单成功=======");
            //2.发送消息 1.创建队列 2.创建交换机 3.创建绑定 提前做好  再发送消息

            //参数1 指定要发送的交换机的名称
            //参数2 指定要发送的routingkey
            //参数3 指定要发送的消息的本身数据
            rabbitTemplate.convertAndSend("springboot_topic_exchange","order.insert","消息本身 insert");
            rabbitTemplate.convertAndSend("springboot_topic_exchange","order.delete","消息本身 delete");

            return "success";

        }

    }
}
