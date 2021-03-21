package com.itheima.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

public class TxAdvice {

    private DataSource dataSource;
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Object transactionManager(ProceedingJoinPoint pjp) throws Throwable {
        //开启事务
        PlatformTransactionManager ptm = new DataSourceTransactionManager(dataSource);
        //事务定义
        TransactionDefinition td = new DefaultTransactionDefinition();
        //事务状态
        TransactionStatus ts = ptm.getTransaction(td);

        Object ret = pjp.proceed(pjp.getArgs());

        //提交事务
        ptm.commit(ts);

        return ret;
    }

}
