package com.itheima.servlet;

import com.itheima.bean.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/*
    封装对象-反射方式
 */
@WebServlet("/servletDemo05")
public class ServletDemo05 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取所有的数据
        Map<String, String[]> map = req.getParameterMap();

        //2.封装学生对象
        Student stu = new Student();
        //2.1遍历集合
        for(String name : map.keySet()) {
            String[] value = map.get(name);
            try {
                //2.2获取Student对象的属性描述器
                PropertyDescriptor pd = new PropertyDescriptor(name,stu.getClass());
                //2.3获取对应的setXxx方法
                Method writeMethod = pd.getWriteMethod();
                //2.4执行方法
                if(value.length > 1) {
                    writeMethod.invoke(stu,(Object)value);
                }else {
                    writeMethod.invoke(stu,value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //3.输出对象
        System.out.println(stu);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
