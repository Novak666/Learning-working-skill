# 1. Elasticsearch编程操作

## 1.1 索引

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class EsApplicationTest03 {

    @Autowired
    private TransportClient transportClient;

    @Autowired
    private ObjectMapper objectMapper;

}
```

### 1.1.1 创建索引

```java
@Test
    public void onlyCreateIndex() {
        transportClient.admin().indices().prepareCreate("blog03").get();
    }
```

### 1.1.2 删除索引

```java
@Test
public void deleteIndex(){
    transportClient.admin().indices().prepareDelete("blog03").get();
}
```

## 1.2 映射

```java
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
```

## 1.3 文档

### 1.3.1 创建文档

2种方式，ObjctMapper和xcontentBuidler

```java
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
```

### 1.3.2 修改文档

修改文档和新增文档一样。当存在相同的文档的唯一ID的时候，便是更新

### 1.3.3 删除文档

```java
//删除文档
@Test
public void deleteByDocument() {
    transportClient.prepareDelete("blog02", "article", "2").get();
}
```

### 1.3.4 批量导入数据

效率高

```java
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
```

### 1.3.5 查询所有

```java
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
```

### 1.3.6 字符串查询

1. 只能查询字符串，从字符串类型字段进行查询
2. 不指定字段查询的时候，先进行使用默认的标准分词器进行分词，再进行查询(中文中使用不多)
3. 如果指定了字段，则使用之前的映射设置的分词器来进行分词，当然也可以指定分词器

```java
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
```

### 1.3.7 匹配查询

```java
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
```

### 1.3.8 多字段查询

```java
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
```

### 1.3.9 模糊查询

```java
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
```

### 1.3.10 相似度查询

```java
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
```

### 1.3.11 范围查询

```java
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
```

### 1.3.12 词条查询

```java
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
```

### 1.3.13 布尔查询(多条件查询)

```java
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
```

+ MUST必须满足某条件，但是需要查询和计算文档的匹配度的分数，速度要慢

+ FILTER必须满足某条件，但是不需要计算匹配度分数，那么优化查询效率，方便缓存

### 1.3.14 分页和排序

```java
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
```

### 1.3.15 高亮数据

```java
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
```

# 2. Spring Data Elasticsearch

## 2.1 简介

Spring Data是一个用于简化数据库访问，并支持云服务的开源框架。其主要目标是使得对数据的访问变得方便快捷，并支持map-reduce框架和云计算数据服务。 Spring Data可以极大的简化JPA的写法，可以在几乎不用写实现的情况下，实现对数据的访问和操作。除了CRUD外，还包括如分页、排序等一些常用的功能

Spring Data的官网：http://projects.spring.io/spring-data/

Spring Data Elasticsearch基于spring data API简化 Elasticsearch操作，将原始操作Elasticsearch的客户端Java API 进行封装 。Spring Data为Elasticsearch项目提供集成搜索引擎。Spring Data Elasticsearch POJO的关键功能区域为中心的模型与Elastichsearch交互文档和轻松地编写一个存储库数据访问层。官方网站：http://projects.spring.io/spring-data-elasticsearch/

SpringBoot集成spring data elasticsearch的方式来开发更加的方便和快捷

## 2.2 简单案例

1. 起步依赖
2. 配置文件配置连接服务器
3. 创建POJO，建立映射关系，@document、@field、@id
4. 创建dao接口继承elasticsearchRepository<Article,Long>
5. 根据需要执行不同的dao的方法

```java
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
```

```java
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
```

```java
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
```

