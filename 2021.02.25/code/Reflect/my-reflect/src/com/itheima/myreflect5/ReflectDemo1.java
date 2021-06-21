package com.itheima.myreflect5;

import java.lang.reflect.Method;

/**
 * 获取Method对象
 */

public class ReflectDemo1 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException {
        //method1();
        //method2();
        //method3();
        //method4();
        //method5();
    }

    private static void method5() throws ClassNotFoundException, NoSuchMethodException {
        //        Method getDeclaredMethod​(String name, Class<?>... parameterTypes)：
//                                返回单个成员方法对象
        //1.获取class对象
        Class clazz = Class.forName("com.itheima.myreflect5.Student");
        //2.获取一个成员方法show
        Method method = clazz.getDeclaredMethod("show");
        //3.打印一下
        System.out.println(method);
    }

    private static void method4() throws ClassNotFoundException, NoSuchMethodException {
        //1.获取class对象
        Class clazz = Class.forName("com.itheima.myreflect5.Student");
        //2.获取一个有形参的方法function2
        Method method = clazz.getMethod("function2", String.class);
        //3.打印一下
        System.out.println(method);
    }

    private static void method3() throws ClassNotFoundException, NoSuchMethodException {
        //        Method getMethod​(String name, Class<?>... parameterTypes) ：
//                                返回单个公共成员方法对象
        //1.获取class对象
        Class clazz = Class.forName("com.itheima.myreflect5.Student");
        //2.获取成员方法function1
        Method method1 = clazz.getMethod("function1");
        //3.打印一下
        System.out.println(method1);
    }

    private static void method2() throws ClassNotFoundException {
        //        Method[] getDeclaredMethods​()：
//                                返回所有成员方法对象的数组，不包括继承的
        //1.获取class对象
        Class clazz = Class.forName("com.itheima.myreflect5.Student");

        //2.获取Method对象
        Method[] methods = clazz.getDeclaredMethods();
        //3.遍历一下数组
        for (Method method : methods) {
            System.out.println(method);
        }
    }

    private static void method1() throws ClassNotFoundException {
        //        Method[] getMethods​()：返回所有公共成员方法对象的数组，包括继承的
        //1.获取class对象
        Class clazz = Class.forName("com.itheima.myreflect5.Student");
        //2.获取成员方法对象
        Method[] methods = clazz.getMethods();
        //3.遍历
        for (Method method : methods) {
            System.out.println(method);
        }
    }
}
