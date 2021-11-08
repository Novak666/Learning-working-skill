# 学习目标

+ Thymeleaf的介绍

+ Thymeleaf的入门

+ Thymeleaf的语法及标签

+ 商品详情页静态化工程搭建

+ 商品详情页静态化功能实现

  ```
  1.详情页静态化操作
  2.填充基础数据  Spu、List<Sku>
  3.规格切换
  ```

  搜索页面渲染

  ```
  1.数据展示
  2.搜索条件展示
  3.实现条件搜索控制
  ```

- 用户修改商品信息，同步更新创建商品详情页

# 1. Thymeleaf介绍

​	thymeleaf是一个XML/XHTML/HTML5模板引擎，可用于Web与非Web环境中的应用开发。它是一个开源的Java库，基于Apache License 2.0许可，由Daniel Fernández创建，该作者还是Java加密库Jasypt的作者。

Thymeleaf提供了一个用于整合Spring MVC的可选模块，在应用开发中，你可以使用Thymeleaf来完全代替JSP或其他模板引擎，如Velocity、FreeMarker等。Thymeleaf的主要目标在于提供一种可被浏览器正确显示的、格式良好的模板创建方式，因此也可以用作静态建模。你可以使用它创建经过验证的XML与HTML模板。相对于编写逻辑或代码，开发者只需将标签属性添加到模板中即可。接下来，这些标签属性就会在DOM（文档对象模型）上执行预先制定好的逻辑。

它的特点便是：开箱即用，Thymeleaf允许您处理六种模板，每种模板称为模板模式：

- XML
- 有效的XML
- XHTML
- 有效的XHTML
- HTML5
- 旧版HTML5

所有这些模式都指的是格式良好的XML文件，但*Legacy HTML5*模式除外，它允许您处理HTML5文件，其中包含独立（非关闭）标记，没有值的标记属性或不在引号之间写入的标记属性。为了在这种特定模式下处理文件，Thymeleaf将首先执行转换，将您的文件转换为格式良好的XML文件，这些文件仍然是完全有效的HTML5（实际上是创建HTML5代码的推荐方法）[1](https://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html#fn1)。

另请注意，验证仅适用于XML和XHTML模板。

然而，这些并不是Thymeleaf可以处理的唯一模板类型，并且用户始终能够通过指定在此模式下*解析*模板的方法和*编写*结果的方式来定义他/她自己的模式。这样，任何可以建模为DOM树（无论是否为XML）的东西都可以被Thymeleaf有效地作为模板处理。

<font color='red'>thymeleaf是spring开源的出来的一个基于模板生成静态数据的框架(jar包)，基于html，xml等作为模板来生成数据，并且可以代替jsp作为视图技术。也能在web环境和非web环境都可以生成其他的文本类型的数据</font>

# 2. Springboot整合thymeleaf

使用springboot 来集成使用Thymeleaf可以大大减少单纯使用thymleaf的代码量，所以我们接下来使用springboot集成使用thymeleaf.

实现的步骤为：

+ 创建一个sprinboot项目
+ 添加thymeleaf的起步依赖
+ 添加spring web的起步依赖
+ 编写html 使用thymleaf的语法获取变量对应后台传递的值
+ 编写controller 设置变量的值到model中

(1)创建工程

创建一个独立的工程springboot-thymeleaf，该工程为案例工程，不需要放到changgou-parent工程中。

**pom.xml依赖**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itheima</groupId>
    <artifactId>springboot-thymeleaf</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
    </parent>

    <dependencies>
        <!--web起步依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--thymeleaf配置-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
    </dependencies>
</project>
```

(2)创建html

在resources中创建templates目录，在templates目录创建 demo1.html,代码如下：

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Thymeleaf的入门</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<!--输出hello数据-->
<p th:text="${hello}"></p>
</body>
</html>
```

解释：

`<html xmlns:th="http://www.thymeleaf.org">`:这句声明使用thymeleaf标签

`<p th:text="${hello}"></p>`:这句使用 th:text="${变量名}" 表示 使用thymeleaf获取文本数据，类似于EL表达式。

(3)修改application.yml配置

创建application.yml,并设置thymeleaf的缓存设置，设置为false。默认加缓存的，用于测试。

```yaml
spring:
  thymeleaf:
    cache: false
```

在这里，其实还有一些默认配置，比如视图前缀：classpath:/templates/,视图后缀：.html

`org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties`部分源码如下：

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/1.png)

(4)控制层

创建controller用于测试后台 设置数据到model中。

创建com.itheima.controller.TestController，代码如下：

```java
@Controller
@RequestMapping("/test")
public class TestController {

    /***
     * 访问/test/hello  跳转到demo1页面
     * @param model
     * @return
     */
    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("hello","hello welcome");
        return "demo1";
    }
}
```

(5)测试

创建启动类`com.itheima.ThymeleafApplication`，代码如下：

```java
@SpringBootApplication
public class ThymeleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThymeleafApplication.class,args);
    }
}
```

启动系统，并在浏览器访问

```
http://localhost:8080/test/hello
```

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/2.png)

# 3. Thymeleaf基本语法

(1)th:href 后台代码

