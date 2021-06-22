package com.itheima.service;

import com.itheima.domain.Account;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
public interface AccountService {
    @Transactional
    public void transfer(String outName, String inName, Double money);

}
