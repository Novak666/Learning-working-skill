package com.itheima.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class AOPAdvice2 {

//    @Pointcut("execution(* *..*(..))")
//    public void pt(){}

//    @Before("AOPPointcut.pt1()")
//    public void before(){
//        System.out.println("前置before...");
//    }
//
//    @After("AOPPointcut.pt1()")
//    public void after(){
//        System.out.println("后置after...");
//    }
//
//    @AfterReturning("AOPPointcut.pt1()")
//    public void afterReturing(){
//        System.out.println("返回后afterReturing...");
//    }
//
//    @AfterThrowing("AOPPointcut.pt1()")
//    public void afterThrowing(){
//        System.out.println("抛出异常后afterThrowing...");
//    }
//
//    @Around("AOPPointcut.pt1()")
//    public Object around(ProceedingJoinPoint pjp) throws Throwable {
//        System.out.println("环绕前around before...");
//        Object ret = pjp.proceed();
//        System.out.println("环绕后around after...");
//        return ret;
//    }

//    @Before("AOPPointcut.pt1()")
//    public void aop003log(){
//        System.out.println("前置before...1");
//    }

    @Before("AOPPointcut.pt1()")
    public void aop001exception(){
        System.out.println("前置before...2");
    }

}
