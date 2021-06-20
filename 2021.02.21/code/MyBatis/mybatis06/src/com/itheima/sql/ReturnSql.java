package com.itheima.sql;

import com.itheima.bean.Student;
import org.apache.ibatis.jdbc.SQL;

public class ReturnSql {
    //定义方法，返回查询的sql语句
    public String getSelectAll() {
        return new SQL() {
            {
                SELECT("*");
                FROM("student");
            }
        }.toString();
    }

    //定义方法，返回新增的sql语句
    public String getInsert(Student stu) {
        return new SQL() {
            {
                INSERT_INTO("student");
                INTO_VALUES("#{id},#{name},#{age}");
            }
        }.toString();
    }

    //定义方法，返回修改的sql语句
    public String getUpdate(Student stu) {
        return new SQL() {
            {
                UPDATE("student");
                SET("name=#{name}","age=#{age}");
                WHERE("id=#{id}");
            }
        }.toString();
    }

    //定义方法，返回删除的sql语句
    public String getDelete(Integer id) {
        return new SQL() {
            {
                DELETE_FROM("student");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
