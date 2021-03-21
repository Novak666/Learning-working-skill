package com.itheima.service;

import com.itheima.config.SpringConfig;
import com.itheima.domain.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.List;

import static org.junit.Assert.*;
//设定spring专用的类加载器
@RunWith(SpringJUnit4ClassRunner.class)
//设定加载的spring上下文对应的配置
@ContextConfiguration(classes = SpringConfig.class)
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Test
    public void test(){
        Jedis jedis = new Jedis("192.168.40.130",6378);
        jedis.set("name","itheima");
        jedis.close();
    }

    @Test
    public void save(){
        Account account = new Account();
        account.setName("Jock");
        account.setMoney(666.66);

    }

    @Test
    public void changeMoney() {
        accountService.changeMoney(1,200D);
    }

    @Test
    public void findMondyById() {
        Double money = accountService.findMondyById(1);
        System.out.println(money);
    }
}