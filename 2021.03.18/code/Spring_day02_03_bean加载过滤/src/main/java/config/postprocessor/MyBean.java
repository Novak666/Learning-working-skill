package config.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBean implements BeanPostProcessor {
    @Override
    //所有bean初始化前置操作
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("bean之前巴拉巴拉");
        System.out.println(beanName);
        return bean;
    }

    @Override
    //所有bean初始化后置操作
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("bean之后巴拉巴拉");
        return bean;
    }
}
