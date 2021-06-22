package com.itheima.dao;

import com.itheima.domain.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AccountDao {

    void inMoney(@Param("name") String name, @Param("money") Double money);

    void outMoney(@Param("name") String name, @Param("money") Double money);

    void b();
}
