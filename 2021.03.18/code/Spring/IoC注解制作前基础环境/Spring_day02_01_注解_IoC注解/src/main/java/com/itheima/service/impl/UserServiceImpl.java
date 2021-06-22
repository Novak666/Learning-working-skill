package com.itheima.service.impl;

import com.itheima.service.UserService;

public class UserServiceImpl implements UserService {

    public void save() {
        System.out.println("user service running...");
    }

    public void init(){
        System.out.println("user servier init...");
    }

    public void destroy(){
        System.out.println("user servier destroy...");
    }

}
