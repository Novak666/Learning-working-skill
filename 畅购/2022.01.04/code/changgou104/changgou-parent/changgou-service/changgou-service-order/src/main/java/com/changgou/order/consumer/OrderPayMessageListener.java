package com.changgou.order.consumer;

import com.alibaba.fastjson.JSON;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/12/06 15:28
 **/
@Component
@RabbitListener(queues = "queue.order")//指定监听的队列名
public class OrderPayMessageListener {

    @Autowired
    private OrderMapper orderMapper;

    //写一个方法处理业务逻辑  监听消息 修改订单的状态 支付时间 交易流水
    @RabbitHandler //处理字符类型
    public void handler(String msg) throws Exception{
        if (!StringUtils.isEmpty(msg)) {
            //1.接收消息 转成MAP 对象
            Map<String, String> map = JSON.parseObject(msg, Map.class);
            if(map.get("return_code").equals("SUCCESS")) {
                //2.判断 是否支付成功 如果支付成功 更新订单的状态和支付时间 交易流水
                String out_trade_no = map.get("out_trade_no");
                Order order = orderMapper.selectByPrimaryKey(out_trade_no);
                if(map.get("result_code").equals("SUCCESS")){
                    //2.1 先根据订单号 获取订单的数据
                    if(order!=null && order.getIsDelete().equals("0")) {
                        //2.2 修改状态 和交易流水 和 支付时间 更新时间
                        order.setPayStatus("1");//已经支付
                        order.setUpdateTime(new Date());//更新时间

                        //支付时间
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date date = simpleDateFormat.parse(map.get("time_end"));
                        order.setPayTime(date);
                        order.setTransactionId(map.get("transaction_id"));
                        //2.3 执行SQL 保存到数据库中
                        orderMapper.updateByPrimaryKeySelective(order);
                    }
                }else {
                    //3.判断 是否支付成功 如果支付失败 ：关闭交易 判断 成功 删除订单
                    //todo 模拟浏览器发送请求给微信支付 关闭订单
                    //删除订单
                    order.setIsDelete("1");//删除
                    orderMapper.updateByPrimaryKeySelective(order);
                }
            }else{
                //
                System.out.println("通信失败");
            }
        }else{
            System.out.println("为空");//log.error("出错")
        }
    }
}
