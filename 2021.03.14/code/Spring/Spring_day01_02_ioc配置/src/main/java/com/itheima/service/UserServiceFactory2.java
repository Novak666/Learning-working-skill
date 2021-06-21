package com.itheima.service;

import com.itheima.service.impl.UserServiceImpl;

public class UserServiceFactory2 {

    public UserService getService(){
        System.out.println(" instance factory create object...");
        return new UserServiceImpl();
    }

}
