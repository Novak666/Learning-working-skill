package com.itheima.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class AOPAdvice {

    public void before(JoinPoint jp){
        //通过JoinPoint参数获取调用原始方法所携带的参数
        Object[] args = jp.getArgs();
        System.out.println("before..."+args[0]);
    }

    public void before1(int x,int y){
        System.out.println("before(int)..."+x+","+y);
    }


//    public void after(JoinPoint jp){
//        Object[] args = jp.getArgs();
//        System.out.println("after..."+args[0]);
//    }

    public void after(){
        System.out.println("after...");
    }

    public void afterReturing(Object ret){
        System.out.println("afterReturing..."+ret);
    }

//    public void afterReturing(){
//        System.out.println("afterReturing...");
//    }

    public void afterThrowing(Throwable t){
        System.out.println("afterThrowing..."+t.getMessage());
    }

    public Object around(ProceedingJoinPoint pjp) {
        System.out.println("around before...");
        Object ret = null;
        try {
            //对原始方法的调用
            ret = pjp.proceed();
        } catch (Throwable throwable) {
            System.out.println("around...exception...."+throwable.getMessage());
        }
        System.out.println("around after..."+ret);
        return ret;
    }

    public void before2(){
        System.out.println("before..2");
    }
    public void before3(){
        System.out.println("before..3");
    }
}
