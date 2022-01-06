package com.itheima.controller;

import com.itheima.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/05 11:18
 **/
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    public String hello(Model model){
        //设置简单的数据类型
        model.addAttribute("hello","hello welcome");
        //设置复杂类型（POJO类型）需要在页面显示
        //设置复杂类型（POJO类型）需要在页面显示
        User user = new User();
        user.setName("zhangsan");
        user.setId(1);
        user.setAddress("深圳");
        model.addAttribute("user",user);
        //设置 List数据类型
        List<User> users = new ArrayList<User>();
        users.add(new User(1,"zhangsan","深圳"));
        users.add(new User(2,"李四","北京"));
        users.add(new User(3,"王五","武汉"));
        model.addAttribute("users",users);

        model.addAttribute("url","/test/hello");
        model.addAttribute("key1","key1");
        model.addAttribute("key2","key2");

        //循环遍历Map
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("No","123");
        dataMap.put("address","深圳");
        model.addAttribute("dataMap",dataMap);

        //输出日期
        model.addAttribute("date",new Date());
        //后台设置一个年龄值
        model.addAttribute("age",19);

        return "demo1";
    }

}
