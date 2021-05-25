package com.itheima.routing;

import com.itheima.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 路由模式
 *
 * @author ljh
 * @version 1.0
 * @date 2020/12/15 10:47
 * @description 标题
 * @package com.itheima.work
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();

        //创建交换机 广播类型（路由模式）
        channel.exchangeDeclare("exchange_direct", BuiltinExchangeType.DIRECT);


        //创建声明队列(创建队列) 2个以上
        //参数1 指定队列的名称
        //参数2 指定是消息否持久化  一般设置为true 要保存到磁盘中
        //参数3 指定是否独占通道  不独占
        //参数4 指定是否自动删除 不要自动删除。
        //参数5 指定额外的参数 可以不指定
        channel.queueDeclare("direct_queue1", true, false, false, null);
        channel.queueDeclare("direct_queue2", true, false, false, null);

        //将队列绑定到指定的交换机上
        //要绑定的队列
        //要绑定到的交换机
        //设置routingkey 路由模式中一定要指定一个routingkey 标识他的标记位
        channel.queueBind("direct_queue1","exchange_direct","order.insert");
        channel.queueBind("direct_queue2","exchange_direct","order.select");

        //创建消息
        String msg1 = "hello i am from direct producer 做insert";
        String msg2 = "hello i am from direct producer 做select";

        //消息发送
        //参数1 指定交换机 需要指定交换机
        //参数2 指定routingkey  指定routingkey 标识不同的业务操作类型等。
        //参数3 指定携带的额外的参数 null
        //参数4 要发送的消息本身（字节数组）
        channel.basicPublish("exchange_direct", "order.insert", null, msg1.getBytes());
        channel.basicPublish("exchange_direct", "order.select", null, msg2.getBytes());


        //关闭资源
        channel.close();
        connection.close();
    }
}
