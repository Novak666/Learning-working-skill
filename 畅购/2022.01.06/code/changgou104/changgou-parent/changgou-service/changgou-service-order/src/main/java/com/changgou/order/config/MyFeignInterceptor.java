package com.changgou.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/21 15:57
 **/
@Component
public class MyFeignInterceptor implements RequestInterceptor {
    //该方法会在feign进行调用，发送请求之前被执行 请求头中设置令牌过去就可以将令牌携带过去
    @Override
    public void apply(RequestTemplate template) {
        //1.获取请求对象(springmvc提供给了一个线程副本获取到当前请求对象)
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes!=null){
            HttpServletRequest request = requestAttributes.getRequest();

            Enumeration<String> headerNames = request.getHeaderNames();
            while(headerNames.hasMoreElements()){
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                //2.获取请求头 设置头信息 Authorization=bearer xlajsdlfajfl.llajl.ljljlj 到下游
                template.header(headerName,headerValue);
            }

        }

    }
}
