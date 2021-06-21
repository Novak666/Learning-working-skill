package com.itheima.service;

import com.itheima.service.impl.UserServiceImpl;

public class UserServiceFactory {
    public static UserService getService(){
        System.out.println("factory create object...");
        return new UserServiceImpl();
    }
}
