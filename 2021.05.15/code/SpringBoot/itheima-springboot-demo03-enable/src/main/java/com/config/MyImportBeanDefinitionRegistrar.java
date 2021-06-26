package com.config;

import com.itheima.pojo.User;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <bean class ="" name="xxxx" ></bean>
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 10:59
 * @description 标题
 * @package com.config
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    //registerBeanDefinitions 手动将java对象注册给spring容器  例如：将User类型的对象给spring容器管理
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(User.class);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        registry.registerBeanDefinition("abcdefg",beanDefinition);
    }
}
