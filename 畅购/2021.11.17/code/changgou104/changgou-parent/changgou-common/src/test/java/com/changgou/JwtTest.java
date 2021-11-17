package com.changgou;

import io.jsonwebtoken.*;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/12 14:08
 **/
public class JwtTest {

    @Test
    public void createToken() {
        //1.设置头 默认不需要设置
        JwtBuilder builder = Jwts.builder();
        //2.设置载荷
        builder
                .setId("唯一的标识")//唯一标识
                .setSubject("主题")//主题数据
                .setIssuer("heima")//签发者
                //.setExpiration(new Date())//设置有效期
                .setIssuedAt(new Date())//设置签发日期
                //3.设置签名
                .signWith(SignatureAlgorithm.HS256,"itcast");

        //自定义Claims(自定义载荷信息)//JSON
        Map<String, Object> map = new HashMap<>();
        map.put("key1","value1");
        map.put("key2","value2");
        builder.addClaims(map);

        //4.执行生成的动作 返回一个字符串（JWT===》token）
        String token = builder.compact();
        System.out.println(token);
    }

    @Test
    public void parseToken(){
        //定义一个token字符串
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiLllK_kuIDnmoTmoIfor4YiLCJzdWIiOiLkuLvpopgiLCJpc3MiOiJoZWltYSIsImlhdCI6MTYwOTU3MTExOSwia2V5MSI6InZhbHVlMSIsImtleTIiOiJ2YWx1ZTIifQ.x6w8X7eBPSp_xHR0377BAa475gfpaXK-2ImnT347YdQ";
        JwtParser parser = Jwts.parser();
        //设置签名的秘钥
        Jws<Claims> itcast = parser
                .setSigningKey("itcast")
                .parseClaimsJws(token);
        //获取载荷信息
        Claims body = itcast.getBody();
        JwsHeader header = itcast.getHeader();
        //打印
        System.out.println(body);
        System.out.println(header);
    }

}
