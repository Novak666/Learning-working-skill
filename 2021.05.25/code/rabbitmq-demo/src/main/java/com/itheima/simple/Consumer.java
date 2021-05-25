package com.itheima.simple;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 简单模式接收消息
 *
 * @author ljh
 * @version 1.0
 * @date 2020/12/15 09:57
 * @description 标题
 * @package com.itheima.simple
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        //创建链接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置RabbitMQ服务主机地址,默认localhost
        connectionFactory.setHost("localhost");
        //设置RabbitMQ服务端口,默认5672
        connectionFactory.setPort(5672);
        //设置虚拟主机名字，默认/
        connectionFactory.setVirtualHost("/pay");
        //设置用户连接名，默认guest
        connectionFactory.setUsername("lisi");
        //设置链接密码，默认guest
        connectionFactory.setPassword("lisi");
        //创建链接
        Connection connection = connectionFactory.newConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare("simple_queue1", true, false, false, null);
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
                System.out.println("消息本身："+new String(body,"utf-8"));
                System.out.println("exchange:"+envelope.getExchange());
                System.out.println("routingkey:"+envelope.getRoutingKey());
                System.out.println("消息的序号:"+envelope.getDeliveryTag());
            }
        };
        //消息监听
        //参数1 指定要监听哪一个队列 （要从该队列中获取消息）
        //参数2 指定消息的应答模式 1. 自动应答 2 手动应答 一般采用自动应答   true:自动应答
        //参数3 指定消费者
        channel.basicConsume("simple_queue1",true,consumer);
        //关闭资源(不建议关闭，建议一直监听消息)

    }
}
