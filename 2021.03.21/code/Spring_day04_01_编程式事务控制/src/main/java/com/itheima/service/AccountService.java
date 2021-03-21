package com.itheima.service;

import com.itheima.domain.Account;

import java.util.List;

public interface AccountService {

    /**
     * 转账操作
     * @param outName     出账用户名
     * @param inName      入账用户名
     * @param money       转账金额
     */
    public void transfer(String outName, String inName, Double money);

}
