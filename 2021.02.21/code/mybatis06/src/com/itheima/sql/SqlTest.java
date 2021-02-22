package com.itheima.sql;

import org.apache.ibatis.jdbc.SQL;

public class SqlTest {
    public static void main(String[] args) {
        String sql = getSql();
        System.out.println(sql);
    }

    //定义方法，获取查询student表的sql语句
    /*public static String getSql() {
        String sql = "SELECT * FROM student";
        return sql;
    }*/

    public static String getSql() {
        String sql = new SQL(){
            {
                SELECT("*");
                FROM("student");
            }
        }.toString();

        return sql;
    }
}
