package com.itheima.service.impl;

import com.itheima.bean.Student;
import com.itheima.mapper.StudentMapper;
import com.itheima.mapper.impl.StudentMapperImpl;
import com.itheima.service.StudentService;

import java.util.List;
/*
    业务层实现类
 */
public class StudentServiceImpl implements StudentService {

    //创建持久层对象
    private StudentMapper mapper = new StudentMapperImpl();

    @Override
    public List<Student> selectAll() {
        return mapper.selectAll();
    }

    @Override
    public Student selectById(Integer id) {
        return mapper.selectById(id);
    }

    @Override
    public Integer insert(Student stu) {
        return mapper.insert(stu);
    }

    @Override
    public Integer update(Student stu) {
        return mapper.update(stu);
    }

    @Override
    public Integer delete(Integer id) {
        return mapper.delete(id);
    }
}
