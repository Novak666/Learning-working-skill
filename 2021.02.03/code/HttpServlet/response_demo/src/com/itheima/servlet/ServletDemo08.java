package com.itheima.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/*
    文件下载
 */
@WebServlet("/servletDemo08")
public class ServletDemo08 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.创建字节输入流，关联读取的文件
        //获取文件的绝对路径
        String realPath = getServletContext().getRealPath("/img/hm.png");
        //创建字节输出流对象
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(realPath));

        //2.设置响应头支持的类型  应用支持的类型为字节流
        /*
            Content-Type 消息头名称   支持的类型
            application/octet-stream   消息头参数  应用类型为字节流
         */
        resp.setHeader("Content-Type","application/octet-stream");

        //3.设置响应头以下载方式打开  以附件形式处理内容
        /*
            Content-Disposition  消息头名称  处理的形式
            attachment;filename=  消息头参数  附件形式进行处理
         */
        resp.setHeader("Content-Disposition","attachment;filename=" + System.currentTimeMillis() + ".png");

        //4.获取字节输出流对象
        ServletOutputStream sos = resp.getOutputStream();

        //5.循环读写文件
        byte[] arr = new byte[1024];
        int len;
        while((len = bis.read(arr)) != -1) {
            sos.write(arr,0,len);
        }

        //6.释放资源
        bis.close();
        sos.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
