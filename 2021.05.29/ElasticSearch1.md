# 1. Elasticsearch简介

## 1.1 搜索中的问题

1. 传统的方式进行搜索的时候速度慢
2. 搜索的精度不高

## 1.2 倒排索引

以关键词搜索文章为例子

传统的方式：先找文档，再找内容，再匹配，速度慢

倒排索引：通过关键词找文档，直接找数据，速度快

搜索步骤：

1. 需要建立倒排的流程

2. 根据倒排查询数据流程

## 1.3 Elasticsearch

Elasticsearch是一个基于Apache Lucene(TM)的开源搜索引擎，无论在开源还是专有领域，Lucene可以被认为是迄今为止最先进、性能最好的、功能最全的搜索引擎库。但是，Lucene只是一个库。想要发挥其强大的作用，你需使用Java并要将其集成到你的应用中。Lucene非常复杂，你需要深入的了解检索相关知识来理解它是如何工作的。 
Elasticsearch也是使用Java编写并使用Lucene来建立索引并实现搜索功能，但是它的目的是通过简单连贯的RESTful API让全文搜索变得简单并隐藏Lucene的复杂性。 
然而，Elasticsearch不仅仅是Lucene和全文搜索引擎，它还提供

+ 分布式的实时文件存储，每个字段都被索引并可被搜索

+ 实时分析的分布式搜索引擎

- 可以扩展到上百台服务器，处理PB级结构化或非结构化数据

# 2. Elasticsearch的安装与启动

## 2.1 下载压缩包

Elasticsearch分为Linux和Window版本，基于我们主要学习的是Elasticsearch的Java客户端的使用，所以我们课程中使用的是安装较为简便的Window版本。项目上线后，公司的运维人员会安装Linux版的ES供我们连接使用

Elasticsearch的官方地址：https://www.elastic.co/products/Elasticsearch

## 2.2 安装ES

Window版的Elasticsearch的安装很简单，类似Window版的Tomcat，解压开即安装完毕

## 2.3 启动ES

bin目录下点击Elasticsearch.bat

9300是tcp通讯端口，集群间和TCPClient都执行该端口，可供java程序调用

9200是http协议的RESTful接口

通过浏览器访问Elasticsearch服务器，看到返回的<font color='red'>json</font>信息，代表服务启动成功

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.29/pics/1.png)

## 2.4 注意事项

1. Elasticsearch5是使用java开发的，且本版本的es需要的jdk版本要是1.8以上，所以安装Elasticsearch之前保证JDK1.8+安装完毕，并正确的配置好JDK环境变量，否则启动Elasticsearch失败
2. 出现闪退，通过路径访问发现“空间不足”，修改解压目录下的/config/jvm.options文件

# 3. 安装ES图形化界面插件

## 3.1 安装nodejs

下载nodejs：https://nodejs.org/en/download/

双击msi文件，下一步即可。安装成功后，执行cmd命令查看版本：

```shell
node -v
```

## 3.2 安装ES head插件

1. 解压Elasticsearch-head-master.rar
2. cd到目录Elasticsearch-head-master中执行命令

```shell
npm run start
```

3. 修改Elasticsearch/config下的配置文件：Elasticsearch.yml，增加以下配置：

```yaml
http.cors.enabled: true
http.cors.allow-origin: "*"
network.host: 127.0.0.1
```

注意有空格

4. 重启ES服务器，再执行命令

```shell
npm run start
```

# 4. Elasticsearch相关概念

## 4.1 简介

Elasticsearch是面向文档(document oriented)的，这意味着它可以存储整个对象或文档(document)。然而它不仅仅是存储(store)，还会索引(index)每个文档的内容使之可以被搜索。在Elasticsearch中，你可以对文档(而非成行成列的数据)进行索引、搜索、排序、过滤。Elasticsearch比较传统关系型数据库如下：

| Elasticsearch | Relational DB |
| :-----------: | :-----------: |
|     索引      |    数据库     |
|     类型      |      表       |
|     文档      |      行       |
|     字段      |      列       |
|     映射      |     约束      |

## 4.2 核心概念

### 4.2.1 索引-index

