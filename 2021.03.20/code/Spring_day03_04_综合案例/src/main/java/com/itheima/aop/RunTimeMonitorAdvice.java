package com.itheima.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RunTimeMonitorAdvice {

    //切入点，监控业务层接口
    @Pointcut("execution(* com.itheima.service.*Service.find*(..))")
    public void pt(){}

    @Around("pt()")
    public Object runtimeAround(ProceedingJoinPoint pjp) throws Throwable {
        //获取执行签名信息
        Signature signature = pjp.getSignature();
        //通过签名获取执行类型（接口名）
        String className = signature.getDeclaringTypeName();
        //通过签名获取执行操作名称（方法名）
        String methodName = signature.getName();

        //执行时长累计值
        long sum = 0L;

        for (int i = 0; i < 10000; i++) {
            //获取操作前系统时间beginTime
            long startTime = System.currentTimeMillis();
            //原始操作调用
            pjp.proceed(pjp.getArgs());
            //获取操作后系统时间endTime
            long endTime = System.currentTimeMillis();
            sum += endTime-startTime;
        }
        //打印信息
        System.out.println(className+":"+methodName+"   (万次）run:"+sum+"ms");
        return null;
    }
}
