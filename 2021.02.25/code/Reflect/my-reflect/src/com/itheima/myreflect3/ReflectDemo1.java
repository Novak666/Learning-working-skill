package com.itheima.myreflect3;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;

/**
 * 获取Constructor对象
 */

public class ReflectDemo1 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException {
        //method1();
        //method2();
        //method3();
        //method4();


    }

    private static void method4() throws ClassNotFoundException, NoSuchMethodException {
        //        Constructor<T> getDeclaredConstructor​(Class<?>... parameterTypes)：
//                                      返回单个构造方法对象
        //1.获取Class对象
        Class clazz = Class.forName("com.itheima.myreflect3.Student");
        Constructor constructor = clazz.getDeclaredConstructor(String.class);
        System.out.println(constructor);
    }

    private static void method3() throws ClassNotFoundException, NoSuchMethodException {
        //        Constructor<T> getConstructor​(Class<?>... parameterTypes)：
//                                      返回单个公共构造方法对象
        //1.获取Class对象
        Class clazz = Class.forName("com.itheima.myreflect3.Student");
        //小括号中,一定要跟构造方法的形参保持一致.
        Constructor constructor1 = clazz.getConstructor();
        System.out.println(constructor1);

        Constructor constructor2 = clazz.getConstructor(String.class, int.class);
        System.out.println(constructor2);

        //因为Student类中,没有只有一个int的构造,所以这里会报错.
        Constructor constructor3 = clazz.getConstructor(int.class);
        System.out.println(constructor3);
    }

    private static void method2() throws ClassNotFoundException {
        //        Constructor<?>[] getDeclaredConstructors​()：
//                                      返回所有构造方法对象的数组
        //1.获取Class对象
        Class clazz = Class.forName("com.itheima.myreflect3.Student");

        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
        }
    }

    private static void method1() throws ClassNotFoundException {
        //        Constructor<?>[] getConstructors​()：
//                                      返回所有公共构造方法对象的数组
        //1.获取Class对象
        Class clazz = Class.forName("com.itheima.myreflect3.Student");
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
        }
    }
}
