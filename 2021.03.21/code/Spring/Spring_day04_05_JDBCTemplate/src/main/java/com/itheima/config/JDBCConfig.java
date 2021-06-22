package com.itheima.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public class JDBCConfig {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    @Bean("dataSource")
    public DataSource getDataSource(){
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }

    //注册JdbcTemplate模块对象bean
    @Bean("jdbcTemplate")
    public JdbcTemplate getJdbcTemplate(@Autowired DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean("jdbcTemplate2")
    public NamedParameterJdbcTemplate getJdbcTemplate2(@Autowired DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }


}
