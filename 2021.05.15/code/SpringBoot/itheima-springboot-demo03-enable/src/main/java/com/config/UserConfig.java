package com.config;

import com.itheima.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 10:18
 * @description 标题
 * @package com.config
 */
@Configuration//applicationContext.xml
//<bean
public class UserConfig {

    @Bean
    public User user(){
        return new User();
    }
}
