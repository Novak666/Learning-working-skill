package com.changgou.seckill.service;

import com.changgou.core.service.CoreService;
import com.changgou.seckill.pojo.SeckillOrder;

/****
 * @Author:admin
 * @Description:SeckillOrder业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SeckillOrderService extends CoreService<SeckillOrder> {

    /**
     * 抢购下单
     * @param id
     * @param time
     * @param username
     * @return
     */
    boolean add(Long id, String time, String username);
}
