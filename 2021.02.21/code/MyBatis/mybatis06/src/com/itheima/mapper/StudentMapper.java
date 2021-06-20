package com.itheima.mapper;

import com.itheima.bean.Student;
import com.itheima.sql.ReturnSql;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

public interface StudentMapper {
    //查询全部
    //@Select("SELECT * FROM student")
    @SelectProvider(type = ReturnSql.class , method = "getSelectAll")
    public abstract List<Student> selectAll();

    //新增功能
    //@Insert("INSERT INTO student VALUES (#{id},#{name},#{age})")
    @InsertProvider(type = ReturnSql.class , method = "getInsert")
    public abstract Integer insert(Student stu);

    //修改功能
    //@Update("UPDATE student SET name=#{name},age=#{age} WHERE id=#{id}")
    @UpdateProvider(type = ReturnSql.class , method = "getUpdate")
    public abstract Integer update(Student stu);

    //删除功能
    //@Delete("DELETE FROM student WHERE id=#{id}")
    @DeleteProvider(type = ReturnSql.class , method = "getDelete")
    public abstract Integer delete(Integer id);
}
