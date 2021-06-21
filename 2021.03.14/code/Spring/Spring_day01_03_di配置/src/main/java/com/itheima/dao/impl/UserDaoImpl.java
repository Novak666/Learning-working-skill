package com.itheima.dao.impl;

import com.itheima.dao.UserDao;

public class UserDaoImpl implements UserDao{

    private String username;
    private String pwd;
    private String driver;

    public UserDaoImpl(String driver,String username, String pwd) {
        this.driver = driver;
        this.username = username;
        this.pwd = pwd;
    }

    public void save(){
        System.out.println("user dao running..."+ username+" "+pwd + " "+ driver);
    }
}
