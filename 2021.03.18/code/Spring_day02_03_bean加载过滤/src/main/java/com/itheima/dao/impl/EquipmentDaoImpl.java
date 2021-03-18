package com.itheima.dao.impl;

import com.itheima.dao.EquipmentDao;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class EquipmentDaoImpl implements EquipmentDao,InitializingBean {

    public void save() {
        System.out.println("equipment dao running...");
    }

    @Override
    //定义当前bean初始化操作，功效等同于init-method属性配置
    public void afterPropertiesSet() throws Exception {
        SqlSessionFactoryBean fb;
        System.out.println("EquipmentDaoImpl......bean ...init......");
    }
}
