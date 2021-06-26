package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 14:59
 * @description 标题
 * @package com.itheima
 */
@SpringBootApplication
public class IthimaAdminClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(IthimaAdminClientApplication.class,args);
    }
    @RestController
    @RequestMapping("/user")
    class TestController {

        @RequestMapping("/findAll")
        public String a() {
            return "aaaa";
        }
    }


}
