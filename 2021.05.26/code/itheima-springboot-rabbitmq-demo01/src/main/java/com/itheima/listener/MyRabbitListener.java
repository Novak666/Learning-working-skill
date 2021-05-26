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
 * @date 2020/12/16 09:50
 * @description 标题
 * @package com.itheima.listener
 */
@Component
@RabbitListener(queues = "queue_demo01",concurrency = "10-100")//指定要监听的队列名称
public class MyRabbitListener {

    /**
     *
     * @param message 消息本身（消息的内容 和包括消息包含一些别的数据：比如：交换机 消息的序号）
     * @param channel 通道
     * @param msg  消息本身 只是 消息内容
     */
    @RabbitHandler//处理消息
    public void msg(Message message, Channel channel, String msg){

        //System.out.println("接收到的消息是："+msg);
        //模拟处理业务
        try {
            System.out.println("开始=================");
            Thread.sleep(1000);
            //int i=1/0;
            //System.out.println("结束=================");
            //成功 就应该要签收消息
            //参数1 指定消息的序号
            //参数2 指定是否批量的进行签收 true 表示批量处理签收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
        } catch (Exception e) {
            e.printStackTrace();
            //异常 就要拒绝签收

            try {
                //参数1 指定消息的序号
                //参数2 指定是否批量的进行签收 true 表示批量处理拒绝签收
                //参数3 指定 是否重回队列 true 表示消息重回队列  false 表示 丢弃消息
                //channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,true);

                //参数1 指定消息的序号
                //参数2 指定 是否重回队列 true 表示消息重回队列  false 表示 丢弃消息
                channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);


            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
