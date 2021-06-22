package com.itheima.dao.impl;

import com.itheima.dao.AccountDao;
import com.itheima.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//dao注册为bean
@Repository
//@Primary
public class AccountDaoImpl2 implements AccountDao {

    //注入模板对象
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void save(Account account) {
        String sql = "insert into account(name,money)values(:name,:money)";
        Map pm = new HashMap();
        pm.put("name",account.getName());
        pm.put("money",account.getMoney());
        jdbcTemplate.update(sql,pm);
    }

    public void delete(Integer id) {

    }

    public void update(Account account) {

    }

    public String findNameById(Integer id) {
        return null;
    }

    public Account findById(Integer id) {
        return null;
    }

    public List<Account> findAll() {
        return null;
    }

    public List<Account> findAll(int pageNum, int preNum) {
        return null;
    }

    public Long getCount() {
        return null;
    }
}

