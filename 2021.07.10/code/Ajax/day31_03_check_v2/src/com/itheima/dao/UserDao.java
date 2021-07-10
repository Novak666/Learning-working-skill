package com.itheima.dao;

import com.itheima.pojo.User;
import com.itheima.utils.DruidUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

/**
 * 包名:com.itheima.dao
 *
 * @author Leevi
 * 日期2020-10-22  10:13
 */
public class UserDao {
    private QueryRunner queryRunner = new QueryRunner(DruidUtil.getDataSource());
    public User findByUsername(String username) throws Exception {
        String sql = "select * from user where username=?";
        User user = queryRunner.query(sql, new BeanHandler<>(User.class), username);
        return user;
    }
}
