package com.atguigu.boot.controller;

import com.atguigu.boot.bean.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String handle01(){
        log.info("请求进来了....");
        return "Hello, Spring Boot 2!";
    }

    @Autowired
    Car car;

    @RequestMapping("/car")
    public Car car(){
        return car;
    }
}
