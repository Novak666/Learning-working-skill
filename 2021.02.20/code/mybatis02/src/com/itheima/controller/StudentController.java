package com.itheima.controller;

import com.itheima.bean.Student;
import com.itheima.service.StudentService;
import com.itheima.service.impl.StudentServiceImpl;
import org.junit.Test;

import java.util.List;

/*
    控制层测试类
 */
public class StudentController {
    //创建业务层对象
    private StudentService service = new StudentServiceImpl();

    //查询全部功能测试
    @Test
    public void selectAll() {
        List<Student> students = service.selectAll();
        for (Student stu : students) {
            System.out.println(stu);
        }
    }

    //根据id查询功能测试
    @Test
    public void selectById() {
        Student stu = service.selectById(3);
        System.out.println(stu);
    }

    //新增功能测试
    @Test
    public void insert() {
        Student stu = new Student(4,"赵六",26);
        Integer result = service.insert(stu);
        System.out.println(result);
    }

    //修改功能测试
    @Test
    public void update() {
        Student stu = new Student(4,"赵六",16);
        Integer result = service.update(stu);
        System.out.println(result);
    }

    //删除功能测试
    @Test
    public void delete() {
        Integer result = service.delete(4);
        System.out.println(result);
    }
}
