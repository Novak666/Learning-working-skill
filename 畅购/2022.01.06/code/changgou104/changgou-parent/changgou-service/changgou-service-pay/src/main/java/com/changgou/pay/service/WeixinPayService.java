package com.changgou.pay.service;

import java.util.Map;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/28 9:20
 **/
public interface WeixinPayService {
    Map<String, String> createNative(Map<String,String> parameter);

    Map<String, String> queryStatus(String out_trade_no);
}
