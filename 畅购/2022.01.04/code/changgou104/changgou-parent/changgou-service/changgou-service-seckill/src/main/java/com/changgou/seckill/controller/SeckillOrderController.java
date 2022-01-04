package com.changgou.seckill.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SeckillOrderService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/seckillOrder")
@CrossOrigin
public class SeckillOrderController extends AbstractCoreController<SeckillOrder>{

    private SeckillOrderService  seckillOrderService;

    @Autowired
    public SeckillOrderController(SeckillOrderService  seckillOrderService) {
        super(seckillOrderService, SeckillOrder.class);
        this.seckillOrderService = seckillOrderService;
    }

    //下单
    @RequestMapping(value = "/add")
    public Result add(String time, Long id){
        String username = "zhangsan";//需要通过令牌获取 token
        boolean flag = this.seckillOrderService.add(id,time,username);
        if(flag){
            return new Result(true, StatusCode.OK,"排队成功,稍等");
        }else{
            return new Result(false, StatusCode.ERROR,"下单失败");
        }
    }
}
