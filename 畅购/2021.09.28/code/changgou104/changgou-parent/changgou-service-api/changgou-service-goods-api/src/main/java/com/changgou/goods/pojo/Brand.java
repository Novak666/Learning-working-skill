package com.changgou.goods.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/09/26
 **/
@ApiModel(description = "Brand",value = "Brand")
@Table(name="tb_brand")//指定映射关系到表名为tb_brand
public class Brand implements Serializable {

    @ApiModelProperty(value = "品牌id",required = false)
    @Id//注解标识该字段为主键

    // @GeneratedValue 设置主键的生成策略
    // strategy = GenerationType.IDENTITY 标识自增
    //@Column(name = "id") 建立 该注解修饰的POJO的字段和 表中的列建立映射关系  name用于指定列名 如果是pojo的属性名和表中的列名一致可以不写。
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;//品牌id

    @ApiModelProperty(value = "品牌名称",required = false)
    @Column(name = "name")
    private String name;//品牌名称
    @ApiModelProperty(value = "品牌图片地址",required = false)
    @Column(name = "image")
    private String image;//品牌图片地址
    @ApiModelProperty(value = "品牌的首字母",required = false)
    @Column(name = "letter")
    private String letter;//品牌的首字母
    @ApiModelProperty(value = "排序",required = false)
    @Column(name = "seq")
    private Integer seq;//排序


    //get方法
    public Integer getId() {
        return id;
    }

    //set方法
    public void setId(Integer id) {
        this.id = id;
    }
    //get方法
    public String getName() {
        return name;
    }

    //set方法
    public void setName(String name) {
        this.name = name;
    }
    //get方法
    public String getImage() {
        return image;
    }

    //set方法
    public void setImage(String image) {
        this.image = image;
    }
    //get方法
    public String getLetter() {
        return letter;
    }

    //set方法
    public void setLetter(String letter) {
        this.letter = letter;
    }
    //get方法
    public Integer getSeq() {
        return seq;
    }

    //set方法
    public void setSeq(Integer seq) {
        this.seq = seq;
    }


}
