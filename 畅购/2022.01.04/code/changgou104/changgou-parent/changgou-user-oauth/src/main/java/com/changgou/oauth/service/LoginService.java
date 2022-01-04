package com.changgou.oauth.service;

import java.util.Map;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/18 20:05
 **/
public interface LoginService {
    Map<String, String> login(String username, String password, String grantType, String clientId, String clientSecret);
}
