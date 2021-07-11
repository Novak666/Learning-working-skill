package com.itheima.servlet;

import com.alibaba.fastjson.JSON;
import com.itheima.pojo.User;
import com.itheima.utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Leevi
 * 日期2020-10-23  11:41
 * request.getParameter()方法只能获取到"name=value&name=value"这种类型的请求参数
 * 但是该方法无法获取请求体中的json类型的请求参数,就得使用json解析的jar包将json类型的请求参数转换成java对象
 */
@WebServlet("/demo01")
public class ServletDemo01 extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求参数
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        String nickname = request.getParameter("nickname");
//
//        //向客户端响应数据
//        User user = new User(username, password, nickname);

        User user = JSON.parseObject(request.getInputStream(), User.class);
//
        System.out.println(user);
        //int num = 10/0;

//        //将user转换成json字符串，响应给客户端
//        String jsonStr = JSON.toJSONString(user);
//        response.getWriter().write(jsonStr);

        response.setContentType("application/json;charset=utf-8");
        JSON.writeJSONString(response.getWriter(),user);
    }
}
