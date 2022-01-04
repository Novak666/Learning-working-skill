package com.changgou.seckill.task;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import entity.IdWorker;
import entity.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2022/01/04 18:04
 **/
@Component
public class MultiThreadingCreateOrder {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private IdWorker idWorker;


    //写一个方法 （下单的方法） 异步调用（多线程的方式调用）
    @Async//标识该方法是一个异步调用（多线程调用）
    public  void createOrder(){
        System.out.println("开始下单============="+Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 从队列中队头弹出 获取到数据
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).rightPop();
        if(seckillStatus!=null) {
            String username = seckillStatus.getUsername();
            Long id = seckillStatus.getGoodsId();
            String time = seckillStatus.getTime();

            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).get(id);
            //5.判断 是否商品的剩余库存==0 如果 是  更新到数据库中
            if (seckillGoods.getStockCount() <= 0) {
                //设置给0
                seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                //删除掉redis中的商品数据
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).delete(id);
            }
            //发放优惠券
            //6.下预订单到redis中   order也要存储到redis中 redis; hash类型： key? value?
            SeckillOrder order = new SeckillOrder();
            //设置属性 hset key field value  field:用户名 value：订单对象
            //主键
            order.setId(idWorker.nextId());
            //购买的商品的ID
            order.setSeckillId(id);
            //单价
            order.setMoney(seckillGoods.getCostPrice());

            //设置所属的用户
            order.setUserId(username);

            //设置创建时间
            order.setCreateTime(new Date());

            //设置状态 未支付
            order.setStatus("0");

            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY).put(username, order);

            //修改用户的抢单的状态
            //seckillStatus.setStatus(seckillStatus.ORDERED);
            //已经下单了
            seckillStatus.setStatus(2);
            //订单号
            seckillStatus.setOrderId(order.getId());
            //金额
            seckillStatus.setMoney(Float.valueOf(order.getMoney()));
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).put(username, seckillStatus);

            //要求只有付款了之后 才清除
        }
        System.out.println("结束下单============"+Thread.currentThread().getName());
    }
}
