package com.itheima.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/*
    ServletConfig的使用
 */
public class ServletConfigDemo extends HttpServlet {

    //声明ServletConfig配置对象
    private ServletConfig config;

    /*
        通过init方法来为ServletConfig配置对象赋值
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //根据key获取value
        String encodingValue = config.getInitParameter("encoding");
        System.out.println(encodingValue);

        //获取Servlet的名称
        String servletName = config.getServletName();
        System.out.println(servletName);

        //获取所有的key
        Enumeration<String> names = config.getInitParameterNames();
        //遍历得到的key
        while(names.hasMoreElements()) {
            //获取每一个key
            String name = names.nextElement();
            //通过key获取value
            String value = config.getInitParameter(name);
            System.out.println("name:" + name + ",value:" + value);
        }

        //获取ServletContext对象
        ServletContext context = config.getServletContext();
        System.out.println(context);

        //获取ServletContextDemo设置共享的数据
        Object username = context.getAttribute("username");
        System.out.println(username);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
