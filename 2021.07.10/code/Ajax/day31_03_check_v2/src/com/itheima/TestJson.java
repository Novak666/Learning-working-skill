package com.itheima;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.pojo.ResultBean;
import com.itheima.pojo.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名:com.itheima
 * @author Leevi
 * 日期2020-10-22  10:59
 * 测试json转换
 * 为什么要将java对象转成json:因为我们要将java对象中的数据通过http协议传给各种客户端,而json是一种所有语言都能解析的数据格式
 * 在Java代码中怎么将Java对象转成json: 使用框架
 * 1. json-lib: 很古老的很重量级的框架，一般不用
 * 2. jackson: spring默认支持的json解析框架
 * 3. fastjson: 阿里开发的速度飞快的json解析框架
 * 4. gson: 谷歌开发一款使用特别简单的json解析框架
 *
 * 一: 使用jackson进行json和java对象的转换
 *    目标: 将Java对象转成json字符串
 */
public class TestJson {
    @Test
    public void test01() throws JsonProcessingException {
        User user = new User(1, "张三丰", "123456", "18999999999@163.com", "18999999999");

        //使用jackson将user对象转换成json字符串
        String jsonStr = new ObjectMapper().writeValueAsString(user);

        System.out.println(jsonStr);
    }

    @Test
    public void test02() throws JsonProcessingException {
        User user = new User(1, "张三丰", "123456", "18999999999@163.com", "18999999999");

        ResultBean resultBean = new ResultBean(true, user);

        //将resultBean对象转成json字符串
        String jsonStr = new ObjectMapper().writeValueAsString(resultBean);
        System.out.println(jsonStr);
    }

    @Test
    public void test03() throws JsonProcessingException {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "张三丰", "123456", "18999999999@163.com", "18999999999"));
        userList.add(new User(2, "周杰棍", "654321", "19898989898@163.com", "19898989898"));
        userList.add(new User(3, "奥巴马", "666666", "666666@163.com", "19866666666"));

        String jsonStr = new ObjectMapper().writeValueAsString(userList);
        System.out.println(jsonStr);
    }

    @Test
    public void test04() throws JsonProcessingException {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "张三丰", "123456", "18999999999@163.com", "18999999999"));
        userList.add(new User(2, "周杰棍", "654321", "19898989898@163.com", "19898989898"));
        userList.add(new User(3, "奥巴马", "666666", "666666@163.com", "19866666666"));


        ResultBean resultBean = new ResultBean(true, userList);

        String jsonStr = new ObjectMapper().writeValueAsString(resultBean);
        System.out.println(jsonStr);
    }

    @Test
    public void test05(){
        //使用fastjson将java对象转json字符串
        User user = new User(1, "张三丰", "123456", "18999999999@163.com", "18999999999");

        //使用fastJson将user对象转成json字符串
        String jsonStr = JSON.toJSONString(user);
        System.out.println(jsonStr);
    }
}
