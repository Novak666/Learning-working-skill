package com.itheima.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
    Servlet线程安全
 */
public class ServletDemo04 extends HttpServlet{
    //1.定义用户名成员变量
    //private String username = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = null;
        //synchronized (this) {
            //2.获取用户名
            username = req.getParameter("username");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //3.获取输出流对象
            PrintWriter pw = resp.getWriter();

            //4.响应给客户端浏览器
            pw.print("welcome:" + username);

            //5.关流
            pw.close();
        //}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
