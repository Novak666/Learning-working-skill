package com.itheima.dao.impl;

import com.itheima.dao.BookDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//@Primary
public class BookDaoImpl2 implements BookDao {

    public void save() {
        System.out.println("book dao running...2");
    }
}
