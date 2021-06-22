package com.itheima.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

@Configuration
@Import({JDBCConfig.class})
@ComponentScan("com.itheima")
@Order(2)
public class SpringConfig {

    @Bean("b1")
    public String getB1(){
        System.out.println("b1");
        return "";
    }


}
