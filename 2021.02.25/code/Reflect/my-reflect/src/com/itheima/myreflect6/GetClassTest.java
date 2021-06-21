package com.itheima.myreflect6;

import org.junit.Test;

public class GetClassTest {
    @Test
    public void test1(){
        Class<?> clz = Person.class;
    }
    @Test
    public void test2() throws ClassNotFoundException {
        Class<?> clz = Class.forName("com.itheima.myreflect6.Person");
    }
    @Test
    public void test3() {
        Class<?> clz = new Person().getClass();
    }
}
