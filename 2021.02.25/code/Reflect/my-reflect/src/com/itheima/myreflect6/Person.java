package com.itheima.myreflect6;

public class Person {
    static {
        System.out.println("Person：静态代码块");
    }
    {
        System.out.println("Person：动态代码块");
    }
    public Person(){
        System.out.println("Person：构造方法");
    }
}
