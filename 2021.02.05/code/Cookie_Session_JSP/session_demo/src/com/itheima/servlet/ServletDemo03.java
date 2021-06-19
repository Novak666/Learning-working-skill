package com.itheima.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
    Cookie的禁用
 */
@WebServlet("/servletDemo03")
public class ServletDemo03 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取HttpSession对象
        HttpSession session = req.getSession(false);
        System.out.println(session);
        if(session == null) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("为了不影响正常的使用，请不要禁用浏览器的Cookie~");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