一个索引就是一个拥有几分相似特征的文档的集合。比如说，你可以有一个客户数据的索引，另一个产品目录的索引，还有一个订单数据的索引。一个索引由一个名字来标识(必须全部是小写字母的)，并且当我们要对对应于这个索引中的文档进行索引、搜索、更新和删除的时候，都要使用到这个名字。在一个集群中，可以定义任意多的索引

### 4.2.2 类型-type

在一个索引中，你可以定义一种或多种类型。一个类型是你的索引的一个逻辑上的分类/分区，其语义完全由你来定。通常，会为具有一组共同字段的文档定义一个类型。比如说，我们假设你运营一个博客平台并且将你所有的数据存储到一个索引中。在这个索引中，你可以为用户数据定义一个类型，为博客数据定义另一个类型，当然，也可以为评论数据定义另一个类型

### 4.2.3 文档-document

一个文档是一个可被索引的基础信息单元。比如，你可以拥有某一个客户的文档，某一个产品的一个文档，当然，也可以拥有某个订单的一个文档。文档以JSON(Javascript Object Notation)格式来表示，而JSON是一个到处存在的互联网数据交互格式

注意：每一篇文档注意在进行创建的时候都会进行打分。用于进行排名，打分的公式实际上用的就是lucece的打分公式

### 4.2.4 字段-field

相当于是数据表的字段，对文档数据根据不同属性进行的分类标识

### 4.2.5 映射-mapping

mapping是处理数据的方式和规则方面做一些限制，如某个字段的数据类型、默认值、分词器、是否被索引等等，这些都是映射里面可以设置的，其它就是处理ES里面数据的一些使用规则。映射按着最优规则处理数据对性能提高很大，因此需要思考如何建立映射才能对性能更好

映射对应的字段field有四个重要属性：数据类型、是否分词、是否存储、是否索引。类似于指定数据库中的表中的列的一些约束，需要根据不同的应用场景来决定使用哪种数据类型和属性值

+ 数据类型：定义了该字段field的数据存储的方式，有基本数据类型，字符串类型以及复杂的数据类型
+ 是否分词：定义了该字段的值是否要被索引，分词的目的就是为了要建立倒排索引的结构
+ 是否索引：定义了该字段是否要被搜索，要索引的目的就是为了要方便搜索
+ 是否存储：定义了是否该存储该数据到底层的lucene中，默认是不存储的。存储不存储看页面是否需要展示

首先介绍一下自动映射，默认情况下：

是否索引：是

是否分词：是，使用标准分词器

数据类型：根据Java类型自动转换

<font color='red'>是否存储(lucene)：否，不存储，ES默认存储到_source中，如果查询的字段数据量很大，而且只需要查询一个字段，可以设置为true，即存储到lucene。</font>

### 4.2.6 接近实时-NRT

Elasticsearch是一个接近实时的搜索平台。这意味着，从索引一个文档直到这个文档能够被搜索到有一个轻微的延迟(通常是1秒以内)

### 4.2.7 集群-cluster

一个集群就是由一个或多个节点组织在一起，它们共同持有整个的数据，并一起提供索引和搜索功能。一个集群由一个唯一的名字标识，这个名字默认就是“Elasticsearch”。这个名字是很重要的，因为一个节点只能通过指定某个集群的名字，来加入这个集群

### 4.2.8 节点-node

一个节点是集群中的一个服务器，作为集群的一部分，它存储数据，参与集群的索引和搜索功能。和集群类似，一个节点也是由一个名字来标识的，默认情况下，这个名字是一个随机的角色的名字，这个名字会在启动的时候赋予节点。这个名字对于管理工作来说挺重要的，因为在这个管理过程中，你会去确定网络中的哪些服务器对应于Elasticsearch集群中的哪些节点

一个节点可以通过配置集群名称的方式来加入一个指定的集群。默认情况下，每个节点都会被安排加入到一个叫做“Elasticsearch”的集群中，这意味着，如果你在你的网络中启动了若干个节点，并假定它们能够相互发现彼此，它们将会自动地形成并加入到一个叫做“Elasticsearch”的集群中

