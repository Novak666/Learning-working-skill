package com.itheima.controller;

import com.itheima.domain.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BookController {
    //使用原生request对象传递参数
    @RequestMapping("/showPageAndData1")
    public String showPageAndData1(HttpServletRequest request) {
        request.setAttribute("name","itheima");
        return "page";
    }
    //使用Model形参传递参数
    @RequestMapping("/showPageAndData2")
    public String showPageAndData2(Model model) {
        //添加数据的方式，key对value
        model.addAttribute("name","Jock");
        Book book  = new Book();
        book.setName("SpringMVC入门案例");
        book.setPrice(66.66d);
        //添加数据的方式，key对value
        model.addAttribute("book",book);
        return "page";
    }

    //使用ModelAndView形参传递参数，该对象还封装了页面信息
    @RequestMapping("/showPageAndData3")
    public ModelAndView showPageAndData3(ModelAndView modelAndView) {
        //ModelAndView mav = new ModelAndView();    替换形参中的参数
        Book book  = new Book();
        book.setName("SpringMVC入门案例");
        book.setPrice(66.66d);

        //添加数据的方式，key对value
        modelAndView.addObject("book",book);
        //添加数据的方式，key对value
        modelAndView.addObject("name","Jockme");
        //设置页面的方式，该方法最后一次执行的结果生效
        modelAndView.setViewName("page");
        //返回值设定成ModelAndView对象
        return modelAndView;
    }

    //ModelAndView对象支持转发的手工设定，该设定不会启用前缀后缀的页面拼接格式
    @RequestMapping("/showPageAndData4")
    public ModelAndView showPageAndData4(ModelAndView modelAndView) {
        modelAndView.setViewName("forward:/WEB-INF/page/page.jsp");
        return modelAndView;
    }

    //ModelAndView对象支持重定向的手工设定，该设定不会启用前缀后缀的页面拼接格式
    @RequestMapping("/showPageAndData5")
    public ModelAndView showPageAndData6(ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:page.jsp");
        return modelAndView;
    }
}
