package com.itheima.table01;

import com.itheima.bean.Card;

import java.util.List;

public interface OneToOneMapper {
    //查询全部
    public abstract List<Card> selectAll();
}
