package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;

import java.util.List;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/19 15:53
 **/
public interface CartService {
    //添加购物车 给指定的用户添加指定的商品以及数量到购物车中
    void add(Integer num, Long id, String username);

    List<OrderItem> list(String username);
}
