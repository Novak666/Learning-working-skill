package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserService {
    /**
     * 添加用户
     * @param user
     * @return
     */
    @Transactional(readOnly = false)
    public boolean save(User user);

    /**
     * 更新用户
     * @param user
     * @return
     */
    @Transactional(readOnly = false)
    public boolean update(User user);

    /**
     * 删除用户
     * @param uuid
     * @return
     */
    @Transactional(readOnly = false)
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
    //业务层接口修改返回值类型为使用分页插件的格式
    public PageInfo<User> getAll(int page, int size);

    /**
     * 用户登录
     * @param userName 用户名
     * @param password 密码信息
     * @return
     */
    public User login(String userName,String password);
}
