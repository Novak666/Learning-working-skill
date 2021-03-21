package com.itheima.dao;

import com.itheima.domain.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AccountDao {
    /**
     * 入账操作
     * @param name      入账用户名
     * @param money     入账金额
     */
    void inMoney(@Param("name") String name, @Param("money") Double money);

    /**
     * 出账操作
     * @param name      出账用户名
     * @param money     出账金额
     */
    void outMoney(@Param("name") String name, @Param("money") Double money);

}


