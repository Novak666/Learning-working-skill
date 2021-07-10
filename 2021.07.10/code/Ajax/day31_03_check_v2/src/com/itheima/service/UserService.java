package com.itheima.service;

import com.itheima.dao.UserDao;
import com.itheima.pojo.User;

/**
 * 包名:com.itheima.service
 *
 * @author Leevi
 * 日期2020-10-22  10:13
 */
public class UserService {
    private UserDao userDao = new UserDao();
    public User findByUsername(String username) throws Exception {
        return userDao.findByUsername(username);
    }
}
