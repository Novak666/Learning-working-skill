package com.itheima.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取表单中的数据
        String username = req.getParameter("username");
        String age = req.getParameter("age");
        String score = req.getParameter("score");

        //将数据保存到stu.txt文件中
        BufferedWriter bw = new BufferedWriter(new FileWriter("d:\\stu.txt",true));
        bw.write(username + "," + age + "," + score);
        bw.newLine();
        bw.close();

        //给浏览器回应
        PrintWriter pw = resp.getWriter();
        pw.println("Save Success~");
        pw.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
