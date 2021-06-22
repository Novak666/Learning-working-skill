package config.filter;

import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class MyTypeFilter implements TypeFilter {
    @Override
    //加载的类满足要求，匹配成功
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //通过参数获取加载的类的元数据
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //通过类的元数据获取类的名称
        String className = classMetadata.getClassName();
        //如果加载的类名满足过滤器要求，返回匹配成功
        if(className.equals("com.itheima.service.impl.UserServiceImpl")){
            //返回true表示匹配成功，返回false表示匹配失败。此处仅确认匹配结果，不会确认是排除还是加入，排除/加入由配置项决定，与此处无关
            return true;
        }
        return false;
    }
}
