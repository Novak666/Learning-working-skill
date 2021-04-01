package com.itheima.controller;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public boolean save(User user){
        return userService.save(user);
    }

    @PutMapping
    public boolean update(User user){
        return userService.update(user);
    }

    @DeleteMapping("/{uuid}")
    public boolean delete(@PathVariable Integer uuid){
        return userService.delete(uuid);
    }

    @GetMapping("/{uuid}")
    public User get(@PathVariable Integer uuid){
        return userService.get(uuid);
    }

    @GetMapping("/{page}/{size}")
    public PageInfo<User> getAll(@PathVariable Integer page, @PathVariable Integer size){
        return userService.getAll(page,size);
    }

    @PostMapping("/login")
    public User login(String userName,String password){
        return userService.login(userName,password);
    }

}