```
model.addAttribute("key1","value1");
model.addAttribute("url","/test/hello");
```

html:

```html
<a th:href="@{${url}(key1=${key1},key2=${key1})}">去黑马网站首页</a>
```

(2)th:each 遍历，功能类似jstl中的`<c:forEach>`标签。 

创建com.itheima.model.User,代码如下：

```java
public class User {
    private Integer id;
    private String name;
    private String address;
    //..get..set
}
```

Controller添加数据,List输出

```java
/***
 * 访问/test/hello  跳转到demo1页面
 * @param model
 * @return
 */
@RequestMapping("/hello")
public String hello(Model model){
    model.addAttribute("hello","hello welcome");

    //集合数据
    List<User> users = new ArrayList<User>();
    users.add(new User(1,"张三","深圳"));
    users.add(new User(2,"李四","北京"));
    users.add(new User(3,"王五","武汉"));
    model.addAttribute("users",users);
    return "demo1";
}
```

页面输出

```html
<table>
    <tr>
        <td>id</td>
        <td>姓名</td>
        <td>地址</td>
        <td>下标</td>
    </tr>
    <tr th:each="abc,mystat:${users}">
        <td th:text="${abc.id}"></td>
        <td th:text="${abc.name}">zhangsan</td>
        <td th:text="${abc.address}">深圳</td>
        <td th:text="${mystat.index+1}">深圳</td>
    </tr>
</table>
```

测试效果

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/3.png)

(3)Map输出 后台添加Map

```java
//Map定义
Map<String,Object> dataMap = new HashMap<String,Object>();
dataMap.put("No","123");
dataMap.put("address","深圳");
model.addAttribute("dataMap",dataMap);
```

页面输出

```html
<div th:each="map,mapStat:${dataMap}">
    <div th:text="${map}"></div>
    key:<span th:text="${mapStat.current.key}"></span><br/>
    value:<span th:text="${mapStat.current.value}"></span><br/>   
</div>
```

或者：

```html
<div th:each="entry:${dataMap}">
    <div th:text="${entry}"></div>
    key:<span th:text="${entry.key}"></span><br/>
    value:<span th:text="${entry.value}"></span><br/>   
</div>
```

测试效果

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/4.png)

(4)数组输出 后台添加数组

```java
//存储一个数组
String[] names = {"张三","李四","王五"};
model.addAttribute("names",names);
```

页面输出

```html
<div th:each="nm,nmStat:${names}">
    <span th:text="${nmStat.count}"></span><span th:text="${nm}"></span>
    ==============================================
</div>
```

测试效果

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/5.png)

(5)Date输出

后台添加日期

```java
//日期
model.addAttribute("now",new Date());
```

页面输出

```php+HTML
<div>
    <span th:text="${#dates.format(now,'yyyy-MM-dd hh:ss:mm')}"></span>
</div>
```

测试效果

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/6.png)

(6)th:if条件  

```java
//if条件
model.addAttribute("age",22);
```

页面输出

```html
<div>
    <span th:if="${(age>=18)}">终于长大了！</span>
</div>
```

测试效果

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/7.png)

(7)使用javascript

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/8.png)

java代码为:

 ![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/9.png)

(8) 字符拼接使用||

后台代码:

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/10.png)

模板:

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/11.png)

# 4. 搜索页面渲染

## 4.1 搜索分析

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/12.png)

搜索页面要显示的内容主要分为3块。

1)搜索的数据结果

2)筛选出的数据搜索条件

3)用户已经勾选的数据条件

## 4.2 搜索实现

![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/13.png)

1. 用户发送请求到聚合微服务(商品搜索相关的)
2. 聚合微服务，通过feign调用其他的基础微服务(搜索微服务，推荐微服务)
3. 聚合微服务获取到数据(推荐的数据，搜索的数据，菜单的数据....)进行组装
4. 返回给thymeleaf进行数据渲染，直接返回给用户的已经有数据和样式的页面

### 4.2.1 搜索工程搭建

(1)工程创建

在changgou-web工程中创建changgou-web-search工程,并在changgou-web的pom.xml中引入如下依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <!--feign-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>  
</dependencies>
```

(2)静态资源导入

将资源中的`页面/前端页面/search.html`拷贝到工程的`resources/templates`目录下,js、css等拷贝到`static`目录下，如下图：

![14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/14.png)

(3)Feign创建

修改changgou-service-search-api，添加`com.changgou.search.feign.SkuFeign`，实现调用搜索，代码如下：

```java
@FeignClient(name="search",path = "/search")
//@RequestMapping("/search") == path="/search"
public interface SkuFeign {
    @PostMapping
    public Map search(@RequestBody(required = false) Map<String, String> searchMap);
}
```

![15](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/15.png)

(4)changgou-web-search的pom.xml依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>changgou-web</artifactId>
        <groupId>com.changgou</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>changgou-web-search</artifactId>

    <dependencies>
        <!--search API依赖-->
        <dependency>
            <groupId>com.changgou</groupId>
            <artifactId>changgou-service-search-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

(5)搜索调用，在changgou-web-search中创建com.changgou.search.controller.SkuController,实现调用搜索，代码如下：

```java
@Controller
@RequestMapping(value = "/search")
public class SkuController {

