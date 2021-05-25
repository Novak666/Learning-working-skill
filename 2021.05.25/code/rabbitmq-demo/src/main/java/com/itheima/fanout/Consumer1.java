package com.itheima.fanout;

import com.itheima.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 广播模式接收消息
 *
 * @author ljh
 * @version 1.0
 * @date 2020/12/15 09:57
 * @description 标题
 * @package com.itheima.simple
 */
public class Consumer1 {

    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare("fanout_queue1", true, false, false, null);
        //创建消费者，并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            //重写父类的方法 接收到消息之后要进行处理消息

            /**
             *
             * @param consumerTag 消费者的别名 默认生成一个随机的值
             * @param envelope 消息的其他的数据
             * @param properties 属性信息
             * @param body  消息本身
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                 //模拟打印一下表示接受到消息了
                System.out.println("====广播模式=====consumer1=======消息本身："+new String(body,"utf-8"));
                System.out.println("exchange:"+envelope.getExchange());
                System.out.println("routingkey:"+envelope.getRoutingKey());
                System.out.println("消息的序号:"+envelope.getDeliveryTag());
            }
        };
        //消息监听
        //参数1 指定要监听哪一个队列 （要从该队列中获取消息）
        //参数2 指定消息的应答模式 1. 自动应答 2 手动应答 一般采用自动应答   true:自动应答
        //参数3 指定消费者
        channel.basicConsume("fanout_queue1",true,consumer);
        //关闭资源(不建议关闭，建议一直监听消息)

    }
}
