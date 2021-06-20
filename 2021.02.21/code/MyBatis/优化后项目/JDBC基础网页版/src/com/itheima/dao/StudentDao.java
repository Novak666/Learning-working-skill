package com.itheima.dao;

import com.itheima.domain.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;

/*
    Dao层接口
 */
public interface StudentDao {
    //查询所有学生信息
    @Select("SELECT * FROM student")
    public abstract ArrayList<Student> findAll();

    //条件查询，根据id获取学生信息
    @Select("SELECT * FROM student WHERE sid=#{sid}")
    public abstract Student findById(Integer sid);

    //新增学生信息
    @Insert("INSERT INTO student VALUES (#{sid},#{name},#{age},#{birthday})")
    public abstract int insert(Student stu);

    //修改学生信息
    @Update("UPDATE student SET name=#{name},age=#{age},birthday=#{birthday} WHERE sid=#{sid}")
    public abstract int update(Student stu);

    //删除学生信息
    @Delete("DELETE FROM student WHERE sid=#{sid}")
    public abstract int delete(Integer sid);
}