在一个集群里，可以拥有任意多个节点。而且，如果当前你的网络中没有运行任何Elasticsearch节点，这时启动一个节点，会默认创建并加入一个叫做“Elasticsearch”的集群

### 4.2.9 分片和复制-shards&replicas

一个索引可以存储超出单个节点硬件限制的大量数据。比如，一个具有10亿文档的索引占据1TB的磁盘空间，而任一节点都没有这样大的磁盘空间；或者单个节点处理搜索请求，响应太慢。为了解决这个问题，Elasticsearch提供了将索引划分成多份的能力，这些份就叫做分片。当你创建一个索引的时候，你可以指定你想要的分片的数量。每个分片本身也是一个功能完善并且独立的“索引”，这个“索引”可以被放置到集群中的任何节点上。分片很重要，主要有两方面的原因： 1. 允许你水平分割/扩展你的内容容量。2.允许你在分片（潜在地，位于多个节点上）之上进行分布式的、并行的操作，进而提高性能/吞吐量

至于一个分片怎样分布，它的文档怎样聚合回搜索请求，是完全由Elasticsearch管理的，对于作为用户的你来说，这些都是透明的

在一个网络/云的环境里，失败随时都可能发生，在某个分片/节点不知怎么的就处于离线状态，或者由于任何原因消失了，这种情况下，有一个故障转移机制是非常有用并且是强烈推荐的。为此目的，Elasticsearch允许你创建分片的一份或多份拷贝，这些拷贝叫做复制分片，或者直接叫复制

复制之所以重要，有两个主要原因： 在分片/节点失败的情况下，提供了高可用性。因为这个原因，注意到复制分片从不与原/主要(original/primary)分片置于同一节点上是非常重要的。扩展你的搜索量/吞吐量，因为搜索可以在所有的复制上并行运行。总之，每个索引可以被分成多个分片。一个索引也可以被复制0次(意思是没有复制)或多次。一旦复制了，每个索引就有了主分片(作为复制源的原来的分片)和复制分片(主分片的拷贝)之别。分片和复制的数量可以在索引创建的时候指定。在索引创建之后，你可以在任何时候动态地改变复制的数量，但是你事后不能改变分片的数量

默认情况下，Elasticsearch中的每个索引被分片5个主分片和1个复制，这意味着，如果你的集群中至少有两个节点，你的索引将会有5个主分片和另外5个复制分片(1个完全拷贝)，这样的话每个索引总共就有10个分片

# 5. 操作Elasticsearch

操作ES就相当于对操作数据库，我们应该知道针对数据库我们有不同的CRUD功能，那么同样的道理，操作elasticsearch也能有CRUD功能，只不过叫法不一样。有两种方式，如下：

- 使用Elasticsearch提供的restfull风格的API实现操作ES(查看另外一个world文档)
- 使用elasticsearch提供的java Client API来实现操作ES 又有许多方式，我们这里主要使用springboot集成spring data elasticsearch来实现
  - java API 使用官方提供的TransportClient(暂时演示使用官方)
  - java API 使用 REST clients
  - java api 使用  Jest
  - java api 使用 spring data elasticsearch(后续推荐使用)

## 5.1 环境搭建

1. 创建工程，添加起步依赖spring data elasticsearch

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itheima</groupId>
    <artifactId>itheima-springboot-es</artifactId>
    <version>1.0-SNAPSHOT</version>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

2. 配置文件

```yaml
spring:
  data:
    elasticsearch:
      cluster-name: elasticsearch # 集群的名称和es集群名匹配
      cluster-nodes: 127.0.0.1:9300 # 节点的地址（ip：port）
```

3. 启动类

```java
@SpringBootApplication
public class EsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class,args);
    }
}
```

## 5.2 新建索引和添加文档

1. 创建POJO，用于存储数据转成JSON

