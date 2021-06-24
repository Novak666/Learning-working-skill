package com.itheima.service;

import com.itheima.domain.User;

import java.util.List;

public interface UserService {
    /**
     * 添加用户
     * @param user
     * @return
     */
    public boolean save(User user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    public boolean update(User user);

    /**
     * 删除用户
     * @param uuid
     * @return
     */
    public boolean delete(Integer uuid);

    /**
     * 查询单个用户信息
     * @param uuid
     * @return
     */
    public User get(Integer uuid);

    /**
     * 查询全部用户信息
     * @return
     */
    public List<User> getAll();

    /**
     * 根据用户名密码进行登录
     * @param userName
     * @param password
     * @return
     */
    public User login(String userName,String password);
}
