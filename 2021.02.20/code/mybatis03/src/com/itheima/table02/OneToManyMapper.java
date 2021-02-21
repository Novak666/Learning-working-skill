package com.itheima.table02;

import com.itheima.bean.Classes;

import java.util.List;

public interface OneToManyMapper {
    //查询全部
    public abstract List<Classes> selectAll();
}
