package com.itheima.confirm;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/16 09:07
 * @description 标题
 * @package com.itheima.confirm
 */
@Component
public class MyReturnCallBack  implements RabbitTemplate.ReturnCallback {
    /**
     * 一旦出现错误则调用该方法  人工去做
     * @param message  消息本身
     * @param replyCode 响应的状态码
     * @param replyText 错误的信息描述
     * @param exchange 交换机
     * @param routingKey 路由key
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("消息本身："+new String(message.getBody()));
        System.out.println("退回的replyCode是："+replyCode);
        System.out.println("退回的replyText是："+replyText);
        System.out.println("退回的exchange是："+exchange);
        System.out.println("退回的routingKey是："+routingKey);
    }
}