    @Autowired
    private SkuFeign skuFeign;

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/list")
    public String search(@RequestParam(required = false) Map<String,String> searchMap, Model model){
        //调用changgou-service-search微服务
        Map resultMap = skuFeign.search(searchMap);
        model.addAttribute("result",resultMap);
        return "earch";
    }
}
```

(6)启动类创建

修改changgou-web-search,添加启动类com.changgou.SearchWebApplication，代码如下：

```java
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.changgou.search.feign")
public class SearchWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchWebApplication.class,args);
    }
}
```

(7)application.yml配置文件

```properties
server:
  port: 18088
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:7001/eureka
  instance:
    prefer-ip-address: true
feign:
  hystrix:
    enabled: true
spring:
  thymeleaf:
    cache: false
  application:
    name: search-web
  main:
    allow-bean-definition-overriding: true
```

(8)项目完整结构

![16](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/16.png)

copy 静态资源到static下：并且修改路径：

将./ 改成 /

![17](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/17.png)

在search.html的头部引入thymeleaf标签

```xml
<html xmlns:th="http://www.thymeleaf.org">
```

测试：`http://localhost:18088/search/list`,效果如下：

![18](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/18.png)

### 4.2.2 搜索数据填充分析

后端搜索到数据后，前端页面进行数据显示，显示的数据分为3部分

```
1)搜索的数据结果
2)筛选出的数据搜索条件
3)用户已经勾选的数据条件
```

### 4.2.3 关键字搜索

用户每次输入关键字的时候，直接根据关键字搜索，关键字搜索的数据会存储到`result.rows`中，页面每次根据result获取rows，然后循环输出即可,同时页面的搜索框每次需要回显搜索的关键词。

实现思路

```
1.前端表单提交搜索的关键词
2.后端根据关键词进行搜索
3.将搜索条件存储到Model中
4.页面循环迭代输出数据
5.搜索表单回显搜索的关键词
```

(1)后台搜索实现

修改SkuController的search方法，代码如下：

```java
/**
 * 搜索
 * @param searchMap
 * @return
 */
@GetMapping(value = "/list")
public String search(@RequestParam(required = false) Map<String,String> searchMap, Model model){
    //调用changgou-service-search微服务
    Map<String,Object> resultMap = skuFeign.search(searchMap);
    //搜索数据结果
    model.addAttribute("result",resultMap);
    //搜索条件
    model.addAttribute("searchMap",searchMap);
    return "search";
}
```

(2)页面搜索实现修改search.html（表单修改）

![19](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/19.png)

注意：搜索按钮为submit提交。

(3)页面结果输出 修改search.html，代码如下：

![20](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/20.png)

```html
<div class="goods-list">
    <ul class="yui3-g">
        <li class="yui3-u-1-5" th:each="sku:${result.rows}">
            <div class="list-wrap">
                <div class="p-img">
                    <a href="item.html"  target="_blank"><img th:src="${sku.image}" /></a>
                </div>
                <div class="price">
                    <strong>
                        <em>¥</em>
                        <i th:text="${sku.price}">6088.00</i>
                    </strong>
                </div>
                <div class="attr">
                    <!--js注入 html的攻击-->
                    <a target="_blank" href="item.html" th:utext="${sku.name}" >
                        Apple苹果iPhone 6s (A1699)Apple苹果iPhone 6s (A1699)Apple苹果iPhone 6s (A1699)Apple苹果iPhone 6s (A1699)
                    </a>
                </div>
                <div class="commit">
                    <i class="command">已有<span>2000</span>人评价</i>
                </div>
                <div class="operate">
                    <a href="success-cart.html" target="_blank" class="sui-btn btn-bordered btn-danger">加入购物车</a>
                    <a href="javascript:void(0);" class="sui-btn btn-bordered">收藏</a>
                </div>
            </div>
        </li>

    </ul>
</div>
```

```properties
解释：
	utext="${sku.name}"  获取数据 并直接解析html标签
	text="${sku.name}"   获取数据 只当做文本进行输出
```

(4)测试 搜索`华为`关键字,效果如下：

![21](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/21.png)

## 4.3 搜索条件回显

如下图效果：

![22](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/22.png)

搜索条件除了关键字外，还有分类、品牌、以及规格，这些在我们前面已经将数据存入到了Map中，我们可以直接从Map中将数据取出，然后在页面输出即可。

分类：`result.categoryList`

品牌：`result.brandList`

规格：`result.specMap`

### 4.3.1 回显分类列表

修改search.html的条件显示部分：

![23](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/23.png)

上图代码如下：

```html
<div class="type-wrap" th:if="not ${#maps.containsKey(searchMap,'category')}">
    <div class="fl key">商品分类</div>
    <div class="fl value" th:each="category:${result.categoryList}">
        <a th:text="${category}" ></a>
    </div>

    <div class="fl ext"></div>
</div>
```

### 4.3.2 回显品牌列表

![24](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/24.png)

代码如下：

