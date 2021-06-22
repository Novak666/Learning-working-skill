package com.itheima.dao.impl;

import com.itheima.dao.BookDao;
import org.springframework.stereotype.Component;

public class BookDaoImpl implements BookDao {

    public void save() {
        System.out.println("book dao running...");
    }

}
