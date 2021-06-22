package config.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ResourceBundle;

public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
//      1.编程形式加载一个类
//      return new String[]{"com.itheima.dao.impl.BookDaoImpl"};

//      2.加载import.properties文件中的单个类名
//      ResourceBundle bundle = ResourceBundle.getBundle("import");
//      String className = bundle.getString("className");

//      3.加载import.properties文件中的多个类名
        ResourceBundle bundle = ResourceBundle.getBundle("import");
        String className = bundle.getString("className");
        return className.split(",");
    }
}
