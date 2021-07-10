package com.itheima.web.servlet;

import com.itheima.pojo.User;
import com.itheima.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Leevi
 * 日期2020-10-22  10:12
 */
@WebServlet("/user")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserService();
    /**
     * 校验用户名
     * @param request
     * @param response
     */
    public void checkUsername(HttpServletRequest request, HttpServletResponse response){
        try {
            //1. 获取请求参数
            String username = request.getParameter("username");
            //2. 调用业务层的方法，根据username查找用户
            User user = userService.findByUsername(username);
            //3. 判断user是否为null
            if (user == null) {
                //用户名可用
                response.getWriter().write("用户名可用");
            }else {
                //用户名已被占用
                response.getWriter().write("用户名已被占用");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.getWriter().write("服务器异常");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
