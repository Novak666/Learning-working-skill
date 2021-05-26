package com.itheima.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/16 11:12
 * @description 标题
 * @package com.itheima.listener
 */
@Component
//@RabbitListener(queues = "queue_demo03_deq")
public class DLxListner {

    @RabbitHandler
    public void jieshouMsg(Message message, Channel channel,String msg){
        System.out.println(msg);

        //模拟拒绝消息 不重回队列
        try {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
