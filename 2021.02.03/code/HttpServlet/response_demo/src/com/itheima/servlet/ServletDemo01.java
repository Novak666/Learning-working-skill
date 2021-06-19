package com.itheima.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
    字节流响应消息及乱码的解决
 */
@WebServlet("/servletDemo01")
public class ServletDemo01 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String str = "你好";

        ServletOutputStream sos = resp.getOutputStream();
        /*
            项目中常用的编码格式是u8，而浏览器默认使用的编码是gbk。导致乱码！
            解决方式一：修改浏览器的编码格式(不推荐，不能让用户做修改的动作)
            解决方式二：通过输出流写出一个标签：<meta http-equiv='content-type' content='text/html;charset=UTF-8'>
            解决方式三：response.setHeader("Content-Type","text/html;charset=UTF-8");  指定响应头信息
            解决方式四：response.setContentType("text/html;charset=UTF-8");
         */
        //sos.write("<meta http-equiv='content-type' content='text/html;charset=UTF-8'>".getBytes());

        //resp.setHeader("Content-Type","text/html;charset=UTF-8");

        resp.setContentType("text/html;charset=UTF-8");

        sos.write(str.getBytes("UTF-8"));
        sos.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