```html
<div class="type-wrap logo" th:unless="${#maps.containsKey(searchMap,'brand')}">
    <div class="fl key brand">品牌</div>
    <div class="value logos">
        <ul class="logo-list">
            <li th:each="brand:${result.brandList}">
                <a  th:text="${brand}"></a>
            </li>
        </ul>
    </div>
    <div class="ext">
        <a href="javascript:void(0);" class="sui-btn">多选</a>
        <a href="javascript:void(0);">更多</a>
    </div>
</div>
```

### 4.3.3 回显规格列表

![25](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/25.png)

代码如下：

```html
<div class="type-wrap" th:each="spec:${result.specMap}" th:if="not ${#maps.containsKey(searchMap,'spec_'+spec.key)}">
    <div class="fl key" th:text="${spec.key}">网络制式</div>
    <div class="fl value">
        <ul class="type-list">
            <li th:each="op:${spec.value}">
                <a th:text="${op}">GSM（移动/联通2G）</a>
            </li>
        </ul>
    </div>
    <div class="fl ext"></div>
</div>
```

3个回显的整体代码如下：代码起始位置在502行左右

```html
<div class="type-wrap" th:if="not ${#maps.containsKey(searchMap,'category')}">
    <div class="fl key">商品分类</div>
    <div class="fl value" th:each="category:${result.categoryList}">
        <a th:text="${category}"></a>
    </div>
    <div class="fl ext"></div>
</div>
<div class="type-wrap logo" th:unless="${#maps.containsKey(searchMap,'brand')}">
    <div class="fl key brand">品牌</div>
    <div class="value logos">
        <ul class="logo-list">
            <li th:each="brand:${result.brandList}">
                <a th:text="${brand}"></a>
            </li>
        </ul>
    </div>
    <div class="ext">
        <a href="javascript:void(0);" class="sui-btn">多选</a>
        <a href="javascript:void(0);">更多</a>
    </div>
</div>
<div class="type-wrap" th:each="spec:${result.specMap}" th:if="not ${#maps.containsKey(searchMap,'spec_'+spec.key)}">
    <div class="fl key" th:text="${spec.key}">网络制式</div>
    <div class="fl value">
        <ul class="type-list">
            <li th:each="op:${spec.value}">
                <a th:text="${op}">GSM（移动/联通2G）</a>
            </li>
        </ul>
    </div>
    <div class="fl ext"></div>
</div>
```

## 4.4 条件搜索实现

![26](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/26.png)

用户每次点击搜索的时候，其实在上次搜索的基础之上加上了新的搜索条件，也就是在上一次请求的URL后面追加了新的搜索条件，我们可以在后台每次拼接组装出上次搜索的URL，然后每次将URL存入到Model中，页面每次点击不同条件的时候，从Model中取出上次请求的URL，然后再加上新点击的条件参数实现跳转即可。

(1)后台记录搜索URL

修改SkuController，添加组装URL的方法，并将组装好的URL存储起来,代码如下：

![27](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/27.png)

(2)页面搜索对接

![28](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/28.png)以html方式打开，并拖拽图片到另外一个标签页中查看；

th:href 这里是超链接的语法，例如：`th:href="@{${url}(price='500-1000')}"`表示请求地址是取`url`参数的值，同时向后台传递参数price的值为500-100元。

整体代码如下：

```html
<div class="clearfix selector">
    <div class="type-wrap" th:if="not ${#maps.containsKey(searchMap,'category')}">
        <div class="fl key">商品分类</div>
        <div class="fl value" th:each="category:${result.categoryList}">
            <a th:text="${category}" th:href="@{${url}(category=${category})}"></a>
        </div>
        <div class="fl ext"></div>
    </div>
    <div class="type-wrap logo" th:unless="${#maps.containsKey(searchMap,'brand')}">
        <div class="fl key brand">品牌</div>
        <div class="value logos">
            <ul class="logo-list">
                <li th:each="brand:${result.brandList}">
                    <a th:href="@{${url}(brand=${brand})}" th:text="${brand}"></a>
                </li>
            </ul>
        </div>
        <div class="ext">
            <a href="javascript:void(0);" class="sui-btn">多选</a>
            <a href="javascript:void(0);">更多</a>
        </div>
    </div>
    <div class="type-wrap" th:each="spec:${result.specMap}" th:if="not ${#maps.containsKey(searchMap,'spec_'+spec.key)}">
        <div class="fl key" th:text="${spec.key}">网络制式</div>
        <div class="fl value">
            <ul class="type-list">
                <li th:each="op:${spec.value}">
                    <a th:href="@{${url}('spec_'+${spec.key}=${op})}" th:text="${op}">GSM（移动/联通2G）</a>
                </li>
            </ul>
        </div>
        <div class="fl ext"></div>
    </div>

    <div class="type-wrap" th:if="not ${#maps.containsKey(searchMap,'price')}">
        <div class="fl key">价格</div>
        <div class="fl value">
            <ul class="type-list">
                <li>
                    <a th:href="@{${url}(price='0-500')}">0-500元</a>
                </li>
                <li>
                    <a th:href="@{${url}(price='500-1000')}">500-1000元</a>
                </li>
                <li>
                    <a th:href="@{${url}(price='1000-1500')}">1000-1500元</a>
                </li>
                <li>
                    <a th:href="@{${url}(price='1500-2000')}">1500-2000元</a>
                </li>
                <li>
                    <a th:href="@{${url}(price='2000-3000')}">2000-3000元 </a>
                </li>
                <li>
                    <a th:href="@{${url}(price='3000-*')}">3000元以上</a>
                </li>
            </ul>
        </div>
        <div class="fl ext">
        </div>
    </div>
    <div class="type-wrap">
        <div class="fl key">更多筛选项</div>
        <div class="fl value">
            <ul class="type-list">
                <li>
                    <a>特点</a>
                </li>
                <li>
                    <a>系统</a>
                </li>
                <li>
                    <a>手机内存 </a>
                </li>
                <li>
                    <a>单卡双卡</a>
                </li>
                <li>
                    <a>其他</a>
                </li>
            </ul>
        </div>
        <div class="fl ext">
        </div>
    </div>
</div>
```

