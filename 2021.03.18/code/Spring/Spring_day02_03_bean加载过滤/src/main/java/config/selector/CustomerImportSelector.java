package config.selector;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class CustomerImportSelector implements ImportSelector {

    private String expression;

    public CustomerImportSelector(){
        try {
            //初始化时指定加载的properties文件名
            Properties loadAllProperties = PropertiesLoaderUtils.loadAllProperties("import.properties");
            //设定加载的属性名
            expression = loadAllProperties.getProperty("path");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //1.定义扫描包的名称
        String[] basePackages = null;
        //2.判断有@Import注解的类上是否有@ComponentScan注解
        if (importingClassMetadata.hasAnnotation(ComponentScan.class.getName())) {
            //3.取出@ComponentScan注解的属性
            Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(ComponentScan.class.getName());
            //4.取出属性名称为basePackages属性的值
            basePackages = (String[]) annotationAttributes.get("basePackages");
        }
        //5.判断是否有此属性（如果没有ComponentScan注解则属性值为null，如果有ComponentScan注解，则basePackages默认为空数组）
        if (basePackages == null || basePackages.length == 0) {
            String basePackage = null;
            try {
                //6.取出包含@Import注解类的包名
                basePackage = Class.forName(importingClassMetadata.getClassName()).getPackage().getName();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //7.存入数组中
            basePackages = new String[] {basePackage};
        }
        //8.创建类路径扫描器
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        //9.创建类型过滤器(此处使用切入点表达式类型过滤器)
        TypeFilter typeFilter = new AspectJTypeFilter(expression,this.getClass().getClassLoader());
        //10.给扫描器加入类型过滤器
        scanner.addIncludeFilter(typeFilter);
        //11.创建存放全限定类名的集合
        Set<String> classes = new HashSet<>();
        //12.填充集合数据
        for (String basePackage : basePackages) {
            scanner.findCandidateComponents(basePackage).forEach(beanDefinition -> classes.add(beanDefinition.getBeanClassName()));
        }
        //13.按照规则返回
        return classes.toArray(new String[classes.size()]);
    }
}
