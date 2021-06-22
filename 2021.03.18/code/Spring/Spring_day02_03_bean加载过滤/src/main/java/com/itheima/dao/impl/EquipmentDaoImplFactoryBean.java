package com.itheima.dao.impl;

import org.springframework.beans.factory.FactoryBean;

public class EquipmentDaoImplFactoryBean implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        return new EquipmentDaoImpl();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
