package com.itheima.table03;

import com.itheima.bean.Student;

import java.util.List;

public interface ManyToManyMapper {
    //查询全部
    public abstract List<Student> selectAll();
}