## 4.5 移除搜索条件

![29](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/29.png)

如上图，用户点击条件搜索后，要将选中的条件显示出来，并提供移除条件的`x`按钮,显示条件我们可以从searchMap中获取，移除其实就是将之前的请求地址中的指定条件删除即可。

```
思路：
1.显示条件实现
2.移除条件实现
```

(1)条件显示和移除修改search.html，代码如下：

![30](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/30.png)

代码如下：

```html
<ul class="fl sui-tag">
    <li class="with-x" th:if="${#maps.containsKey(searchMap,'category')}">
        <span>商品分类：</span>
        <span th:text="${searchMap.category}">手机</span>
        <a th:href="@{${#strings.replace(url,'category='+searchMap.category,'')}}">×</a>
    </li>
    <li class="with-x" th:if="${#maps.containsKey(searchMap,'brand')}">
        <span>商品品牌：</span>
        <span th:text="${searchMap.brand}">华为</span>
        <a th:href="@{${#strings.replace(url,'brand='+searchMap.brand,'')}}">×</a>
    </li>
    <li class="with-x" th:each="spec:${searchMap}" th:if="${#strings.startsWith(spec.key,'spec_')}">
        <!--spec_测试-->
        <span th:text="${#strings.substring(spec.key,5)}">网络制式</span>
        :
        <span th:text="${spec.value}">电信2G</span>
        <a th:href="@{${#strings.replace(url,spec.key+'='+spec.value,'')}}">×</a>
    </li>
</ul>
```

解释：

```properties
${#strings.startsWith(spec.key,'spec_')}:表示以spec_开始的key
${#strings.replace(url,'spec_','')}:表示将url中的spec_替换成空
```

## 4.6 排序(作业)

![31](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/31.png)

上图代码是排序代码，需要2个属性，`sortRule`:排序规则，ASC或者DESC，`sortField`:排序的域，前端每次只需要将这2个域的值传入到后台即可实现排序。

(1)后台组装排序URL

每次排序的时候恢复第1页查询，所以url地址我们需要重新拼接,每次切换排序的时候，不需要之前的排序信息，修改SkuController,代码如下：

![32](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/32.png)

代码如下:

```java
private String url(Map<String, String> searchMap) {// { spec_网络:"移动4G","keywords":"华为"}
    String url = "/search/list"; // a/b?id=1&
    if (searchMap != null) {
        url += "?";
        for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
            //如果是排序 则 跳过 拼接排序的地址 因为有数据
            if(stringStringEntry.getKey().equals("sortField") || stringStringEntry.getKey().equals("sortRule")){
                continue;
            }
            url += stringStringEntry.getKey() + "=" + stringStringEntry.getValue() + "&";

        }
        if(url.lastIndexOf("&")!=-1)
            url = url.substring(0, url.lastIndexOf("&"));
    }
    return url;
}
```

(2)前端排序实现

修改search.html，实现排序，代码如下：

 ![33](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/33.png)

这一块我们实现了价格排序，同学们课后去实现以下销量和新品排序。

## 4.7 分页

真实的分页应该像百度那样，如下图：

![34](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/34.png)

![35](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/35.png)

![36](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/36.png)

(1)分页工具类定义

在comm工程中添加Page分页对象，代码如下：

