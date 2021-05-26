package com.itheima.controller;

import com.itheima.confirm.MyConfirmCallback;
import com.itheima.confirm.MyReturnCallBack;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/16 08:46
 * @description 标题
 * @package com.itheima.controller
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MyConfirmCallback myConfirmCallback;

    @Autowired
    private MyReturnCallBack myReturnCallBack;

    @RequestMapping("/send1")
    public String send1(){
        //发送消息 有可能丢失

        //可以使用confirm模式
        rabbitTemplate.setConfirmCallback(myConfirmCallback);

        //设置return模式
        rabbitTemplate.setReturnCallback(myReturnCallBack);

        rabbitTemplate.convertAndSend("exchange_direct_demo01","item.insert","数据");

        return "ok";
    }

    @RequestMapping("/send2")
    public String send2(){

        rabbitTemplate.convertAndSend("exchange_direct_demo02","item.ttl","测试ttl数据");

        return "ok";
    }

    @RequestMapping("/send3")
    public String send3(){

        rabbitTemplate.convertAndSend("queue_demo03_deq","dlx消息本身");

        return "ok";
    }

    //模拟下单
    @RequestMapping("/send5")
    public String send5(){
        //模拟下单
        System.out.println("下单成功=================");
        //模拟减库存
        System.out.println("减库存成功================");

        System.out.println("下单的时间："+new Date());

        //生产者 发送消息
        rabbitTemplate.convertAndSend("queue_order_queue1", (Object) "延迟队列的消息：orderId的值:123456", new MessagePostProcessor() {
            //设置过期时间 设置消息的一些属性
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");//设置过期时间 单位是毫秒
                return message;
            }
        });

        return "ok";
    }
}
