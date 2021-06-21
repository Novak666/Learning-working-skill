package com.itheima.dao.impl;

import com.itheima.dao.BookDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

public class BookDaoImpl implements BookDao {

    private int num;

    public void setNum(int num) {
        this.num = num;
    }

    public void save() {
        System.out.println("book dao running..."+"   "+num);
    }
}
