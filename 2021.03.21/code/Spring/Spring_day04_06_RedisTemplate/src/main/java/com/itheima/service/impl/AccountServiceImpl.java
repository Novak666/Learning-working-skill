package com.itheima.service.impl;


import com.itheima.domain.Account;
import com.itheima.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void save(Account account) {
    }

    public void changeMoney(Integer id, Double money) {
        //等同于redis中set account:id:1 100
        redisTemplate.opsForValue().set("account:id:"+id,money);
    }

    public Double findMondyById(Integer id) {
        //等同于redis中get account:id:1
        Object money = redisTemplate.opsForValue().get("account:id:" + id);
        return new Double(money.toString());
    }
}
//        redisTemplate.type()
//        redisTemplate.persist()
//        redisTemplate.move()
//        redisTemplate.hasKey()
//        redisTemplate.getExpire()
//        redisTemplate.expire()
//        redisTemplate.delete()
//        redisTemplate.rename();
//
//        redisTemplate.opsForValue().;
//        redisTemplate.opsForHash().;
//        redisTemplate.opsForList().;
//        redisTemplate.opsForSet().;
//        redisTemplate.opsForZSet();
//
//
//        redisTemplate.boundValueOps().;
//
//        redisTemplate.slaveOf();
//        redisTemplate.slaveOfNoOne();
//
//        redisTemplate.opsForCluster()