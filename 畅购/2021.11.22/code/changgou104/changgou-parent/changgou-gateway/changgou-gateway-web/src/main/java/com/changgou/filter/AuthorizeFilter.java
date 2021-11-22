package com.changgou.filter;

import com.changgou.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/12 15:21
 **/
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZE_TOKEN = "Authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.先获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        //2.再获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        //3.判断如果是登录的路径 放行
        String path = request.getURI().getPath();
        if(path.startsWith("/api/user/login")){
            return  chain.filter(exchange);
        }
        //4.先从请求参数中获取令牌 如果没有获取到
        String token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        if(StringUtils.isEmpty(token)){
            //5.再从请求头中获取令牌  如果没有获取到
            token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        }
        if(StringUtils.isEmpty(token)){
            //6.再从cookie中获取令牌 如果没有获取到  拦截 返回 401
            HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if(cookie!=null){
                token = cookie.getValue();//令牌的数据
            }
        }
        if(StringUtils.isEmpty(token)){//结束
            //重定向到登录的页面  1.设置重定向到的地址 2.设置http状态码的行为 303 302
            response.getHeaders().set("Location","http://localhost:9001/oauth/login?url="+request.getURI().toString());//url
            response.setStatusCode(HttpStatus.SEE_OTHER);
            return response.setComplete();
        }
        //7.如果能获取到 还要解析令牌 解析不成功  拦截 返回 401
//        try {
//            JwtUtil.parseJWT(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }

        //将网关收到的cookie中的token数据 传递给下一个微服务
        request.mutate().header(AUTHORIZE_TOKEN,"bearer "+token);

        //8.如果能解析出来 放行
        return  chain.filter(exchange);
    }

    @Override
    //数据值越低 过滤器的执行的优先级 越高
    public int getOrder() {
        return 0;
    }
}
