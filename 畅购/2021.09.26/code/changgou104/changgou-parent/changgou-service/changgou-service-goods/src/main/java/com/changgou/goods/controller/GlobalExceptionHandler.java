package com.changgou.goods.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/09/26
 **/
@ControllerAdvice//标识该类就是一个异常处理类
public class GlobalExceptionHandler {

    //该方法 当你通过注解指定的异常在controller中修饰了@requestMapping的注解的方法 发生的时候 将被会调用 返回给前端数据
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result handlerException(Exception e) {
        //异常输出到日志文件中 处理222222
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }

    /*@ResponseBody
    @ExceptionHandler(value = yyyy.class)
    public Result handlerException(Exception e) {
        //异常输出到日志文件中 处理 111111
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }*/
}