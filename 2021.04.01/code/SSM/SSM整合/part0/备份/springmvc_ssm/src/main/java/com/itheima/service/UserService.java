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
     * 更新用户
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
     * 查询单个用户
     * @param uuid
     * @return
     */
    public User get(Integer uuid);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    public List<User> getAll(int page, int size);

    /**
     * 用户登录
     * @param userName 用户名
     * @param password 密码信息
     * @return
     */
    public User login(String userName,String password);
}
