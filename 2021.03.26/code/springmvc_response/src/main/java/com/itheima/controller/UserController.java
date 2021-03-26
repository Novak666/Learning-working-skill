package com.itheima.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    //测试服务器是否正常工作使用
    @RequestMapping("/showPage")
    public String showPage() {
        System.out.println("user mvc controller is running ...");
        return "page.jsp";
    }

    //forward:page.jsp转发访问，支持访问WEB-INF下的页面
    @RequestMapping("/showPage1")
    public String showPage1() {
        System.out.println("user mvc controller is running ...");
        return "forward:/WEB-INF/page/page.jsp";
    }

    //redirect:page.jsp重定向访问，不支持访问WEB-INF下的页面
    @RequestMapping("/showPage2")
    public String showPage2() {
        System.out.println("user mvc controller is running ...");
        return "redirect:/WEB-INF/page/page.jsp";
    }

    //页面简化配置格式，使用前缀+页面名称+后缀的形式进行，类似于字符串拼接
    @RequestMapping("/showPage3")
    public String showPage3() {
        System.out.println("user mvc controller is running ...");
        return "page";
    }

    //页面简化配置格式不支持书写forward
    @RequestMapping("/showPage4")
    public String showPage4() {
        System.out.println("user mvc controller is running ...");
        return "forward:page";
    }

    //最简页面配置方式，使用访问路径作为页面名称，省略返回值
    @RequestMapping("/showPage5")
    public void showPage5() {
        System.out.println("user mvc controller is running ...");
    }
}
