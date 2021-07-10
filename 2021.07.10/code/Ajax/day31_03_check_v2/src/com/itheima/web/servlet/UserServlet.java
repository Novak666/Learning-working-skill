package com.itheima.web.servlet;

import com.alibaba.fastjson.JSON;
import com.itheima.pojo.ResultBean;
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
    public void checkUsername(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //resultBean 对象，是用于封装响应数据，flag为true表示服务器没有异常
        ResultBean resultBean = new ResultBean(true);
        try {
            //1. 获取请求参数
            String username = request.getParameter("username");
            //2. 调用业务层的方法，根据username查找用户
            User user = userService.findByUsername(username);
            //3. 判断user是否为null
            if (user == null) {
                //用户名可用
                //flag为true表示服务器没有异常,data为true表示用户名可用
                resultBean.setData(true);
            }else {
                //用户名已被占用,data为false就表示用户名不可用
                resultBean.setData(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //flag为false表示服务器出现异常，errorMsg就是要展示在客户端的异常信息
            resultBean.setFlag(false);
            resultBean.setErrorMsg("服务器异常");
        }

        //将resultBean对象转换成json字符串，响应给客户端
        String jsonStr = JSON.toJSONString(resultBean);
        response.getWriter().write(jsonStr);
    }
}
