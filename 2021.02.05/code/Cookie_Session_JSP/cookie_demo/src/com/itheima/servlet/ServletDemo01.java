package com.itheima.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    Cookie的使用
 */
@WebServlet("/servletDemo01")
public class ServletDemo01 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.通过响应对象写出提示信息
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter pw = resp.getWriter();
        pw.write("欢迎访问本网站，您的最后访问时间为：<br>");

        //2.创建Cookie对象，用于记录最后访问时间
        Cookie cookie = new Cookie("time",System.currentTimeMillis()+"");

        //3.设置最大存活时间
        cookie.setMaxAge(3600);
//        cookie.setMaxAge(0);    // 立即清除

        //4.将cookie对象添加到客户端
        resp.addCookie(cookie);

        //5.获取cookie
        Cookie[] arr = req.getCookies();
        for(Cookie c : arr) {
            if("time".equals(c.getName())) {
                //6.获取cookie对象中的value，进行写出
                String value = c.getValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                pw.write(sdf.format(new Date(Long.parseLong(value))));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
