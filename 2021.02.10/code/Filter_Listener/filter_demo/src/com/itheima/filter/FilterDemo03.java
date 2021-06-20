package com.itheima.filter;

import javax.servlet.*;
import java.io.IOException;

/*
    过滤器生命周期
 */
//@WebFilter("/*")
public class FilterDemo03 implements Filter{

    /*
        初始化方法
     */
    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("对象初始化成功了...");
    }

    /*
        提供服务方法
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filterDemo03执行了...");

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
