package com.itheima.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 该类用于监听消息
 *
 * @author ljh
 * @version 1.0
 * @date 2020/12/15 12:18
 * @description 标题
 * @package com.itheima.listener
 */
@Component
public class MyRabbitmqListener {

    //该方法 就用于监听队列中的消息 一旦有消息就触发该方法进行调用
    @RabbitListener(queues = "springboot_topic_queue")
    public void jieshouMsg(String msg) {
        System.out.println(msg);
    }
}
