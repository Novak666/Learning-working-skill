package com.itheima.service;

import com.itheima.config.SpringConfig;
import com.itheima.domain.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    public void testSave() {
        Account account = new Account();
        account.setName("阿尔萨斯");
        account.setMoney(999.99d);
        accountService.save(account);
    }

    @Test
    public void testDelete() {
        accountService.delete(6);
    }

    @Test
    public void testUpdate() {
        Account account = new Account();
        account.setId(7);
        account.setName("itheima");
        account.setMoney(6666666666.66d);
        accountService.update(account);
    }

    @Test
    public void testFindNameById() {
        String name = accountService.findNameById(2);
        System.out.println(name);
    }

    @Test
    public void testFindById() {
        Account account = accountService.findById(2);
        System.out.println(account);
    }

    @Test
    public void testFindAll() {
        List<Account> list = accountService.findAll();
        System.out.println(list);
    }

    @Test
    public void testFindAll1() {
        List<Account> list = accountService.findAll(1, 2);
        System.out.println(list);
    }

    @Test
    public void testGetCount() {
        Long count = accountService.getCount();
        System.out.println(count);
    }
}