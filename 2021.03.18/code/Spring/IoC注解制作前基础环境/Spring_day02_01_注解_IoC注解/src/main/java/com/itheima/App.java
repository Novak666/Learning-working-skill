package com.itheima;

import com.itheima.dao.BookDao;
import com.itheima.dao.UserDao;
import com.itheima.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        UserService userService = (UserService) ctx.getBean("userService");
        userService.save();

        UserDao userDao = (UserDao) ctx.getBean("userDao");
        userDao.save();

        BookDao bookDao = (BookDao) ctx.getBean("bookDao");
        bookDao.save();

    }
}
