package com.itheima.filter;

import javax.servlet.*;
import java.io.IOException;

/*
    过滤器配置对象的使用
 */
//@WebFilter("/*")
public class FilterDemo04 implements Filter{

    /*
        初始化方法
     */
    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("对象初始化成功了...");

        //获取过滤器名称
        String filterName = filterConfig.getFilterName();
        System.out.println(filterName);

        //根据name获取value
        String username = filterConfig.getInitParameter("username");
        System.out.println(username);
    }

    /*
        提供服务方法
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filterDemo04执行了...");

        //处理乱码
        servletResponse.setContentType("text/html;charset=UTF-8");

        //放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

    /*
        对象销毁
     */
    @Override
    public void destroy() {
        System.out.println("对象销毁了...");
    }
}
