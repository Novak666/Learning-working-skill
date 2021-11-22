package com.changgou.order.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.dao.OrderItemMapper;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.OrderService;
import com.changgou.user.feign.UserFeign;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private UserFeign userFeign;

    @Override
    @Transactional(rollbackFor = Exception.class)//本地的spring的声明式事务注解
    public void add(Order order) {
        //1.添加数据到订单表和订单选表中
        //1.1 生成主键
        order.setId(idWorker.nextId() + "");

        //获取redis中购物车的数据 循环遍历 统计即可
        List<OrderItem> orderItemList = redisTemplate.boundHashOps("Cart_" + order.getUsername()).values();
        Integer totalNum = 0;
        Integer totalMoney = 0;
        //数据是购物车中来的数据
        for (OrderItem orderItem : orderItemList) {

            totalNum += orderItem.getNum();//数量累加
            totalMoney += orderItem.getMoney();//金额

            //订单的选项
            orderItem.setId(idWorker.nextId()+"");
            orderItem.setOrderId(order.getId());
            orderItemMapper.insertSelective(orderItem);

            //2.减库存 通过feign调用减库存
            //2.1 changgou-service-goods-api中定一一个fegin接口
            //2.2 编写一个方法 （根据购买数量和购买的sku的ID 更新库存（减库存））
            //2.3 在changgou-service-goods微服务中实现接口
            //2.4 order微服务添加依赖 并启用feignclients 注入调用
            //update tb_sku set num=num-#{num} where id=#{id} and num>=#{num}
            skuFeign.decCount(orderItem.getSkuId(), orderItem.getNum());
        }
        //1.2 数量合计 和金额合计 todo
        order.setTotalNum(totalNum);
        order.setTotalMoney(totalMoney);
        //1.4 设置实付金额 todo
        order.setPayMoney(totalMoney);

        //1.3 设置邮费免邮
        order.setPostFee(0);
        //1.5 设置创建和更新时间
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());

        order.setBuyerRate("0");//未评价

        //1.6设置状态
        order.setPayStatus("0");
        order.setConsignStatus("0");
        order.setOrderStatus("0");
        order.setIsDelete("0");//未删除
        orderMapper.insertSelective(order);

        //3.加积分
        //3.加积分 通过feign调用减库存
        //3.1 changgou-service-user-api中定一一个fegin接口
        //3.2 编写一个方法 (根据用户名给该用户添加积分值)
        //3.3 在changgou-service-user微服务中实现接口
        //3.4 order微服务添加依赖 并启用feignclients 注入调用
        //update tb_user set points=points+#{points} where username=#{username}
        //调用feign 获取积分值
        userFeign.addPoints(order.getUsername(),10);
        //int i=1/0;todo 分布式事务的问题
        //4.清除购物车
        redisTemplate.delete("Cart_" + order.getUsername());

    }
}
