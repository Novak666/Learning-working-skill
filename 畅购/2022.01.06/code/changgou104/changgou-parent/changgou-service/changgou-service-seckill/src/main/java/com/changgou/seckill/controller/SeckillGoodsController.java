package com.changgou.seckill.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SeckillGoodsService;
import entity.DateUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/seckillGoods")
@CrossOrigin
public class SeckillGoodsController extends AbstractCoreController<SeckillGoods>{

    private SeckillGoodsService  seckillGoodsService;

    @Autowired
    public SeckillGoodsController(SeckillGoodsService  seckillGoodsService) {
        super(seckillGoodsService, SeckillGoods.class);
        this.seckillGoodsService = seckillGoodsService;
    }

    /**
     * 获取时间菜单列表
     * @return
     */
    @GetMapping("/menus")
    //@ResponseBody//底层jackson将数据转成JSON 响应给页面,如果是时间类型 自动将时间进行格式化使用utc作为时区，并转成字符串
    public List<Date> menus(){
        return DateUtil.getDateMenus();
    }

    @GetMapping("/list")
    public List<SeckillGoods> getGoods(String time){
        //从redis中获取某一个传递过来的时间段对应的商品的列表
        return seckillGoodsService.list(time);
    }

    /**
     *
     * @param time 时间段值
     * @param id  商品的ID
     * @return
     */
    @GetMapping("/one")
    public Result<SeckillGoods> one(String time, Long id){
        //从redis中获取某一个传递过来的时间段对应的商品的列表
        SeckillGoods seckillGoods =seckillGoodsService.one(time,id);
        return  new Result<>(true, StatusCode.OK,"查询成功",seckillGoods);
    }
}
