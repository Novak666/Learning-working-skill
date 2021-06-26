package com.itheima;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 10:07
 * @description 标题
 * @package com.itheima
 */
@RestController
public class TestController {
    @RequestMapping("/hello")
    public String show() {
        return "hello world";
    }

}
