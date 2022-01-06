package com.changgou.order.pojo;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2022/01/06 14:34
 **/
public class OrderVo extends Order {
    //1 普通 2  秒杀
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
