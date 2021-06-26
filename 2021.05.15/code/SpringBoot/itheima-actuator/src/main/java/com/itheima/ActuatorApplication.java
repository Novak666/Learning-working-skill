package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 14:36
 * @description 标题
 * @package com.itheima
 */
@SpringBootApplication
public class ActuatorApplication  {
    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class,args);
    }

    @RestController
    @RequestMapping("/test")
    class TestController {

        @GetMapping("/index")
        public String show() {
            return "hello world";
        }
    }


}