```java
public class Page <T> implements Serializable{

	// 页数（第几页）
	private long currentpage;

	// 查询数据库里面对应的数据有多少条
	private long total;// 从数据库查处的总记录数

	// 每页查5条
	private int size;

	// 下页
	private int next;
	
	private List<T> list;

	// 最后一页
	private int last;
	
	private int lpage;
	
	private int rpage;
	
	//从哪条开始查
	private long start;
	
	//全局偏移量
	public int offsize = 2;
	
	public Page() {
		super();
	}

	/****
	 *
	 * @param currentpage
	 * @param total
	 * @param pagesize
	 */
	public void setCurrentpage(long currentpage,long total,long pagesize) {
		//可以整除的情况下
		long pagecount =  total/pagesize;

		//如果整除表示正好分N页，如果不能整除在N页的基础上+1页
		int totalPages = (int) (total%pagesize==0? total/pagesize : (total/pagesize)+1);

		//总页数
		this.last = totalPages;

		//判断当前页是否越界,如果越界，我们就查最后一页
		if(currentpage>totalPages){
			this.currentpage = totalPages;
		}else{
			this.currentpage=currentpage;
		}

		//计算start
		this.start = (this.currentpage-1)*pagesize;
	}

	//上一页
	public long getUpper() {
		return currentpage>1? currentpage-1: currentpage;
	}

	//总共有多少页，即末页
	public void setLast(int last) {
		this.last = (int) (total%size==0? total/size : (total/size)+1);
	}

	/****
	 * 带有偏移量设置的分页
	 * @param total
	 * @param currentpage
	 * @param pagesize
	 * @param offsize
	 */
	public Page(long total,int currentpage,int pagesize,int offsize) {
		this.offsize = offsize;
		initPage(total, currentpage, pagesize);
	}

	/****
	 *
	 * @param total   总记录数
	 * @param currentpage	当前页
	 * @param pagesize	每页显示多少条
	 */
	public Page(long total,int currentpage,int pagesize) {
		initPage(total,currentpage,pagesize);
	}

	/****
	 * 初始化分页
	 * @param total
	 * @param currentpage
	 * @param pagesize
	 */
	public void initPage(long total,int currentpage,int pagesize){
		//总记录数
		this.total = total;
		//每页显示多少条
		this.size=pagesize;

		//计算当前页和数据库查询起始值以及总页数
		setCurrentpage(currentpage, total, pagesize);

		//分页计算
		int leftcount =this.offsize,	//需要向上一页执行多少次
				rightcount =this.offsize;

		//起点页
		this.lpage =currentpage;
		//结束页
		this.rpage =currentpage;

		//2点判断
		this.lpage = currentpage-leftcount;			//正常情况下的起点
		this.rpage = currentpage+rightcount;		//正常情况下的终点

		//页差=总页数和结束页的差
		int topdiv = this.last-rpage;				//判断是否大于最大页数

		/***
		 * 起点页
		 * 1、页差<0  起点页=起点页+页差值
		 * 2、页差>=0 起点和终点判断
		 */
		this.lpage=topdiv<0? this.lpage+topdiv:this.lpage;

		/***
		 * 结束页
		 * 1、起点页<=0   结束页=|起点页|+1
		 * 2、起点页>0    结束页
		 */
		this.rpage=this.lpage<=0? this.rpage+(this.lpage*-1)+1: this.rpage;

		/***
		 * 当起点页<=0  让起点页为第一页
		 * 否则不管
		 */
		this.lpage=this.lpage<=0? 1:this.lpage;

		/***
		 * 如果结束页>总页数   结束页=总页数
		 * 否则不管
		 */
		this.rpage=this.rpage>last? this.last:this.rpage;
	}

	public long getNext() {
		return  currentpage<last? currentpage+1: last;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public long getCurrentpage() {
		return currentpage;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getLast() {
		return last;
	}

	public long getLpage() {
		return lpage;
	}

	public void setLpage(int lpage) {
		this.lpage = lpage;
	}

	public long getRpage() {
		return rpage;
	}

	public void setRpage(int rpage) {
		this.rpage = rpage;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setCurrentpage(long currentpage) {
		this.currentpage = currentpage;
	}

	/**
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<T> list) {
		this.list = list;
	}
}
```

(2)分页实现

由于这里需要获取分页信息，我们可以在`changgou-service-search`服务中修改搜索方法实现获取分页数据，修改`com.changgou.search.service.impl.SkuServiceImpl`的search方法，在return之前添加如下方法获取份额与数据：

 ![37](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/37.png)

![38](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/38.png)

```java
//分页数据保存
//设置当前页码
resultMap.put("pageNum", pageNum);
resultMap.put("pageSize", pageSize);
```

修改SkuController,实现分页信息封装，代码如下：

![39](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/39.png)

(3)页面分页实现 修改search.html，实现分页查询，代码如下：

![40](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/40.png)

注意：每次如果搜条件发生变化都要从第1页查询，而点击下一页的时候，分页数据在页面给出，不需要在后台拼接的url中给出，所以在拼接url的时候，需要过滤掉分页参数，修改`changgou-web-search`的控制层`com.changgou.search.controller.SkuController`的url拼接方法，代码如下：

![41](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/41.png)

其中代码如下：

```java
private String url(Map<String, String> searchMap) {
    String url = "/search/list";
    if (searchMap != null && searchMap.size() > 0) {
        url += "?";
        for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
            String key = stringStringEntry.getKey();//keywords
            String value = stringStringEntry.getValue();// 华为
            if (key.equals("pageNum")) {
                //如果遇到了特殊的key 不需要拼接 比如：pageNum
                continue;
            }
            url += key + "=" + value + "&";

        }
        url = url.substring(0, url.length() - 1);
    }
    return url;
}
```

# 5. 畅购商品详情页(作业)

## 5.1 需求分析

当系统审核完成商品，需要将商品详情页进行展示，那么采用静态页面生成的方式生成，并部署到高性能的web服务器中进行访问是比较合适的。所以，开发流程如下图所示：

![42](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/42.png)

执行步骤解释：

