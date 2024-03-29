package com.changgou.order.config;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/20 22:20
 **/
@Component
public class TokenDecode {
    private static final String PUBLIC_KEY = "public.key";
    //获取用户名
    public String getUsername() {
        return getUserInfo().get("user_name");
    }

    private String getPubKey() {
        Resource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            return null;
        }
    }
    //获取token的数据map
    public Map<String, String> getUserInfo() {
        //1.获取用户名
        //获取令牌的值
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String tokenValue = details.getTokenValue();
        //解析令牌
        //公钥
        //String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA10CtXJLMtkFmPUNkRNQK7smqMGTm+O29WR8JaWrOksGYmImiaCVg1fo7yIbyKiukl+uBl9t4F9POkGPOeilWuNfz8QhAdQ1CoAVUISIsvn2oJL8+x/3My4kHHtZKJsRuIJilfnyiVRdkTEs2wSDipOFxczEIyM1tWR1Odbepax0j6Jxvg38mzqoMViFfHhtuh0h5U659wIgRGXlJuGuBpVbIbp7CO+/DuQR0M/dX4GnwS4TXXUqCJ651zz28ERuEtVRjO3FIqc1U4kvtMEsPMdpTCsawj2cfv9Zj3EfVOK3thAOMsjJVukOWwejOIw5qfdbHMgun6XB5aORcbplHawIDAQAB-----END PUBLIC KEY-----";

        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(tokenValue, new RsaVerifier(getPubKey()));

        //获取Jwt原始内容
        String claims = jwt.getClaims();

        Map<String,String> map = JSON.parseObject(claims,Map.class);
        return map;
    }
}
