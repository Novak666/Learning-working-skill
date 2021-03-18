package com.itheima.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

@Configuration
@Import({JDBCConfig.class})
@ComponentScan("com.itheima")
@Order(1)
public class SpringConfig2 {
    @Bean("b2")

    public String getB2(){
        System.out.println("b2");
        return "";
    }
}