+ 系统管理员（商家运维人员）修改或者审核商品的时候，会触发canal监控数据
+ canal微服务获取修改数据后，调用静态页微服务的方法进行生成静态页 
+ 静态页微服务只负责使用thymeleaf的模板技术生成静态页

## 5.2 商品静态化微服务创建

### 5.2.1 需求分析

该微服务只用于生成商品静态页，不做其他事情。

### 5.2.2 搭建项目

（1）在changgou-web下创建一个名称为changgou-web-item的模块,如图：

![43](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/43.png)

（2）changgou-web-item中添加起步依赖，如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>changgou-web</artifactId>
        <groupId>com.changgou</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>changgou-web-item</artifactId>

    <dependencies>
        <!--api 模块-->
        <dependency>
            <groupId>com.changgou</groupId>
            <artifactId>changgou-service-goods-api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

（3）修改application.yml的配置

```yaml
server:
  port: 18085
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:7001/eureka
  instance:
    prefer-ip-address: true
feign:
  hystrix:
    enabled: true
spring:
  thymeleaf:
    cache: false
  application:
    name: item
  main:
    allow-bean-definition-overriding: true

  #rabbitmq:
  #  host: 192.168.25.138
# 生成静态页的位置
pagepath: D:/project/workspace_changgou/changgou/changgou-parent/changgou-web/changgou-web-item/src/main/resources/templates/items
```

（4）创建系统启动类

```java
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.changgou.goods.feign")
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class,args);
    }
}
```

## 5.3 生成静态页

### 5.3.1 需求分析

页面发送请求，传递要生成的静态页的的商品的SpuID.后台controller 接收请求，调用thyemleaf的原生API生成商品静态页。

![44](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/44.png)

上图是要生成的商品详情页，从图片上可以看出需要查询SPU的3个分类作为面包屑显示，同时还需要查询SKU和SPU信息。

### 5.3.2 Feign创建

一会儿需要查询SPU和SKU以及Category，所以我们需要先创建Feign，修改changgou-service-goods-api,添加CategoryFeign，并在CategoryFeign中添加根据ID查询分类数据，代码如下：

```java
/**
 * 获取分类的对象信息
 * @param id
 * @return
 */
@GetMapping("/{id}")
public Result<Category> findById(@PathVariable(name = "id") Integer id);
```

在changgou-service-goods-api,添加SkuFeign,并添加根据SpuID查询Sku集合，代码如下：

```java
/**
 * 根据条件搜索
 * @param sku
 * @return
 */
@PostMapping(value = "/search" )
public Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);
```

在changgou-service-goods-api,添加SpuFeign,并添加根据SpuID查询Spu信息，代码如下：

```java
/***
 * 根据SpuID查询Spu信息
 * @param id
 * @return
 */
@GetMapping("/{id}")
public Result<Spu> findById(@PathVariable(name = "id") Long id);
```

### 5.3.3 静态页生成代码

(1)创建Controller

在changgou-web-item中创建com.changgou.item.controller.PageController用于接收请求，测试生成静态页

```java
@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 生成静态页面
     * @param id
     * @return
     */
    @RequestMapping("/createHtml/{id}")
    public Result createHtml(@PathVariable(name="id") Long id){
        pageService.createPageHtml(id);
        return new Result(true, StatusCode.OK,"ok");
    }
}
```

(2)创建service

接口：

```java
public interface PageService {
    /**
     * 根据商品的ID 生成静态页
     * @param spuId
     */
    public void createPageHtml(Long spuId) ;
}
```

实现类：

![45](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/45.png)

上图代码如下：

