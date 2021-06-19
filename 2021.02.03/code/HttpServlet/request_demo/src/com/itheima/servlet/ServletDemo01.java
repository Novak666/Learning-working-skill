package com.itheima.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
    获取路径的相关方法
 */
@WebServlet("/servletDemo01")
public class ServletDemo01 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取虚拟目录名称 getContextPath()
        String contextPath = req.getContextPath();
        System.out.println("1:" + contextPath);

        //2.获取Servlet映射路径 getServletPath()
        String servletPath = req.getServletPath();
        System.out.println("2:" + servletPath);

        //3.获取访问者ip getRemoteAddr()
        String ip = req.getRemoteAddr();
        System.out.println("3:" + ip);

        //4.获取请求消息的数据 getQueryString()
        String queryString = req.getQueryString();
        System.out.println("4:" + queryString);

        //5.获取统一资源标识符 getRequestURI()    /request/servletDemo01   共和国
        String requestURI = req.getRequestURI();
        System.out.println("5:" + requestURI);

        //6.获取统一资源定位符 getRequestURL()    http://localhost:8080/request/servletDemo01  中华人民共和国
        StringBuffer requestURL = req.getRequestURL();
        System.out.println("6:" + requestURL);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
