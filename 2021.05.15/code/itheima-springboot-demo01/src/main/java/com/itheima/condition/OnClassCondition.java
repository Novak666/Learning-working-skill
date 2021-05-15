package com.itheima.condition;

import com.itheima.annotation.ConditionalOnClass;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/10 09:01
 * @description 标题
 * @package com.itheima.condition
 */
public class OnClassCondition implements Condition {
    /**
     *
     * @param context 条件上下文对象
     * @param metadata 注解相关的元数据
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //当pom.xml中加入了坐标 就返回true  不加 就返回false
        //从注解中配置的属性中获取之后再加载
        try {
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnClass.class.getName());
            String[] names = (String[]) annotationAttributes.get("name");
            for (String name : names) {
                Class.forName(name);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
