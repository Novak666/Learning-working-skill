package com.itheima.annotation;

import com.itheima.condition.OnClassCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 09:13
 * @description 标题
 * @package com.itheima.annotation
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(OnClassCondition.class)
public @interface ConditionalOnClass {
    String[] name() ;
    //
    //String[] values();
    //
    //
}
