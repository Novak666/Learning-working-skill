package com.itheima.filter;

import javax.servlet.*;
import java.io.IOException;

/*
    过滤器的拦截行为
 */
//@WebFilter("/*")
public class FilterDemo05 implements Filter{
    /*
        提供服务方法
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filterDemo05执行了...");

        //处理乱码
        servletResponse.setContentType("text/html;charset=UTF-8");

        //放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

}
