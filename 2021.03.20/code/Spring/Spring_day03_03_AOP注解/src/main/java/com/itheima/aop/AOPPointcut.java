package com.itheima.aop;

import org.aspectj.lang.annotation.Pointcut;

public class AOPPointcut {
    @Pointcut("execution(* *..*(..))")
    public void pt1(){}
}