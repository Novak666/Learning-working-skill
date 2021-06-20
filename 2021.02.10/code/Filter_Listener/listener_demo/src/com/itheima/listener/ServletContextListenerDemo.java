package com.itheima.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*
    ServletContext对象的创建和销毁的监听器
 */
//@WebListener
public class ServletContextListenerDemo implements ServletContextListener{
    /*
        ServletContext对象创建的时候执行此方法
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("监听到了对象的创建...");

        //获取对象
        ServletContext servletContext = sce.getServletContext();
        //System.out.println(servletContext);

        //添加属性
        servletContext.setAttribute("username","zhangsan");

        //替换属性
        servletContext.setAttribute("username","lisi");

        //移除属性
        servletContext.removeAttribute("username");
    }

    /*
        ServletContext对象销毁的时候执行此方法
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("监听到了对象的销毁...");
    }
}
