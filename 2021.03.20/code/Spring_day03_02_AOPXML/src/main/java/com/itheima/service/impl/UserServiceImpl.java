package com.itheima.service.impl;

import com.itheima.service.UserService;

public class UserServiceImpl implements UserService {

    public void save(int i,int m){
        System.out.println("user service running..."+i+","+m);
//        int i= 1/0;
    }

    public int update() {
        System.out.println("user service update running....");
        return 100;
    }

    public void delete() {
        System.out.println("user service delete running....");
        int i = 1/0;
    }

}
