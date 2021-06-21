package com.itheima.myreflect2;

/**
 * 获取class对象的三种方式
 */
public class ReflectDemo1 {
    public static void main(String[] args) throws ClassNotFoundException {
        //1.Class类中的静态方法forName("全类名")
            //全类名:包名 + 类名
        Class clazz = Class.forName("com.itheima.myreflect2.Student");
        System.out.println(clazz);

        //2.通过class属性来获取
        Class clazz2 = Student.class;
        System.out.println(clazz2);

        //3.利用对象的getClass方法来获取class对象
        //getClass方法是定义在Object类中.
        Student s = new Student();
        Class clazz3 = s.getClass();
        System.out.println(clazz3);

        System.out.println(clazz == clazz2);
        System.out.println(clazz2 == clazz3);
    }
}
