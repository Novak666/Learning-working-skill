package com.changgou.seckill.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.seckill.service.SeckillOrderService;
import com.changgou.seckill.task.MultiThreadingCreateOrder;
import entity.IdWorker;
import entity.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/****
 * @Author:admin
 * @Description:SeckillOrder业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SeckillOrderServiceImpl extends CoreServiceImpl<SeckillOrder> implements SeckillOrderService {

    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MultiThreadingCreateOrder multiThreadingCreateOrder;

    @Autowired
    public SeckillOrderServiceImpl(SeckillOrderMapper seckillOrderMapper) {
        super(seckillOrderMapper, SeckillOrder.class);
        this.seckillOrderMapper = seckillOrderMapper;
    }

    @Override
    public boolean add(Long id, String time, String username) {
        //1.根据商品的ID 获取商品数据
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).get(id);
        //2.判断商品是否存在 如果不存在 标识售罄 并返回 throws runtime
        if (seckillGoods == null || seckillGoods.getStockCount() <= 0) {
            throw new RuntimeException("已经售罄");
        }
        // 压入队列 队列的元素就是抢单的信息（谁抢 抢的是哪一个商品的ID 和时间段 抢单状态 默认是 1 排队中）
        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(), 1, id, time);

        redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).leftPush(seckillStatus);

        //调用多线程下单 （spring的方式：1.启用注解  2.创建一类 交给spring容器 编写一个方法（下单的方法） 3.方法上修饰一个注解@Async 4.调用该方法）
        multiThreadingCreateOrder.createOrder();
        return true;
    }
}
