package com.itheima.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
//等同于<context:component-scan base-package="com.itheima">
@ComponentScan(value = "com.itheima",excludeFilters =
    //等同于<context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    @ComponentScan.Filter(type= FilterType.ANNOTATION,classes = {Controller.class}))
//等同于<context:property-placeholder location="classpath*:jdbc.properties"/>
@PropertySource("classpath:jdbc.properties")
//等同于<tx:annotation-driven />，bean的名称默认取transactionManager
@EnableTransactionManagement
@Import({MyBatisConfig.class,JdbcConfig.class})
public class SpringConfig {
    //等同于<bean id="txManager"/>
    @Bean("transactionManager")
    //等同于<bean class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    public DataSourceTransactionManager getTxManager(@Autowired DataSource dataSource){
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        //等同于<property name="dataSource" ref="dataSource"/>
        tm.setDataSource(dataSource);
        return tm;
    }
}






