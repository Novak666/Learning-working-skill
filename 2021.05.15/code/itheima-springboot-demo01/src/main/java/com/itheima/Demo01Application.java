package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 08:45
 * @description 标题
 * @package com.itheima
 */

@SpringBootApplication
public class Demo01Application {
    public static void main(String[] args) {
        //spring容器对象
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Demo01Application.class, args);
        //从容器中获取对象
        //Object redisTemplate = applicationContext.getBean("redisTemplate");
        //System.out.println(redisTemplate);
        Object user = applicationContext.getBean("user");
        System.out.println(user);

    }
}
