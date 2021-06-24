package com.itheima.controller;

import com.itheima.domain.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping
    public boolean save(User user){
        System.out.println("save ..." + user);
        return true;
    }

    @PutMapping
    public boolean update(User user){
        System.out.println("update ..." + user);
        return true;
    }

    @DeleteMapping("/{uuid}")
    public boolean delete(@PathVariable Integer uuid){
        System.out.println("delete ..." + uuid);
        return true;
    }

    @GetMapping("/{uuid}")
    public User get(@PathVariable Integer uuid){
        System.out.println("get ..." + uuid);
        return null;
    }

    @GetMapping("/{page}/{size}")
    public List getAll(@PathVariable Integer page,@PathVariable Integer size){
        System.out.println("getAll ..." + page+","+size);
        return null;
    }

    @PostMapping("/login")
    public User login(String userName,String password){
        System.out.println("login ..." + userName + " ," +password);
        return null;
    }

}
