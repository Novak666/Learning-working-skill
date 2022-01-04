package com.changgou.order.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.pojo.Order;
import com.changgou.order.service.OrderService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController extends AbstractCoreController<Order>{

    private OrderService  orderService;

    @Autowired
    public OrderController(OrderService  orderService) {
        super(orderService, Order.class);
        this.orderService = orderService;
    }

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 创建订单
     * @param order
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Order order){
        //1.先获取登录的用户名
        String username = tokenDecode.getUsername();
        order.setUsername(username);
        //2.添加数据到订单表和订单选表中
        orderService.add(order);

        return new Result(true, StatusCode.OK,"创建订单成功");
    }
}
