package com.itheima.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//自定义类型转换器，实现Converter接口，接口中指定的泛型即为最终作用的条件
//本例中的泛型填写的是String，Date，最终出现字符串转日期时，该类型转换器生效
public class MyDateConverter implements Converter<String, Date> {
    //重写接口的抽象方法，参数由泛型决定
    public Date convert(String source) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        //类型转换器无法预计使用过程中出现的异常，因此必须在类型转换器内部捕获，不允许抛出，框架无法预计此类异常如何处理
        try {
            date = df.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
