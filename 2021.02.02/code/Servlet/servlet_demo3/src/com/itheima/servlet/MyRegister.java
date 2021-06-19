package com.itheima.servlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Set;
/*
    注册配置Servlet的功能类
 */
public class MyRegister implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) {
        //1.创建Servlet对象
        ServletDemo2 servletDemo2 = new ServletDemo2();

        //2.在ServletContext中添加Servlet，并得到Servlet的配置对象
        ServletRegistration.Dynamic registration = servletContext.addServlet("servletDemo2", servletDemo2);

        //3.配置Servlet
        registration.setLoadOnStartup(0);   //Servlet加载时机
        registration.addMapping("/servletDemo2");   //映射访问资源路径
    }
}
