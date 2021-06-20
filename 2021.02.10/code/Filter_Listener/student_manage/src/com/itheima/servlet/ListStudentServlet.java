package com.itheima.servlet;

import com.itheima.bean.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
    实现查看功能
 */
@WebServlet("/listStudentServlet")
public class ListStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.创建字符输入流对象，关联读取的文件
        BufferedReader br = new BufferedReader(new FileReader("d:\\stu.txt"));

        //2.创建集合对象，用于保存Student对象
        ArrayList<Student> list = new ArrayList<>();

        //3.循环读取文件中的数据，将数据封装到Student对象中。再把多个学生对象添加到集合中
        String line;
        while((line = br.readLine()) != null) {
            //张三,23,95
            Student stu = new Student();
            String[] arr = line.split(",");
            stu.setUsername(arr[0]);
            stu.setAge(Integer.parseInt(arr[1]));
            stu.setScore(Integer.parseInt(arr[2]));
            list.add(stu);
        }

        //4.将集合对象存入会话域中
        req.getSession().setAttribute("students",list);

        //5.重定向到学生列表页面
        resp.sendRedirect(req.getContextPath() + "/listStudent.jsp");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
