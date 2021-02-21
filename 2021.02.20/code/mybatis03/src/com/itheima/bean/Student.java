package com.itheima.bean;

import java.util.List;

public class Student {
    private Integer id;     //主键id
    private String name;    //学生姓名
    private Integer age;    //学生年龄

    private List<Course> courses;   // 学生所选择的课程集合

    public Student() {
    }

    public Student(Integer id, String name, Integer age, List<Course> courses) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
