package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/19 15:25
 **/
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.changgou.order.dao")
@EnableFeignClients(basePackages = "com.changgou.goods.feign")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
