package com.itheima.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
//设定当前类中名称为age和gender的变量放入session范围，不常用，了解即可
@SessionAttributes(names = {"age","gender"})
public class UserController {

    //获取request,response,session对象的原生接口
    @RequestMapping("/servletApi")
    public String servletApi(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        System.out.println(request);
        System.out.println(response);
        System.out.println(session);
        return "page";
    }

    //获取head数据的快捷操作方式
    @RequestMapping("/headApi")
    public String headApi(@RequestHeader("Accept-Encoding") String headMsg){
        System.out.println(headMsg);
        return "page";
    }

    //获取cookie数据的快捷操作方式
    @RequestMapping("/cookieApi")
    public String cookieApi(@CookieValue("JSESSIONID") String jsessionid){
        System.out.println(jsessionid);
        return "page";
    }

    //测试用方法，为下面的试验服务，用于在session中放入数据
    @RequestMapping("/setSessionData")
    public String setSessionData(HttpSession session){
        session.setAttribute("name","itheima");
        return "page";
    }

    //获取session数据的快捷操作方式
    @RequestMapping("/sessionApi")
    public String sessionApi(@SessionAttribute("name") String name,
                             @SessionAttribute("age") int age,
                             @SessionAttribute("gender") String gender){
        System.out.println(name);
        System.out.println(age);
        System.out.println(gender);
        return "page";
    }

    //配合@SessionAttributes(names = {"age","gender"})使用
    //将数据放入session存储范围，通过Model对象实现数据set，通过@SessionAttributes注解实现范围设定
    @RequestMapping("/setSessionData2")
    public String setSessionDate2(Model model) {
        model.addAttribute("age",39);
        model.addAttribute("gender","男");
        return "page";
    }

}
