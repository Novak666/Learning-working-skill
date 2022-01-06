package com.changgou.seckill.consumer;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import entity.SystemConstants;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2022/01/06 19:24
 **/
@Component
@RabbitListener(queues = "queue.seckillorder")
public class SeckillUpdateOrderListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedissonClient redissonClient;


    //将redis中的数据 存储到mysql中
    @RabbitHandler
    public void jieshouMessage(String msg) {
        //1.转换数据为MAP
        Map<String, String> map = JSON.parseObject(msg, Map.class);
        //map里面有一个attach  attache 里面有username的值

        if (map.get("return_code").equals("SUCCESS")) {
            //{type:1,username:zhangsan}
            Map<String, String> attachMap = JSON.parseObject(map.get("attach"), Map.class);
            //2.判断如果支付成功  将预订单从redis中 存储到Mysql
            if (map.get("result_code").equals("SUCCESS")) {
                //2.1 先获取到预订单
                SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).get(attachMap.get("username"));
                //2.2 修改补充属性再存储到mysql
                String time_end = map.get("time_end");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                try {
                    Date payTime = simpleDateFormat.parse(time_end);
                    //设置支付时间
                    seckillOrder.setPayTime(payTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //已经支付
                seckillOrder.setStatus("1");
                //设置交易流水
                seckillOrder.setTransactionId(map.get("transaction_id"));
                seckillOrderMapper.insertSelective(seckillOrder);
                //redis预订单删除
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).delete(attachMap.get("username"));
                //清除掉排队标识
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_QUEUE_REPEAT_KEY).delete(attachMap.get("username"));
                //清除抢单的状态数据
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).delete(attachMap.get("username"));

            } else {
                //3.判断如果支付失败  关闭微信的交易订单 恢复库存 删除预订单
                //3.1 todo  关闭微信订单 使用Httpclient 发送请求 关闭订单
                SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).get(attachMap.get("username"));
                SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + seckillStatus.getTime()).get(seckillStatus.getGoodsId());
                if (seckillGoods == null) {
                    //1.从数据库查询 设置到redis中 //2.修改数据库的库存是为1 todo
                    seckillGoods = seckillGoodsMapper.selectByPrimaryKey(seckillStatus.getGoodsId());
                }
                RLock lock = redissonClient.getLock("huifuku:" + seckillStatus.getGoodsId());
                //上锁
                try {
                    lock.lock(5, TimeUnit.SECONDS);
                    seckillGoods.setStockCount(seckillGoods.getStockCount() + 1);
                    redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + seckillStatus.getTime()).put(seckillStatus.getGoodsId(), seckillGoods);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    //释放锁
                    lock.unlock();

                }


                //redis预订单删除
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).delete(attachMap.get("username"));
                //清除掉排队标识
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_QUEUE_REPEAT_KEY).delete(attachMap.get("username"));
                //清除抢单的状态数据
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).delete(attachMap.get("username"));


            }
        } else {
            System.out.println("通信不成功");
        }

    }
}
