package com.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 10:50
 * @description 标题
 * @package com.config
 */
public class MyImportSelector implements ImportSelector {

    //返回的是类的全路径的字符串数组  用来从例如：properties  xml 等其他的配置文件中加载 配置好的一些类的全路径 这些类交给spring容器进行管理 并进行配置
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //将来这个数据是从配置文件中读取到的
        return new String[]{"com.itheima.pojo.Role","com.itheima.pojo.User"};
    }
}
