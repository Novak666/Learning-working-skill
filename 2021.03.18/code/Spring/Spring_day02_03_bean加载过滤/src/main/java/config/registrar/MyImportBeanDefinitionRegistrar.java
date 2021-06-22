package config.registrar;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //自定义注册器
        //1.开启类路径bean定义扫描器，需要参数bean定义注册器BeanDefinitionRegistry，需要制定是否使用默认类型过滤器
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry,false);
        //2.添加包含性加载类型过滤器（可选，也可以设置为排除性加载类型过滤器）
        scanner.addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                //所有匹配全部成功，此处应该添加实际的业务判定条件
                return true;
            }
        });
        //设置扫描路径
        scanner.scan("com.itheima");
    }
}
