package com.itheima.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
//该文件已经可以删除了
public class TxAdvice {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Object tx(ProceedingJoinPoint pjp) throws Throwable {
        DataSourceTransactionManager dstm = new DataSourceTransactionManager();
        dstm.setDataSource(dataSource);
        TransactionDefinition td = new DefaultTransactionDefinition();
        TransactionStatus ts = dstm.getTransaction(td);

        Object ret = pjp.proceed(pjp.getArgs());

        dstm.commit(ts);
        return ret;
    }
}
