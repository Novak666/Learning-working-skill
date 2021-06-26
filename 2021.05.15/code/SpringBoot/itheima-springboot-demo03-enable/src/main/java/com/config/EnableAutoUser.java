package com.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 10:45
 * @description 标题
 * @package com.config
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({UserConfig.class})
public @interface EnableAutoUser {
}
