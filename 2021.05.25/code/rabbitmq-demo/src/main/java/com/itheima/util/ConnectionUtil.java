package com.itheima.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/15 10:48
 * @description 标题
 * @package com.itheima.util
 */
public class ConnectionUtil {
    public static Connection getConnection() throws Exception {
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
        return connection;
    }
}
