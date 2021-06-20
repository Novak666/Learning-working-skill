package com.itheima.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

/*
    应用域对象中的属性变化的监听器
 */
//@WebListener
public class ServletContextAttributeListenerDemo implements ServletContextAttributeListener{
    /*
        向应用域对象中添加属性时执行此方法
     */
    @Override
    public void attributeAdded(ServletContextAttributeEvent scae) {
        System.out.println("监听到了属性的添加...");

        //获取应用域对象
        ServletContext servletContext = scae.getServletContext();
        //获取属性
        Object value = servletContext.getAttribute("username");
        System.out.println(value);
    }

    /*
        向应用域对象中替换属性时执行此方法
     */
    @Override
    public void attributeReplaced(ServletContextAttributeEvent scae) {
        System.out.println("监听到了属性的替换...");

        //获取应用域对象
        ServletContext servletContext = scae.getServletContext();
        //获取属性
        Object value = servletContext.getAttribute("username");
        System.out.println(value);
    }

    /*
        向应用域对象中移除属性时执行此方法
     */
    @Override
    public void attributeRemoved(ServletContextAttributeEvent scae) {
        System.out.println("监听到了属性的移除...");

        //获取应用域对象
        ServletContext servletContext = scae.getServletContext();
        //获取属性
        Object value = servletContext.getAttribute("username");
        System.out.println(value);
    }
}
