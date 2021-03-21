package com.itheima.service;

import com.itheima.domain.Account;

import java.util.List;

public interface AccountService {

    void save(Account account);

    void changeMoney(Integer id,Double money);

    Double findMondyById(Integer id);
}
