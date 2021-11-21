package com.changgou.order.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/****
 * @Author:admin
 * @Description:Order业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class OrderServiceImpl extends CoreServiceImpl<Order> implements OrderService {

    private OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        super(orderMapper, Order.class);
        this.orderMapper = orderMapper;
    }
}
