package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/19 15:54
 **/
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Override
    public void add(Integer num, Long id, String username) {
        if(num<=0){
            //删除掉
            redisTemplate.boundHashOps("Cart_" + username).delete(id);
            return;
        }
        //1.根据商品的sku的ID 获取商品的数据
        //1.1 在changgou-service-goods-api中创建feign接口 sku,spu
        //1.2 需要在changgou-service-goods微服务中实现feign接口
        //1.3 订单微服务添加依赖
        //1.4 订单微服务启动类中 启用feignclients 注入 使用
        Sku sku = skuFeign.findById(id).getData();

        //2.将商品的数据 进行转换到POJO（orderItem）中
        OrderItem orderItem = new OrderItem();

        //设置一级 二级 三级分类的ID  需要先通过sku的ID 获取到spu的ID 再通过spu的ID 获取spu的数据
        Spu spu = spuFeign.findById(sku.getSpuId()).getData();
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setImage(sku.getImage());
        orderItem.setPrice(sku.getPrice());
        orderItem.setSkuId(id);
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setName(sku.getName());
        orderItem.setNum(num);
        orderItem.setMoney(num*sku.getPrice());//应付金额
        orderItem.setPayMoney(num*sku.getPrice());//实付金额
        //3.将数据存到hash类型的redis中 存储到购物车中 1.起步依赖 2. 配置host:port 3.注入redisTemplate
        //hset key field value
        redisTemplate.boundHashOps("Cart_" + username).put(id, orderItem);
    }

    @Override
    public List<OrderItem> list(String username) {
        return redisTemplate.boundHashOps("Cart_" + username).values();
    }
}
