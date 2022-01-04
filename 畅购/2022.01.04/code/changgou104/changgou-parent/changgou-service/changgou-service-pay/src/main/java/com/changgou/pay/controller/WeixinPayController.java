package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/11/28 9:06
 **/
@RestController
@RequestMapping("/weixin/pay")
public class WeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;

    /**
     * 生成支付二维码的链接 返回给页面 页面通过jS插件生成二维码
     * @param out_trade_no  订单号
     * @param total_fee  金额 （单位:分）
     * @return
     */
    @GetMapping("/create/native")
    public Result<Map<String,String>> createNative(String out_trade_no, String total_fee){
        //1.模拟浏览器 发送HTTPS请求 给微信支付系统
        Map<String,String> resultMap = weixinPayService.createNative(out_trade_no,total_fee);
        return new Result<Map<String,String>>(true, StatusCode.OK,"生成成功",resultMap);
    }

    /**
     * 根据订单号 获取订单对应的支付的状态等信息
     * @param out_trade_no
     * @return
     */
    @GetMapping("/status/query")
    public Result<Map<String,String>> queryStatus(String out_trade_no){
        Map<String,String> resultMap =  weixinPayService.queryStatus(out_trade_no);
        return new Result<Map<String,String>>(true,StatusCode.OK,"查询订单的状态成功，具体状态请查看data",resultMap);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Value("${mq.pay.exchange.order}")
    private String exchange;

    @Value("${mq.pay.routing.key}")
    private String routing;

    //成功
    private static final String SUCCESS_RETURN="<xml>" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "  <return_msg><![CDATA[OK]]></return_msg>\n" +
            "</xml>";
    //写一个方法 接收微信发送过来的请求（微信的通知）
    @RequestMapping("/notify/url")
    public String notifyurl(HttpServletRequest request){
        InputStream inStream;
        try {
            //hutools io工具类
            //1.接收微信通知过来的数据 通过数据流的形式获取
            //读取支付回调数据
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            // 将支付回调数据转换成xml字符串
            String result = new String(outSteam.toByteArray(), "utf-8");
            //XML--->MAP
            System.out.println(result);
            Map<String, String> map = WXPayUtil.xmlToMap(result);
            //2.发送消息给MQ 消息本身至少：订单号，交易流水，支付时间  {key1:value1,key2:value2,key3:value3}
            //参数1 指定交换机的名称
            //参数2 指定routingkey
            //参数3 指定消息本身
            rabbitTemplate.convertAndSend(exchange,routing, JSON.toJSONString(map));

            //3.返回给微信
            System.out.println("呵呵呵呵呵===============");
            return SUCCESS_RETURN;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