```java
public class Article {
    private Long id;
    private String title;
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

2. 

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class EsTest {

    @Autowired
    private TransportClient transportClient;//操作链接到es服务端的客户端API
    @Autowired
    private ObjectMapper objectMapper;//json的转换器

    //insert  //update

    //创建索引  创建类型  创建文档  能自动的进行映射  (创建博客系统 blog01,article类型，docuemnt数据（JSON）)

    /**
     * {
     * "id":1,
     * "title":"震惊！最近航母开到美国了"
     * "content":"标题党数据"
     * <p>
     * }
     */
    @Test
    public void create() throws Exception {

        //restTemplate

        Article article = new Article(2L, "震惊一点不震惊！最近航母开到美国了", "标题党数据");

        String jsonstr = objectMapper.writeValueAsString(article);
        //参数1 指定索引名
        //参数2 指定类型名称
        //参数3 指定文档的唯一标识
        IndexResponse indexResponse = transportClient.prepareIndex("blog02", "article", "1")
                //参数1 指定文档的数据是一个JSON字符串
                //参数2 指定数据类型 JSON数据类型
                .setSource(jsonstr, XContentType.JSON)
                //指定动作 将数据存储到es服务器中
                .get();
        System.out.println(indexResponse.getIndex() + ":" + indexResponse.getVersion() + ":" + indexResponse.getType());
    }

}
```

## 5.3 根据ID查询文档

```java
//select 数据

@Test
public void select() {
    GetResponse docuemntResponse = transportClient.prepareGet("blog02", "article", "1").get();
    Map<String, Object> sourceAsMap = docuemntResponse.getSourceAsMap();
    String sourceAsString = docuemntResponse.getSourceAsString();
    System.out.println(sourceAsMap);
    System.out.println(sourceAsString);
}
```

## 5.4 根据ID删除文档

```java
//delete
@Test
public void delete() {
    //删除文档ID为1的数据
    transportClient.prepareDelete("blog02", "article", "1").get();
}
```

# 6. 分词器

## 6.1 简介

我们刚才在进行说明倒排索引的时候说过，在进行数据存储的时候，需要先进行分词。而分词指的就是按照一定的规则将词一个个切割。这个规则是有内部的分词器机制来决定的，不同的分词器就是不同的规则

- standard分词器
- ik分词器
- stop分词器
- 其他的分词器

## 6.2 IK分词器

### 6.2.1 简介

IK分词是一款国人开发的相对简单的中文分词器。虽然开发者自2012年之后就不在维护了，但在工程应用中IK算是比较流行的一款

特点：

1. 分词效果优秀
2. 能将原本不是词的变成一个词
3. 能将原本是一个词的进行停用，这些词我们称为停用词。停用词：单独运用没有具体语言意义的词汇，可根据语义自己定义

### 6.2.2 IK分词器安装

下载地址：https://github.com/medcl/elasticsearch-analysis-ik/releases

这里将已经提供的elasticsearch-analysis-ik-5.6.8.zip解压，将解压后的elasticsearch重新命名为ik(任意)，将文件夹拷贝到elasticsearch/plugins目录下，重新启动elasticsearch，即可加载IK分词器

### 6.2.3 IK分词器使用

IK提供了两个分词算法ik_smart和ik_max_word，其中ik_smart为最少切分(智能切分)，ik_max_word为最细粒度划分

测试案例：

+ http://127.0.0.1:9200/_analyze?analyzer=ik_smart&pretty=true&text=我是程序员

+ http://127.0.0.1:9200/_analyze?analyzer=ik_max_word&pretty=true&text=我是程序员

### 6.2.4 添加自定义词

添加一个新的词便于分词，需要自定义扩展

1. 进入elasticsearch/plugins/ik/config目录
2. 新建一个my.dic文件(文件名任意)，特别注意编辑内容(以utf8无bom保存，如果不行加一些换行)
3. 修改IKAnalyzer.cfg.xml(在ik/config目录下)

```xml
<properties>
    <comment>IK Analyzer 扩展配置</comment>
    <!‐‐用户可以在这里配置自己的扩展字典 ‐‐>
    <entry key="ext_dict">my.dic</entry>
    <!‐‐用户可以在这里配置自己的扩展停止词字典‐‐>
    <entry key="ext_stopwords"></entry>
</properties>
```

停用词和上面步骤类似，配置完成重启ES服务器即可，在浏览器输入内容查看效果http://127.0.0.1:9200/_analyze?analyzer=ik_smart&pretty=true&text=我是传智播客的学生

# 7. 总结

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.29/pics/2.png)