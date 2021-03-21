package com.itheima.dao;

import com.itheima.domain.Account;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.SqlSessionFactoryBean;

import java.util.List;

public interface AccountDao {

    @Update("update account set money = money + #{money} where name = #{name}")
    void inMoney(@Param("name") String name, @Param("money") Double money);

    @Update("update account set money = money - #{money} where name = #{name}")
    void outMoney(@Param("name") String name, @Param("money") Double money);

}