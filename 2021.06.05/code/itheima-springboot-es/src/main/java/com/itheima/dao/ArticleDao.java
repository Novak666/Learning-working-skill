package com.itheima.dao;

import com.itheima.pojo.Article;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Article,Long
 * 第一个标识操作的数据类型
 * 第二个标识操作的数据类型的文档唯一标识的数据类型
 * @author ljh
 * @version 1.0
 * @date 2020/12/19 15:25
 * @description 标题
 * @package com.itheima.dao
 */
public interface ArticleDao extends ElasticsearchRepository<Article,Long> {
    //创建文档
    //更新文档@Highlight
    //查询文档
    //删除文档

    //自定义查询
    List<Article> findByTitle(String title);

    List<Article> findByTitleAndContent(String title,String content);

    @Query("{\"match\": {\"title\": {\"query\": \"?0\"}}}")//dsl语句
    List<Article> abcdefg(String acdf);

}
