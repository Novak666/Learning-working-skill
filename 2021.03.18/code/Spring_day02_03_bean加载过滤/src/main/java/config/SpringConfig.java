package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.itheima")
//1.设置排除bean，排除的规则是注解修饰的（FilterType.ANNOTATION）的bean，具体的注解为（Service.class）
/*
@ComponentScan(
    value = "com.itheima",
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ANNOTATION,
        classes = Service.class
        )
    )
*/

//2.设置排除bean，排除的规则是自定义规则（FilterType.CUSTOM），具体的规则定义为（MyTypeFilter.class）
/*
@ComponentScan(
        value = "com.itheima",
        excludeFilters = @ComponentScan.Filter(
                type= FilterType.CUSTOM,
                classes = MyTypeFilter.class
        )
)
*/

//3.自定义导入器
//@Import(MyImportSelector.class)

//4.自定义注册器
//@Import(MyImportBeanDefinitionRegistrar.class)

//5.自定义工厂后处理bean，bean定义前处理后处理bean
//@Import({CustomeImportBeanDefinitionRegistrar.class,MyBeanFactory.class, MyBean.class})

public class SpringConfig {
}
