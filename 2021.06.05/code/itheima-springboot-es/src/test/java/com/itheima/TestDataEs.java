package com.itheima;

import com.itheima.dao.ArticleDao;
import com.itheima.pojo.Article;
import org.elasticsearch.client.Requests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/19 15:27
 * @description 标题
 * @package com.itheima
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDataEs {

    @Autowired
    private ArticleDao articleDao;

    //核心的类 操作ES的
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    //创建索引和映射
    @Test
    public void createIndexAndMapping(){
        elasticsearchTemplate.createIndex(Article.class);
        elasticsearchTemplate.putMapping(Article.class);
    }

    //创建文档

    @Test
    public void createDoducmnet(){
        List<Article> articles = new ArrayList<>();
        for (long i = 0; i < 100; i++) {
            Article article = new Article(i,"华为手机哼班123"+i,"华为手机OK"+i);
            articles.add(article);
        }
        articleDao.saveAll(articles);

    }

    //更新文档

    //删除文档
    @Test
    public void delete(){
        articleDao.deleteById(1L);
    }


    //查询文档
    @Test
    public void select(){
        /*Iterable<Article> all = articleDao.findAll();


        //分页查询

        //参数1 指定排序的类型
        //参数2 指定排序的字段
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        //参数1 指定当前的页码 0 表示第一个页
        //参数2 指定每页显示的行
        //参数3 指定排序对象
        Pageable pageble = PageRequest.of(0,10,sort);
        Page<Article> page = articleDao.findAll(pageble);


        long totalElements = page.getTotalElements();
        System.out.println(totalElements);
        int totalPages = page.getTotalPages();
        System.out.println(totalPages);


        System.out.println("==============");
        List<Article> content = page.getContent();//当前的页的记录
        for (Article article : content) {
            System.out.println(article.getTitle());
        }*/

        //获取列表记录
       /* for (Article article : all) {
            System.out.println(article.getTitle());
        }*/

        //自定义查询
        List<Article> articles = articleDao.findByTitle("手机");
        for (Article article : articles) {
            System.out.println(article.getTitle());
        }
    }

}
