package com.changgou.pay.pojo;

import org.springframework.stereotype.Component;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2022/01/06 19:08
 **/

@Component
//@ConfigurationProperties(prefix = "")
public class WxProperties {
    private String seckillorder;
    private String seckillkey;

    public String getSeckillorder() {
        return seckillorder;
    }

    public void setSeckillorder(String seckillorder) {
        this.seckillorder = seckillorder;
    }

    public String getSeckillkey() {
        return seckillkey;
    }

    public void setSeckillkey(String seckillkey) {
        this.seckillkey = seckillkey;
    }
}
