package com.itheima;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.pojo.Article;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @author ljh
 * @version 1.0
 * @date 2020/12/19 08:47
 * @description 标题
 * @package com.itheima
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EsTestday02 {
    @Autowired
    private TransportClient transportClient;

    @Autowired
    private ObjectMapper objectMapper;

    //索引的操作 创建
    @Test
    public void onlyCreateIndex() {
        transportClient.admin().indices().prepareCreate("blog03").get();
    }

    //删除索引
    @Test
    public void deleteIndex(){
        transportClient.admin().indices().prepareDelete("blog03").get();
    }

    //手动创建映射

    /**
     * "mappings" : {
     *     "article" : {
     *         "properties" : {
     *             "id" : { "type" : "long","store":"true" },
     *             "title" : { "type" : "text","analyzer":"ik_smart","index":"true","store":"true" },
     *             "content" : { "type" : "text","analyzer":"ik_smart","index":"true","store":"true" }
     *         }
     *     }
     * }
     *
     *
     */
    @Test
    public void putMapping() throws Exception{
        //1.创建索引
        transportClient.admin().indices().prepareCreate("blog03").get();
        //2.创建映射 设置针对某一个索引进行映射
        PutMappingRequest putMappingRequest = new PutMappingRequest("blog03");
        //设置类型
        putMappingRequest.type("article");
        //设置具体的映射关系
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()//{
                    .startObject("article") //"article":{
                        .startObject("properties")//"properties":{
                            .startObject("id") //"id":{
                                .field("type","long")
                                .field("store","false")
                            .endObject()//}
                            .startObject("title") //"title":{
                                .field("type","text")
                                .field("store","true")
                                .field("analyzer","ik_smart")
                                .field("index","true")
                            .endObject()//}
                            .startObject("content") //"content":{
                                .field("type","text")
                                .field("store","true")
                                .field("analyzer","ik_smart")
                                .field("index","true")
                            .endObject()//}
                        .endObject()//}
                    .endObject()//}
                .endObject()//}
                ;

        putMappingRequest.source(xContentBuilder);
        //创建映射
        transportClient.admin().indices().putMapping(putMappingRequest).get();

    }

    //创建文档（向es中的索引中添加文档数据）

    /**
     * { "id":2,"title":"数据","content":"数据"}
     *
     * @throws Exception
     */
    @Test
    public void createDocument() throws  Exception{
        Article article = new Article(1L, "震惊一点不震惊！最近航母开到美国了", "标题党数据");

        String jsonstr = objectMapper.writeValueAsString(article);


        transportClient.prepareIndex("blog03","article","2")
                 //设置文档数据
                .setSource(jsonstr,XContentType.JSON)
                .get();
    }

    //创建文档 另外一种方式 文档就是JSON
    @Test
    public void createDocumentJson() throws  Exception{
        XContentBuilder xcontentbuilder= XContentFactory.jsonBuilder()
                .startObject()//{
                .field("id",2)
                .field("title","数据")
                .field("content","数据测试xcontentbuidler")
                .endObject()//}
                ;
        transportClient.prepareIndex("blog03","article","2")
                //设置文档数据
                .setSource(xcontentbuilder)
                .get();

    }

    //造数据
    @Test
    public void createBatch() throws Exception{
        long start=System.currentTimeMillis();

        BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();

        for (long i = 0; i < 100; i++) {
            Article article = new Article(i, "elasticsearch华为手机很棒"+i, "华为手机真的很棒啊"+i);

            String jsonstr = objectMapper.writeValueAsString(article);

            IndexRequest request = new IndexRequest("blog03","article",i+"");
            request.source(jsonstr,XContentType.JSON);

            bulkRequestBuilder.add(request);

            /*transportClient.prepareIndex("blog03","article",i+"")
                    //设置文档数据
                    .setSource(jsonstr,XContentType.JSON)
                    .get();*/
        }

        //批量进行导入
        bulkRequestBuilder.get();

        long end = System.currentTimeMillis();

        System.out.println("话费 了："+(end-start));

    }

    //查询 文档

    //查询所有的文档
    @Test
    public void MatchAllQuery() throws Exception {
        //1.创建查询对象 设置查询条件
        QueryBuilder query = QueryBuilders.matchAllQuery();//select *

        //2.执行查询
        SearchResponse response = transportClient
                .prepareSearch("blog03")//设置从哪一个索引中搜索
                .setTypes("article")//设置搜索的类型
                .setQuery(query)//设置查询条件
                .get();//执行
        //3.获取结果集
        SearchHits hits = response.getHits();
        System.out.println("根据条件命中的总记录数：" + hits.getTotalHits());//获取总结果数

        //4.遍历结果集 打印数据
        for (SearchHit hit : hits) {
            String articleJSON = hit.getSourceAsString();//相当于是一个一个的JSON的文档--->articlePOJO类型的JSON
            System.out.println(articleJSON);
            Article article = objectMapper.readValue(articleJSON, Article.class);
        }

    }

    //字符串查询 页面传递一个搜索的文本，代码接收文本 从es中获取数据返回
    //特点：只能搜索字符串  从字符串类型的字段中进行搜索 搜索的时候使用了分词器  分词器是用的是标准分词器。（使用较少）
    @Test
    public void queryString(){
        //1.创建查询对象 设置查询条件 执行查询
        SearchResponse searchResponse = transportClient
                .prepareSearch("blog03")
                .setTypes("article")
                //参数1 指定要搜素的内容
                //参数2 指定要搜索的字段 如果没有写从所有的字段中进行搜索（只能从字符串类型的字段中进行搜）
                .setQuery(QueryBuilders.queryStringQuery("很棒").field("id"))
                .get();
        //2.获取结果集
        SearchHits hits = searchResponse.getHits();
        System.out.println("根据条件命中的总记录数："+hits.getTotalHits());
        //3.打印
        for (SearchHit hit : hits) {
            System.out.println("搜索的到的数据："+hit.getSourceAsString());
        }
    }

    //匹配查询  特点：先进行分词 再进行匹配查询  再和合并返回数据  和创建倒排索引的时候的分词器保持一致
    @Test
    public void MatchQuery(){
        //1.创建查询对象 设置查询条件 执行查询
        SearchResponse searchResponse = transportClient
                .prepareSearch("blog03")
                .setTypes("article")
                //参数1 指定搜索的字段
                //参数2 指定要搜索的值
                .setQuery(QueryBuilders.matchQuery("title","华"))
                .get();
        //2.获取结果集
        SearchHits hits = searchResponse.getHits();
        System.out.println("根据条件命中的总记录数："+hits.getTotalHits());
        //3.打印
        for (SearchHit hit : hits) {
            System.out.println("搜索的到的数据："+hit.getSourceAsString());
        }
    }
    //多字段匹配查询 从多个字段中间分词再匹配搜索
    @Test
    public void MultiMatchQuery(){
        //1.创建查询对象 设置查询条件 执行查询
        SearchResponse searchResponse = transportClient
                .prepareSearch("blog03")
                .setTypes("article")
                //参数1 指定要搜索的值
                //参数2 指定从哪一些字段上进行搜索
                .setQuery(QueryBuilders.multiMatchQuery("华为","title","content"))
                .get();
        //2.获取结果集
        SearchHits hits = searchResponse.getHits();
        System.out.println("根据条件命中的总记录数："+hits.getTotalHits());
        //3.打印
        for (SearchHit hit : hits) {
            System.out.println("搜索的到的数据："+hit.getSourceAsString());
        }
    }



    //   * 表示任意字符  可以占用也可以不占字符空间 手机*
    //   ? 表示任意字符  占位符 占用一个字符空间  手机?

    //模糊搜索
    @Test
    public void wildcardQuery(){
        //1.创建查询对象 设置查询条件 执行查询
        SearchResponse searchResponse = transportClient
                .prepareSearch("blog03")
                .setTypes("article")
                //搜索手开头的数据
                .setQuery(QueryBuilders.wildcardQuery("title","手机?" ))
                .get();
        //2.获取结果集
        SearchHits hits = searchResponse.getHits();
        System.out.println("根据条件命中的总记录数："+hits.getTotalHits());
        //3.打印
        for (SearchHit hit : hits) {
            System.out.println("搜索的到的数据："+hit.getSourceAsString());
        }
    }
    //fuzzyQuery 相似度
    //将相似的单词进行匹配搜索 目前支持英文 2个字母
    @Test
    public void fuzzyQuery(){
        //1.创建查询对象 设置查询条件 执行查询
        SearchResponse searchResponse = transportClient
                .prepareSearch("blog03")
                .setTypes("article")
                //
                .setQuery(QueryBuilders.fuzzyQuery("title","eaaaaicsearch" ))
                .get();
        //2.获取结果集
        SearchHits hits = searchResponse.getHits();
        System.out.println("根据条件命中的总记录数："+hits.getTotalHits());
        //3.打印
        for (SearchHit hit : hits) {
            System.out.println("搜索的到的数据："+hit.getSourceAsString());
        }
    }

    //范围查询
    @Test
    public void rangQuery(){
        //1.创建查询对象 设置查询条件 执行查询
        SearchResponse searchResponse = transportClient
                .prepareSearch("blog03")
                .setTypes("article")
                //gte >=
                //lte <=

                // from true  >=
                // to  true <=
                .setQuery(QueryBuilders.rangeQuery("id").from(1,true).to(2,true))
                .get();
        //2.获取结果集
        SearchHits hits = searchResponse.getHits();
        System.out.println("根据条件命中的总记录数："+hits.getTotalHits());
        //3.打印
        for (SearchHit hit : hits) {
            System.out.println("搜索的到的数据："+hit.getSourceAsString());
        }
    }

    //词条查询：特点 不分词，整体进行匹配查询
    @Test
    public void termQuery(){
        //1.创建查询对象 设置查询条件 执行查询
        SearchResponse searchResponse = transportClient
                .prepareSearch("blog03")
                .setTypes("article")
                //参数1 指定要搜索的字段 参数2 指定要搜索的值 不分词进行匹配查询
                .setQuery(QueryBuilders.termQuery("title","华为手机"))
                .get();
        //2.获取结果集
        SearchHits hits = searchResponse.getHits();
        System.out.println("根据条件命中的总记录数："+hits.getTotalHits());
        //3.打印
        for (SearchHit hit : hits) {
            System.out.println("搜索的到的数据："+hit.getSourceAsString());
        }
    }

    //bool查询  多条件组合查询

    //需求1. term查询 title是华为的 数据 并且 id范围在0-10 的数据
    @Test
    public void boolQuery(){// {}  where not condit1  contion

        //1.创建子查询条件1
        TermQueryBuilder queryBuilder1 = QueryBuilders.termQuery("title", "华为");
        //2.创建子查询条件2
        RangeQueryBuilder queryBuilder2 = QueryBuilders.rangeQuery("id").gte(0).lte(10);
        //3.组合2个条件 再执行条件查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // MUST     必须要满足  相当于 AND
        // MUST_NOT 必须不满足  相当于NOT
        // SHOULD   应该满足   相当于 OR
        // filter  必须要满足  相当于AND
        boolQueryBuilder.filter(queryBuilder1);
        boolQueryBuilder.filter(queryBuilder2);


        //4.创建查询对象 设置查询条件 执行查询
        SearchResponse searchResponse = transportClient
                .prepareSearch("blog03")
                .setTypes("article")
                //组合条件查询
                .setQuery(boolQueryBuilder)
                .get();
        //5.获取结果集
        SearchHits hits = searchResponse.getHits();
        System.out.println("根据条件命中的总记录数："+hits.getTotalHits());
        //6.打印
        for (SearchHit hit : hits) {
            System.out.println("搜索的到的数据："+hit.getSourceAsString());
        }
    }

    //分页排序  limit 0,10 (0-->(page-1)*rows  10: rows)
    // 排序 order by id desc/asc
    @Test
    public void pageSortQuery(){
        //1.创建查询对象 设置查询条件 执行查询
        SearchResponse searchResponse = transportClient
                .prepareSearch("blog03")
                .setTypes("article")
                .setFrom(0)// page-1 * rows
                .setSize(10)// rows
                //指定排序的字段
                //指定排序的类型 ASC DESC
                .addSort("id", SortOrder.DESC)//默认是没有排序的
                .setQuery(QueryBuilders.termQuery("title","华为"))
                .get();
        //2.获取结果集
        SearchHits hits = searchResponse.getHits();
        System.out.println("根据条件命中的总记录数："+hits.getTotalHits());
        //3.打印
        for (SearchHit hit : hits) {
            System.out.println("搜索的到的数据："+hit.getSourceAsString());
        }
    }

    // 查询的时候就要高亮
    // 搜索的时候输入了一个 手机
    // 返回给页面 的时候  携带数据为：{“title”:“<em style="color:red">手机</em>华为<em style="color:red">手机</em>”}

    //高亮 根据查询的文本 进行分词  再进行匹配 匹配成功进行字符截取 进行拼接 前缀和后缀 返回给页面 （内部做的事情）

    //你需要指定 设置高亮的字段 以及设置前缀和后缀

    //你需要  获取高亮的数据 返回给页面

    @Test
    public void gaoliangQuery() throws IOException {

        //0  设置高亮的字段 设置前缀和后缀
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder
                .field("title")//高亮的字段
                .preTags("<span style=\"color:red\">")//前缀
                .postTags("</span>");

        //1.创建查询对象 设置查询条件 执行查询
        SearchResponse searchResponse = transportClient
                .prepareSearch("blog03")
                .setTypes("article")
                .setFrom(0)// page-1 * rows
                .setSize(10)// rows
                .addSort("id", SortOrder.DESC)//默认是没有排序的
                .highlighter(highlightBuilder)//设置高亮的条件
                .setQuery(QueryBuilders.matchQuery("title","华为手机"))
                .get();
        //2.获取结果集
        SearchHits hits = searchResponse.getHits();
        //3.打印
        for (SearchHit hit : hits) {
            //hit.getSourceAsString() 获取到的数据是没有高亮的
            System.out.println("搜索的到的数据："+hit.getSourceAsString());
            //获取高亮的数据
            //key 是高亮的字段
            //value 高亮的字段对应的数据值（高亮的内容）
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            // 这里面有<span>....</span>
            HighlightField highlightField = highlightFields.get("title");

            //高亮的碎片
            Text[] fragments = highlightField.getFragments();

            StringBuffer sb = new StringBuffer();
            for (Text fragment : fragments) {
                String gaoliang = fragment.string();
                sb.append(gaoliang);
            }
            String s = sb.toString();//高亮的数据 是title字段的高亮数据

            Article article = objectMapper.readValue(hit.getSourceAsString(), Article.class);

            article.setTitle(s);

            System.out.println(article.getId()+";"+article.getTitle()+"=========="+article.getContent());
        }
    }




}
