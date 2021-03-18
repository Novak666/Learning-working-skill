package com.itheima.dao.impl;

import com.itheima.dao.BookDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Primary
@PropertySource(value={"classpath:jdbc.properties","classpath:abc.properties"},ignoreResourceNotFound = true)
public class BookDaoImpl implements BookDao {
    @Value("${jdbc.userName}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;


    public void save() {
        System.out.println("book dao running...1 "+userName+" "+password);
    }
}
