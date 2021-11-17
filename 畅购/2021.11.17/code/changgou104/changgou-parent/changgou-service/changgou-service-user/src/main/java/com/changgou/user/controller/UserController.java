package com.changgou.user.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.core.AbstractCoreController;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import entity.BCrypt;
import entity.JwtUtil;
import entity.Result;
import entity.StatusCode;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController extends AbstractCoreController<User>{

    private UserService  userService;

    @Autowired
    public UserController(UserService  userService) {
        super(userService, User.class);
        this.userService = userService;
    }

    /**
     * 用户登录(将来不用)
     *
     * @param username
     * @param password 从页面传递过来 目前没有加密 明文
     * @return
     */
    @RequestMapping("/login")
    public Result login(String username, String password, HttpServletResponse response) {
        //1.判断是否为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return new Result(false, StatusCode.LOGINERROR, "用户名和密码不能为空");
        }
        //2.判断是否数据库中有该用户名对应的数据
        User user = userService.selectByPrimaryKey(username);
        if (user == null) {
            return new Result(false, StatusCode.LOGINERROR, "用户名或者密码错误");
        }
        //3.判断是否数据的密码和传递的密码一致
        if (!BCrypt.checkpw(password, user.getPassword())) {
            return new Result(false, StatusCode.LOGINERROR, "用户名或者密码错误");
        }

        //4.先生成一个令牌   返回给用户 放在“兜里” 存储在cookie中
        Map<String,Object> info = new HashMap<String,Object>();
        info.put("username",username);
        info.put("role","ROLE_ADMIN");//security


        String token = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(info), null);

        //放到兜里（cookie 中）
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new Result(true, StatusCode.OK, "登录成功");
    }

    public static void main(String[] args) throws Exception {
        //bcrypt
        String encode = new BCryptPasswordEncoder().encode("123456");
        System.out.println(encode);

        //
        byte[] bytes = Base64.getDecoder().decode("eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9");
        System.out.println(new String(bytes,"utf-8"));
    }
}
