package com.itheima.myreflect1;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class ReflectDemo1 {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //获取系统类加载器，加载prop.properties文件
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("prop.properties");
        //创建Properties集合
        Properties prop = new Properties();
        //将文件中的数据读取到集合当中
        prop.load(is);
        is.close();

        //--------下面的代码就是利用反射创建对象并调用方法---------------------

        //获取字节码文件对象
        Class clazz = Class.forName(prop.getProperty("className"));
        //获取构造器对象
        Constructor constructor = clazz.getConstructor();
        //利用构造器对象创建一个对象
        Object o = constructor.newInstance();
        //获取方法对象
        Method method = clazz.getMethod(prop.getProperty("methodName"));
        //运行方法
        method.invoke(o);
    }
}
