package com.itheima;

import com.config.EnableAutoUser;
import com.config.MyImportBeanDefinitionRegistrar;
import com.config.MyImportSelector;
import com.config.UserConfig;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 10:19
 * @description 标题
 * @package com.itheima
 */
@SpringBootApplication
//获取第三方的Bean的方式   放大包扫描    1种
//@ComponentScan(basePackages = "com")

//获取第三方的Bean的方式   直接导入配置类    2种
//@Import(UserConfig.class)

//@Import(MyImportSelector.class)// importSelector
@Import(MyImportBeanDefinitionRegistrar.class)// ImportBeanDefinitionRegistrar

//获取第三方的Bean的方式   直接使用开启类型的注解 Enable*    3种
//@EnableAutoUser
public class Demo2EnableApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Demo2EnableApplication.class, args);

        //直接获取user对象 user对象已经在demo3中的容器中了
        //Object user = context.getBean("user");
        //Object role = context.getBean("role");
        User user = (User)context.getBean("abcdefg");
        //Role role = context.getBean(Role.class);

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }

        System.out.println(user);
        //System.out.println(role);
    }
}
