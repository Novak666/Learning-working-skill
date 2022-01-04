package com.changgou.pay.service.impl;

import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/28 9:21
 **/
@Service
public class WeixinPayServiceImpl implements WeixinPayService {
    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.partner}")
    private String partner;

    @Value("${weixin.partnerkey}")
    private String partnerkey;

    @Value("${weixin.notifyurl}")
    private String notifyurl;

    //模拟浏览器发送请求到微信支付系统
    //获取到code_url 返回
    @Override
    public Map<String, String> createNative(String out_trade_no, String total_fee) {
        try {
            //1.创建一个map 组装参数
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("appid",appid);
            paramMap.put("mch_id",partner);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            //签名 不用设置，在进行将map转成XML的过程中自动添加签名
            //paramMap.put("nonce_str", WXPayUtil.);

            paramMap.put("body", "畅购");
            //畅购的订单号
            paramMap.put("out_trade_no", out_trade_no);
            //金额 单位 分
            paramMap.put("total_fee", total_fee);
            //终端ip
            paramMap.put("spbill_create_ip", "127.0.0.1");
            //通知地址
            paramMap.put("notify_url", notifyurl);
            //扫码支付
            paramMap.put("trade_type", "NATIVE");
            //2.将map 转成XML 会自动的添加签名
            String xmlParam = WXPayUtil.generateSignedXml(paramMap, partnerkey);
            //3.使用Httpclient 模拟浏览器发送一个HTTPS的POST的请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();
            //4.使用httpclient 模拟浏览器接收响应（微信支付系统返回的一个XML）
            String result = httpClient.getContent();
            System.out.println(result);
            //5.将XML转成MAP
            Map<String, String> stringMap = WXPayUtil.xmlToMap(result);
            //6.按需解析返回 需要金额 需要 订单号 需要code_url
            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("code_url",stringMap.get("code_url"));
            resultMap.put("total_fee",total_fee);
            resultMap.put("out_trade_no",out_trade_no);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //调用查询订单的API
    @Override
    public Map<String, String> queryStatus(String out_trade_no) {
        try {
            //1.创建一个map 组装参数
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("appid",appid);
            paramMap.put("mch_id",partner);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            //签名 不用设置，在进行将map转成XML的过程中[自动]添加签名
            //paramMap.put("nonce_str", WXPayUtil.);

            //畅购的订单号
            paramMap.put("out_trade_no", out_trade_no);


            //2.将map 转成XML 会自动的添加签名
            String xmlParam = WXPayUtil.generateSignedXml(paramMap, partnerkey);

            //3.使用Httpclient 模拟浏览器发送一个HTTPS的POST的请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();
            //4.使用httpclient 模拟浏览器接收响应（微信支付系统返回的一个XML）
            String result = httpClient.getContent();
            System.out.println(result);
            //5.将XML转成MAP
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