```java
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private TemplateEngine templateEngine;

    //生成静态文件路径
    @Value("${pagepath}")
    private String pagepath;

    /**
     * 构建数据模型
     * @param spuId
     * @return
     */
    private Map<String,Object> buildDataModel(Long spuId){
        //构建数据模型
        Map<String,Object> dataMap = new HashMap<>();
        //获取spu 和SKU列表
        Result<Spu> result = spuFeign.findById(spuId);
        Spu spu = result.getData();

        //获取分类信息
        dataMap.put("category1",categoryFeign.findById(spu.getCategory1Id()).getData());
        dataMap.put("category2",categoryFeign.findById(spu.getCategory2Id()).getData());
        dataMap.put("category3",categoryFeign.findById(spu.getCategory3Id()).getData());
        if(spu.getImages()!=null) {
            dataMap.put("imageList", spu.getImages().split(","));
        }

        dataMap.put("specificationList",JSON.parseObject(spu.getSpecItems(),Map.class));
        dataMap.put("spu",spu);

        //根据spuId查询Sku集合
        Sku skuCondition = new Sku();
        skuCondition.setSpuId(spu.getId());
        Result<List<Sku>> resultSku = skuFeign.findList(skuCondition);
        dataMap.put("skuList",resultSku.getData());
        return dataMap;
    }

    /***
     * 生成静态页
     * @param spuId
     */
    @Override
    public void createPageHtml(Long spuId) {
        // 1.上下文
        Context context = new Context();
        Map<String, Object> dataModel = buildDataModel(spuId);
        context.setVariables(dataModel);
        // 2.准备文件
        File dir = new File(pagepath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(dir, spuId + ".html");
        // 3.生成页面
        try (PrintWriter writer = new PrintWriter(dest, "UTF-8")) {
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 5.3.4 模板填充

(1)面包屑数据

修改item.html，填充三个分类数据作为面包屑，代码如下：

![46](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/46.png)

(2)商品图片

修改item.html，将商品图片信息输出，在真实工作中需要做空判断，代码如下：

![47](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/47.png)

(3)规格输出

![48](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/48.png)

(4)默认SKU显示

静态页生成后，需要显示默认的Sku，我们这里默认显示第1个Sku即可，这里可以结合着Vue一起实现。可以先定义一个集合，再定义一个spec和sku，用来存储当前选中的Sku信息和Sku的规格，代码如下：

![49](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/49.png)

页面显示默认的Sku信息

![50](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/50.png)

(5)记录选中的Sku

在当前Spu的所有Sku中spec值是唯一的，我们可以根据spec来判断用户选中的是哪个Sku，我们可以在Vue中添加代码来实现，代码如下：

![51](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/51.png)

添加规格点击事件

![52](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/52.png)

(6)样式切换

点击不同规格后，实现样式选中，我们可以根据每个规格判断该规格是否在当前选中的Sku规格中，如果在，则返回true添加selected样式，否则返回false不添加selected样式。

Vue添加代码：

![53](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/53.png)

页面添加样式绑定，代码如下：

![54](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/54.png)

### 5.3.5 静态资源过滤

生成的静态页我们可以先放到changgou-web-item工程中，后面项目实战的时候可以挪出来放到Nginx指定发布目录。一会儿我们将生成的静态页放到resources/templates/items目录下,所以请求该目录下的静态页需要直接到该目录查找即可。

我们创建一个EnableMvcConfig类，开启静态资源过滤，代码如下：

```java
@ControllerAdvice
@Configuration
public class EnableMvcConfig implements WebMvcConfigurer{

    /***
     * 静态资源放行
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/items/**").addResourceLocations("classpath:/templates/items/");
    }
}
```

### 5.3.6 启动测试

启动eurekea服务端

启动商品微服务

启动静态化微服务 changgou-web-item

将静态资源导入到changgou-web-item中，如下图：

![55](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/55.png)

生成静态页地址 `http://localhost:18085/page/createHtml/1087918019151269888`

![56](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/56.png)

静态页生成后访问地址 `http://localhost:18085/items/1087918019151269888.html`

![57](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/57.png)

# 6. canal监听生成静态页

![42](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/42.png)

如上图详情页的解决方案. 监听到数据的变化,直接调用feign 生成静态页即可.

## 6.1 需求分析

当商品微服务审核商品之后，应当发送消息，这里采用了Canal监控数据变化，数据变化后，调用feign实现生成静态页

## 6.2 Feign创建

在changgou-service-api中创建changgou-web-item-api，该工程中主要创建changgou-web-item的对外依赖抽取信息。

(1)Feign创建

在changgou-web-item-api中创建com.changgou.item.feign.PageFeign,代码如下：

```java
@FeignClient(name="item")
@RequestMapping("/page")
public interface PageFeign {

    /***
     * 根据SpuID生成静态页
     * @param id
     * @return
     */
    @RequestMapping("/createHtml/{id}")
    Result createHtml(@PathVariable(name="id") Long id);
}
```

(2)pom.xml依赖

修改changgou-service-canal工程的pom.xml，引入如下依赖：

```xml
<!--静态页API 服务-->
<dependency>
    <groupId>com.changgou</groupId>
    <artifactId>changgou-web-item-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

(3)修改changgou-service-canal工程中的启动类

![58](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/58.png)

```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@EnableCanalClient // 启用canal
@EnableFeignClients(basePackages = {"com.changgou.content.feign","com.changgou.item.feign"})
public class CanalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanalApplication.class, args);
    }
}
```

## 6.3 canal监听数据变化

监听类中,监听商品数据库的tb_spu的数据变化,当数据变化的时候生成静态页或者删除静态页

在原来的监听类中添加如下代码即可,

```java
@Autowired
private PageFeign pageFeign;

@ListenPoint(destination = "example",
        schema = "changgou_goods",
        table = {"tb_spu"},
        eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.INSERT, CanalEntry.EventType.DELETE})
public void onEventCustomSpu(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {

    //判断操作类型
    if (eventType == CanalEntry.EventType.DELETE) {
        String spuId = "";
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        for (CanalEntry.Column column : beforeColumnsList) {
            if (column.getName().equals("id")) {
                spuId = column.getValue();//spuid
                break;
            }
        }
        //todo 删除静态页

    }else{
        //新增 或者 更新
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        String spuId = "";
        for (CanalEntry.Column column : afterColumnsList) {
            if (column.getName().equals("id")) {
                spuId = column.getValue();
                break;
            }
        }
        //更新 生成静态页
        pageFeign.createHtml(Long.valueOf(spuId));
    }
}
```

整体的页面代码如下图所示已经实现：

 ![59](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/59.png)

# 7. 总结

 ![60](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.11.07/pics/60.png)

