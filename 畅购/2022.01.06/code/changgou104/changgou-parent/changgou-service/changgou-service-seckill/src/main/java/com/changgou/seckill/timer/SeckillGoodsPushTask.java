package com.changgou.seckill.timer;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import entity.DateUtil;
import entity.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/12/22 15:29
 **/
@Component
public class SeckillGoodsPushTask {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    //让他定时去执行 隔一段时间执行一次
    //cron 表达式 指定何时执行该方法
    @Scheduled(cron = "0/5 * * * * ?")
    public void pushGoodsToRedis() {
        //1.查询符合条件的商品的数据 以当前时间为基准的5个时间段进行时间的匹配
        // select * from tb_seckill_goods where  status='1' and stock_count>0 and 开始时间<=当前时间 < 结束时间
        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date dateMenu : dateMenus) {//10  12 14  16  18
            //日期的字符串类型
            String time = DateUtil.data2str(dateMenu, DateUtil.PATTERN_YYYYMMDDHH);

            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            //已经审核 参数1 指定POJO的属性名
            criteria.andEqualTo("status","1");
            //剩余库存大于0
            criteria.andGreaterThan("stockCount",0);

            //startTime>=开始的时间段
            criteria.andGreaterThanOrEqualTo("startTime",dateMenu);
            //endTime < 开始时间段+2个小时

            //条件 是 查询在redis中不存在的商品的数据
            Set keys = redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).keys();
            if(keys!=null && keys.size()>0) {
                criteria.andNotIn("id", keys);
            }
            criteria.andLessThan("endTime",DateUtil.addDateHour(dateMenu,2));

            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);
            for (SeckillGoods seckillGood : seckillGoods) {
                //2.将数据存储到redis中 hset key field value
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX+time).put(seckillGood.getId(),seckillGood);

            }
            //设置过期时间 2个小时过期
            redisTemplate.expire(SystemConstants.SEC_KILL_GOODS_PREFIX+time,2, TimeUnit.HOURS);
        }

    }
}
