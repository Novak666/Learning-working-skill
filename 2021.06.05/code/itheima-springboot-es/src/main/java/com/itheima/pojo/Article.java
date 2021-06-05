package com.itheima.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 1.创建索引
 * 2.创建类型
 * 3.设置文档的唯一标识
 * 4.手动创建映射
 *
 *
 * @Document(indexName = "blog03",type = "article")
 *
 * document 注解修饰类 标识该POJO和es文档建立映射关系
 *      indexName 指定索引名称
 *      type 指定类型的名称
 * @Field(type= FieldType.Text,index = true,store = false,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
 *      field 修饰POJO中的属性 标识该属性和es中的字段建立映射关系
 *          type 指定数据类型 text 默认就是分词的 分词器就是标准分词器
 *          index 是否索引 默认是true
 *          store 是否存储 默认是false
 *          analyzer 设置建立倒排索引的时候使用的分词器
 *          searchAnalyzer 设置 搜索的时候使用的分词器  一般不用设置 默认和analzyer指定的一致。
 *
 * @author ljh
 * @version 1.0
 * @date 2020/12/17 11:38
 * @description 标题
 * @package com.itheima.pojo
 */

@Document(indexName = "blog03",type = "article")
public class Article {


    @Id//文档唯一标识
    private Long id;

    @Field(type= FieldType.Text,index = true,store = false,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
    private String title;
    @Field(type= FieldType.Text,index = true,store = false,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
    private String content;

    public Article() {
    }

    public Article(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
