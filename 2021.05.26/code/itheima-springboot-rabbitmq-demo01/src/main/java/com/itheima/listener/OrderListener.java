package com.itheima.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 监听queue2 逻辑： 先获取消息 获取到消息中的订单的ID 2.从数据库获取订单的状态 3.判断订单是否已经支付，如果没有支付，回滚库存
 */
@Component
@RabbitListener(queues = "queue_order_queue2")
public class OrderListener {

    @RabbitHandler
    public void orderhandler(Message message, Channel channel, String msg) {
        System.out.println("获取到消息:" + msg + ":时间为:" + new Date());
        try {
            System.out.println("模拟检查开始=====start");
            Thread.sleep(1000);
            System.out.println("模拟检查结束=====end");
            System.out.println("用户没付款，检查没通过，进入回滚库存处理");
            //签收消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            //拒绝。。。。。别的处理的方式
        }
    }
}