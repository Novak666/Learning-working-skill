package com.itheima.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    解决全局乱码问题
 */
@WebFilter("/*")
public class EncodingFilter implements Filter{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try{
            //1.将请求和响应对象转换为和HTTP协议相关
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            //2.设置编码格式
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");

            //3.放行
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
