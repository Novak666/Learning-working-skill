package com.itheima.service;

import com.itheima.domain.Account;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//对当前接口的所有方法添加事务
@Transactional(isolation = Isolation.DEFAULT)
public interface AccountService {

    /**
     * 转账操作
     * @param outName     出账用户名
     * @param inName      入账用户名
     * @param money       转账金额
     */
    //对当前方法添加事务，该配置将替换接口的配置
    @Transactional(
        readOnly = false,
        timeout = -1,
        isolation = Isolation.DEFAULT,
        rollbackFor = {},   //java.lang.ArithmeticException.class, IOException.class
        noRollbackFor = {},
        propagation = Propagation.REQUIRED
        )
    public void transfer(String outName, String inName, Double money);

}
