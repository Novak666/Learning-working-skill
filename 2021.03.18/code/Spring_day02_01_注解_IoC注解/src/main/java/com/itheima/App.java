package com.itheima;

import com.alibaba.druid.pool.DruidDataSource;
import com.itheima.config.SpringConfig;
import com.itheima.config.SpringConfig2;
import com.itheima.dao.BookDao;
import com.itheima.dao.UserDao;
import com.itheima.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

//        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class, SpringConfig2.class);

        UserService userService = (UserService) ctx.getBean("userService");
        userService.save();

//        UserDao userDao = (UserDao) ctx.getBean("userDao");
//        userDao.save();

//        BookDao bookDao = (BookDao) ctx.getBean("bookDao");
//        bookDao.save();

//        DruidDataSource dataSource = (DruidDataSource)ctx.getBean("dataSource");
//        System.out.println(dataSource);

    }
}
