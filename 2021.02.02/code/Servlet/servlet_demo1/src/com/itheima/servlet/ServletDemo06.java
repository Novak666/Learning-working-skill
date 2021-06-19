package com.itheima.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
    Servlet 多路径映射
 */
public class ServletDemo06 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int money = 1000;

        //获取访问的资源路径
        String name = req.getRequestURI();
        name = name.substring(name.lastIndexOf("/"));

        if("/vip".equals(name)) {
            //如果访问资源路径是/vip 商品价格为9折
            System.out.println("商品原价为：" + money + "。优惠后是：" + (money*0.9));
        } else if("/vvip".equals(name)) {
            //如果访问资源路径是/vvip 商品价格为5折
            System.out.println("商品原价为：" + money + "。优惠后是：" + (money*0.5));
        } else {
            //如果访问资源路径是其他  商品价格原样显示
            System.out.println("商品价格为：" + money);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
