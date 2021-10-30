package com.changgou.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/*建立POJO 要和ES进行映射 通过es的注解建立映射关系

        1 创建索引
        2 创建类型 设置文档的唯一的标识
        3 建立映射关系
        数据类型是什么？ 是否分词 是否索引 是否存储 搜索分词器是什么 建立倒排索引的分词器？

        - @Document(indexName = "skuinfo",type = "docs") 建立POJO和ES中的文档的映射关系
        - indexName 指定索引名称
        - type 指定类型
        - org.springframework.data.annotation.Id 用于标识文档的唯一标识
        - @Field(type = FieldType.Text, analyzer = "ik_smart",store = false,index = true,searchAnalyzer = "ik_smart")
- @field 注解用于建立映射关系 字段的映射关系
        - type:指定数据类型
        - analyzer:指定建立倒排索引的时候使用的分词器
        - searchAnalyzer:指定搜索的时候使用的分词器，如果分词器一致，那么不用配置，只需要analyzer 配置即可
        - store ： true  /false    表示是否存储，默认就是false
        - index:  true  /false     标识是否索引  默认就是要索引*/
/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/10/28
 **/
@Document(indexName = "skuinfo",type = "docs")
public class SkuInfo implements Serializable {
    //商品id，同时也是商品编号
    @Id
    private Long id;

    //SKU名称
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String name;

    //商品价格，单位为：元
    @Field(type = FieldType.Double)
    private Long price;

    //库存数量 会自动的映射 不用写
    private Integer num;

    //商品图片  不分词 不索引 _source
    private String image;

    //商品状态，1-正常，2-下架，3-删除
    private String status;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //是否默认
    private String isDefault;

    //SPUID
    private Long spuId;

    //类目ID
    private Long categoryId;

    //类目名称 关键字 一定不分词
    @Field(type = FieldType.Keyword)
    private String categoryName;

    //品牌名称
    @Field(type = FieldType.Keyword)
    private String brandName;


    //规格
    private String spec;

    //规格参数
    private Map<String,Object> specMap;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Map<String, Object> getSpecMap() {
        return specMap;
    }

    public void setSpecMap(Map<String, Object> specMap) {
        this.specMap = specMap;
    }
}
