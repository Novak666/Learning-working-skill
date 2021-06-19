package com.itheima.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
    Cookie的路径限制
    取自第一次访问的资源路径前缀
    只要以这个前缀为开头(包括子级路径)。获取到
    反之获取不到
 */
@WebServlet("/servlet/servletDemo02")
public class ServletDemo02 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建Cookie并添加
        Cookie cookie = new Cookie("username","zhangsan");
        cookie.setMaxAge(3600);
        resp.addCookie(cookie);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
