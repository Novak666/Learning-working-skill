package com.itheima;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;

/**
 * 包名:com.itheima
 * @author Leevi
 * 日期2020-10-22  14:52
 * 使用fastJson将json字符串转换成Java对象
 */
public class TestFastJson {
    @Test
    public void test01(){
        String jsonStr = "{\"id\":1,\"username\":\"zs\",\"password\":\"123456\",\"email\":\"zs@163.com\",\"phone\":\"1386789898\"}";

        User user = JSON.parseObject(jsonStr, User.class);

        System.out.println(user);
    }

    @Test
    public void test02(){
        String jsonArr = "[{\"id\":1,\"username\":\"奥巴马\",\"password\":\"123456\",\"email\":\"123456@qq.com\",\"phone\":\"18999999999\"},{\"id\":2,\"username\":\"周杰棍\",\"password\":\"654321\",\"email\":\"654321@qq.com\",\"phone\":\"18666666666\"},{\"id\":3,\"username\":\"王丽红\",\"password\":\"777777\",\"email\":\"777777@qq.com\",\"phone\":\"18777777777\"}]";

        //将jsonArr转换成List<User>
        List<User> userList = JSON.parseArray(jsonArr, User.class);
        for (User user : userList) {
            System.out.println(user);
        }
    }
}
