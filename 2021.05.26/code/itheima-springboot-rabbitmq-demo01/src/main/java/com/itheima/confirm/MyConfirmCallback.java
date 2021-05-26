package com.itheima.confirm;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/16 08:57
 * @description 标题
 * @package com.itheima.confirm
 */
@Component
public class MyConfirmCallback implements RabbitTemplate.ConfirmCallback {
    //回调函数

    /**
     * @param correlationData
     * @param ack             是否确认发送成功 如果是true 标识成功  false 表示失败
     * @param cause           如果是成功 cause 是null值 如果是失败 有失败的信息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            // 下单 成功 扣款成功 通知 另外一个系统 +钱
            System.out.println("成功======");
        } else {
            System.out.println("失败，失败的原因：" + cause);
        }
    }
}
