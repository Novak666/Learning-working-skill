package com.itheima.mapper;

import com.itheima.bean.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StudentMapper {
    //查询全部
    @Select("SELECT * FROM student")
    public abstract List<Student> selectAll();

    //新增操作
    @Insert("INSERT INTO student VALUES (#{id},#{name},#{age})")
    public abstract Integer insert(Student stu);

    //修改操作
    @Update("UPDATE student SET name=#{name},age=#{age} WHERE id=#{id}")
    public abstract Integer update(Student stu);

    //删除操作
    @Delete("DELETE FROM student WHERE id=#{id}")
    public abstract Integer delete(Integer id);
}
