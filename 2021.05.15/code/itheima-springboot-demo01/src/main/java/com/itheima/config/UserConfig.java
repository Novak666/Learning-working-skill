package com.itheima.config;

import com.itheima.annotation.ConditionalOnClass;
import com.itheima.condition.OnClassCondition;
import com.itheima.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 1.创建一个applicationContext.xml
 * 2.<bean class="xxx.xxx.xx" id="aaa"></bean>
 *
 *
 *
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 08:52
 * @description 标题
 * @package com.itheima.config
 */
//说明该类就是一个配置类---》该类 就类似于applicationContext.xml
@Configuration
public class UserConfig {

    // 相当于 <bean class="com.itheima.pojo.User" id="user"></bean>
    // 当 加入了redis的客户端的坐标之后才执行方法 如果不加 不执行方法
    @Bean
    //@Conditional(OnClassCondition.class)  标识条件注解 ，内部指定条件对象 当条件对象返回true 就执行  返回false 就不执行
    @ConditionalOnClass(name={"redis.clients.jedis.Jedis"})
    public User user(){
        return new User();
    }
}
