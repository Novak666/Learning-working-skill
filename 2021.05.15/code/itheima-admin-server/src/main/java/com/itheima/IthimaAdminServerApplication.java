package com.itheima;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 14:57
 * @description 标题
 * @package com.itheima
 */
@SpringBootApplication
@EnableAdminServer
public class IthimaAdminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(IthimaAdminServerApplication.class,args);
    }
}
