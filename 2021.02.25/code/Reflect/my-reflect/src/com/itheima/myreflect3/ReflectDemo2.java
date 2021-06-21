package com.itheima.myreflect3;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 获取Constructor对象并创建对象
 */

public class ReflectDemo2 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //T newInstance​(Object... initargs)：根据指定的构造方法创建对象
        //method1();
        //method2();
        //method3();
        //method4();
    }

    private static void method4() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        //获取一个私有的构造方法并创建对象
        //1.获取class对象
        Class clazz = Class.forName("com.itheima.myreflect3.Student");

        //2.获取一个私有化的构造方法.
        Constructor constructor = clazz.getDeclaredConstructor(String.class);

        //被private修饰的成员,不能直接使用的
        //如果用反射强行获取并使用,需要临时取消访问检查
        constructor.setAccessible(true);

        //3.直接创建对象
        Student student = (Student) constructor.newInstance("zhangsan");

        System.out.println(student);
    }

    private static void method3() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        //简写格式
        //1.获取class对象
        Class clazz = Class.forName("com.itheima.myreflect3.Student");

        //2.在Class类中,有一个newInstance​方法,可以利用空参直接创建一个对象
        Student student = (Student) clazz.newInstance();//这个方法现在已经过时了,了解一下

        System.out.println(student);
    }

    private static void method2() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        //1.获取class对象
        Class clazz = Class.forName("com.itheima.myreflect3.Student");

        //2.获取构造方法对象
        Constructor constructor = clazz.getConstructor();

        //3.利用空参来创建Student的对象
        Student student = (Student) constructor.newInstance();

        System.out.println(student);
    }

    private static void method1() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        //1.获取class对象
        Class clazz = Class.forName("com.itheima.myreflect3.Student");

        //2.获取构造方法对象
        Constructor constructor = clazz.getConstructor(String.class, int.class);

        //3.利用newInstance​创建Student的对象
        Student student = (Student) constructor.newInstance("zhangsan", 23);

        System.out.println(student);
    }
}
