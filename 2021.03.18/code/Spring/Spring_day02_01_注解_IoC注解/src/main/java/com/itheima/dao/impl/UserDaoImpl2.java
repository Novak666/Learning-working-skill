package com.itheima.dao.impl;

import com.itheima.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository("userDao2")
public class UserDaoImpl2 implements UserDao {

    public void save() {
        System.out.println("user dao running...2");
    }
}
