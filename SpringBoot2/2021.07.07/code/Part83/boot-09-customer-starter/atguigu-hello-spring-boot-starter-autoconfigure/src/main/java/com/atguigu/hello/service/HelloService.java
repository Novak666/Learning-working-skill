package com.atguigu.hello.service;

import com.atguigu.hello.bean.HelloProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/09/23
 **/
public class HelloService {

    @Autowired
    HelloProperties helloProperties;

    public String sayHello(String username) {
        return helloProperties.getPrefix() + " " + username + " " + helloProperties.getSuffix();
    }

}
