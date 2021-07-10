package com.itheima;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * 包名:com.itheima
 * @author Leevi
 * 日期2020-10-22  14:33
 * 使用jackson将json字符串转换成Java对象
 */
public class TestJackson {
    @Test
    public void test01() throws IOException {
        String jsonStr = "{\"id\":1,\"username\":\"zs\",\"password\":\"123456\",\"email\":\"zs@163.com\",\"phone\":\"1386789898\"}";

        //目标: 将jsonStr转换成User对象
        User user = new ObjectMapper().readValue(jsonStr, User.class);

        System.out.println(user.getUsername());
    }

    @Test
    public void test02() throws IOException {
        //将json字符串转换成List<Bean>
        String jsonStr = "[{\"id\":1,\"username\":\"zs\",\"password\":\"123456\",\"email\":\"zs@163.com\",\"phone\":\"1386789898\"},{\"id\":2,\"username\":\"ls\",\"password\":\"123456\",\"email\":\"ls@163.com\",\"phone\":\"1386781898\"},{\"id\":3,\"username\":\"ww\",\"password\":\"123456\",\"email\":\"ww@163.com\",\"phone\":\"1386782898\"}]";
        TypeReference ref = new TypeReference<List<User>>() {};

        List<User> userList = new ObjectMapper().readValue(jsonStr, ref);

        for (User user : userList) {
            System.out.println(user);
        }
    }


}
