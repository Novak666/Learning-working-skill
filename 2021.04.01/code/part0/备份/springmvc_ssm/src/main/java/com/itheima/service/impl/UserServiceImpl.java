package com.itheima.service.impl;

import com.itheima.domain.User;
import com.itheima.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public boolean save(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(Integer uuid) {
        return false;
    }

    @Override
    public User get(Integer uuid) {
        return null;
    }

    @Override
    public List<User> getAll(int page, int size) {
        return null;
    }

    @Override
    public User login(String userName, String password) {
        return null;
    }
}
