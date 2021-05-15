package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 *
 * @SpringBootApplication
 *      @ComponentScan  注解用于扫描某一个包下的所有的类 将类带有注解：@componset,@controller @service @respoirtory的交给spring容器
 *      默认的情况下 会自动的扫描 该注解修饰的类所在的包以及子包
 *      @SpringBootConfigruation 作用就是修饰类标识该类就是一个配置类
 *
 *      @EnableAutoConfiguration: 启用自动配置
 *
 *
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 09:50
 * @description 标题
 * @package com.itheima
 */
@SpringBootApplication
public class Demo02Application {//当做 applicationContext.xml
    public static void main(String[] args) {
        //jetty
        SpringApplication.run(Demo02Application.class,args);
    }


}
