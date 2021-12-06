package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/10/14
 **/
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.changgou.content.dao"})
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}
