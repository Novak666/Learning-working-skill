package com.itheima.service.impl;

import com.itheima.dao.BookDao;
import com.itheima.dao.UserDao;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
//定义bean，后面添加bean的id
@Component("userService")
//设定bean的作用域
@Scope("singleton")
public class UserServiceImpl implements UserService {

    @Value("4")
    private int num ;
    @Value("itheima")
    private String version;
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;
    @Autowired
    private BookDao bookDao;

    public UserServiceImpl(){
        System.out.println("service running....");
    }


//    public void setNum(int num) {
//        this.num = num;
//    }
//
//    public void setVersion(String version) {
//        this.version = version;
//    }

    public void save() {
        System.out.println("user service running..." +num+" "+version);
        userDao.save();
        bookDao.save();
    }
    //设定bean的生命周期
    @PreDestroy
    public void destroy(){
        System.out.println("user service destroy...");
    }
    //设定bean的生命周期
    @PostConstruct
    public void init(){
        System.out.println("user service init...");
    }

}
