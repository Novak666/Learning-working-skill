# 1. 数据源的自动配置

## 1.1 导入JDBC

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
```

数据库驱动

```xml
<!--默认版本：-->
<mysql.version>8.0.22</mysql.version>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <!--<version>5.1.49</version>-->
</dependency>

<!--
想要修改版本
1、直接依赖引入具体版本（maven的就近依赖原则）
2、重新声明版本（maven的属性的就近优先原则）
-->
<properties>
    <java.version>1.8</java.version>
    <mysql.version>5.1.49</mysql.version>
</properties>
```

## 1.2 自动配置的类

- DataSourceAutoConfiguration ： 数据源的自动配置
  + 修改数据源相关的配置：**spring.datasource**
  + **数据库连接池的配置，是自己容器中没有DataSource才自动配置的**(源码)
  + 底层配置好的连接池是：**HikariDataSource**

```java
@Configuration(proxyBeanMethods = false)

@Conditional(PooledDataSourceCondition.class)
@ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
@Import({ DataSourceConfiguration.Hikari.class, DataSourceConfiguration.Tomcat.class,
DataSourceConfiguration.Dbcp2.class, DataSourceConfiguration.OracleUcp.class,
DataSourceConfiguration.Generic.class, DataSourceJmxConfiguration.class })
protected static class PooledDataSourceConfiguration
```

- DataSourceTransactionManagerAutoConfiguration： 事务管理器的自动配置
- JdbcTemplateAutoConfiguration： **JdbcTemplate的自动配置，可以来对数据库进行crud**
  + 可以修改这个配置项@ConfigurationProperties(prefix = **"spring.jdbc"**)  来修改JdbcTemplate
  + @Bean@Primary JdbcTemplate 容器中有这个组件

- JndiDataSourceAutoConfiguration： jndi的自动配置
- XADataSourceAutoConfiguration： 分布式事务相关的

## 1.3 修改配置项

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
```

## 1.4 测试

```java
@Slf4j
@SpringBootTest
class Boot05WebAdminApplicationTests {

   @Autowired
   JdbcTemplate jdbcTemplate;

   @Test
   void contextLoads() {
//        jdbcTemplate.queryForObject("select * from account_tbl")
//        jdbcTemplate.queryForList("select * from account_tbl",)
      Long aLong = jdbcTemplate.queryForObject("select count(*) from account", Long.class);
      log.info("记录总数：{}",aLong);

//    log.info("数据源类型：{}",dataSource.getClass());
   }

}
```

# 2. Druid数据源

druid官方github地址：https://github.com/alibaba/druid

## 2.1 自定义方式

依赖

```xml
<dependency>
   <groupId>com.alibaba</groupId>
   <artifactId>druid</artifactId>
   <version>1.1.17</version>
</dependency>
```

测试

```java
@Configuration
public class MyDataSourceConfig {

    // 默认的自动配置是判断容器中没有才会配@ConditionalOnMissingBean(DataSource.class)
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();

//        druidDataSource.setUrl();
//        druidDataSource.setUsername();
//        druidDataSource.setPassword();
        return druidDataSource;
    }
}
```

更多配置

1. Druid内置提供了一个`StatViewServlet`用于展示Druid的统计信息。[官方文档 - 配置_StatViewServlet配置](https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_StatViewServlet%E9%85%8D%E7%BD%AE)。这个`StatViewServlet`的用途包括
   + 提供监控信息展示的html页面
   + 提供监控信息的JSON API

2. `WebStatFilter`用于采集web-jdbc关联监控的数据，如SQL监控、URI监控
3. Druid提供了`WallFilter`，它是基于SQL语义分析来实现防御SQL注入攻击的

```java
/**
 * 配置 druid的监控页功能
 * @return
 */
@Bean
public ServletRegistrationBean statViewServlet(){
    StatViewServlet statViewServlet = new StatViewServlet();
    ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(statViewServlet, "/druid/*");

    registrationBean.addInitParameter("loginUsername","admin");
    registrationBean.addInitParameter("loginPassword","123456");


    return registrationBean;
}
```

```java
/**
 * WebStatFilter 用于采集web-jdbc关联监控的数据。
 */
@Bean
public FilterRegistrationBean webStatFilter(){
    WebStatFilter webStatFilter = new WebStatFilter();

    FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(webStatFilter);
    filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
    filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

    return filterRegistrationBean;
}
```

## 2.2 官方starter方式

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.17</version>
</dependency>
```

- 扩展配置项 `spring.datasource.druid`
- 自动配置类`DruidDataSourceAutoConfigure`
- `DruidSpringAopConfiguration.class`, 监控SpringBean的；配置项：`spring.datasource.druid.aop-patterns`
- `DruidStatViewServletConfiguration.class`监控页的配置，`spring.datasource.druid.stat-view-servlet`默认开启
- `DruidWebStatFilterConfiguration.class`，web监控配置，`spring.datasource.druid.web-stat-filter`默认开启
- `DruidFilterConfiguration.class`所有Druid的filter的配置

```java
private static final String FILTER_STAT_PREFIX = "spring.datasource.druid.filter.stat";
private static final String FILTER_CONFIG_PREFIX = "spring.datasource.druid.filter.config";
private static final String FILTER_ENCODING_PREFIX = "spring.datasource.druid.filter.encoding";
private static final String FILTER_SLF4J_PREFIX = "spring.datasource.druid.filter.slf4j";
private static final String FILTER_LOG4J_PREFIX = "spring.datasource.druid.filter.log4j";
private static final String FILTER_LOG4J2_PREFIX = "spring.datasource.druid.filter.log4j2";
private static final String FILTER_COMMONS_LOG_PREFIX = "spring.datasource.druid.filter.commons-log";
private static final String FILTER_WALL_PREFIX = "spring.datasource.druid.filter.wall";
```

配置示例

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
    
    druid:
          aop-patterns: com.atguigu.admin.*  #springbean监控
          filters: stat,wall,slf4j  #所有开启的功能
    
          stat-view-servlet:  #监控页配置
            enabled: true
            login-username: admin
            login-password: admin
            resetEnable: false
    
          web-stat-filter:  #web监控
            enabled: true
            urlPattern: /*
            exclusions: '*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*'
    
    
          filter:
            stat: #sql监控
              slow-sql-millis: 1000
              logSlowSql: true
              enabled: true
            wall: #防火墙
              enabled: true
              config:
                drop-table-allow: false
```

# 3. 整合MyBatis

## 3.1 配置文件方式

能在配置文件中操作，背后还是源码决定的configuration(EnableConfigurationProperties)

## 3.2 注解方式

## 3.3 混合方式

参考之前

# 4. 整合MyBatisPlus

[MyBatis-Plus](https://github.com/baomidou/mybatis-plus)（简称 MP）是一个 [MyBatis](http://www.mybatis.org/mybatis-3/)的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生

[IDEA的MyBatis的插件 - MyBatisX](https://plugins.jetbrains.com/plugin/10119-mybatisx)

[MyBatisPlus官方文档](https://baomidou.com/guide/)

添加依赖

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.1</version>
</dependency>
```

+ `MybatisPlusAutoConfiguration`配置类，`MybatisPlusProperties`配置项绑定
+ `SqlSessionFactory`自动配置好，底层是容器中默认的数据源
+ `mapperLocations`自动配置好的，有默认值`classpath*:/mapper/**/*.xml`，这表示任意包的类路径下的所有mapper文件夹下任意路径下的所有xml都是sql映射文件。 建议以后sql映射文件放在 mapper下
+ 容器中也自动配置好了`SqlSessionTemplate`
+ `@Mapper` 标注的接口也会被自动扫描，建议直接 `@MapperScan("com.lun.boot.mapper")`批量扫描
+ MyBatisPlus**优点**之一：只需要我们的Mapper继承MyBatisPlus的`BaseMapper` 就可以拥有CRUD能力，减轻开发工作

```java
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lun.hellomybatisplus.model.User;

public interface UserMapper extends BaseMapper<User> {

}
```

<font color='red'>暂时跳过</font>

# 5. Redis

Redis 是一个开源（BSD许可）的，内存中的数据结构存储系统，它可以用作数据库、**缓存**和消息中间件。 它支持多种类型的数据结构，如 [字符串（strings）](http://www.redis.cn/topics/data-types-intro.html#strings)， [散列（hashes）](http://www.redis.cn/topics/data-types-intro.html#hashes)， [列表（lists）](http://www.redis.cn/topics/data-types-intro.html#lists)， [集合（sets）](http://www.redis.cn/topics/data-types-intro.html#sets)， [有序集合（sorted sets）](http://www.redis.cn/topics/data-types-intro.html#sorted-sets) 与范围查询， [bitmaps](http://www.redis.cn/topics/data-types-intro.html#bitmaps)， [hyperloglogs](http://www.redis.cn/topics/data-types-intro.html#hyperloglogs) 和 [地理空间（geospatial）](http://www.redis.cn/commands/geoadd.html) 索引半径查询。 Redis 内置了 [复制（replication）](http://www.redis.cn/topics/replication.html)，[LUA脚本（Lua scripting）](http://www.redis.cn/commands/eval.html)， [LRU驱动事件（LRU eviction）](http://www.redis.cn/topics/lru-cache.html)，[事务（transactions）](http://www.redis.cn/topics/transactions.html) 和不同级别的 [磁盘持久化（persistence）](http://www.redis.cn/topics/persistence.html)， 并通过 [Redis哨兵（Sentinel）](http://www.redis.cn/topics/sentinel.html)和自动 [分区（Cluster）](http://www.redis.cn/topics/cluster-tutorial.html)提供高可用性（high availability）

## 5.1 简单案例

添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!--导入jedis-->
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
```

自动配置：

+ `RedisAutoConfiguration`自动配置类，RedisProperties 属性类 --> spring.redis.xxx是对redis的配置
+ 连接工厂`LettuceConnectionConfiguration`、`JedisConnectionConfiguration`是准备好的
+ 自动注入了`RedisTemplate<Object, Object>`，`xxxTemplate`
+ 自动注入了`StringRedisTemplate`，key，value都是String
+ 底层只要我们使用`StringRedisTemplate`、`RedisTemplate`就可以操作Redis

外网Redis环境搭建：

1. 阿里云按量付费Redis，其中选择经典网络
2. 申请Redis的公网连接地址
3. 修改白名单，允许`0.0.0.0/0`访问

Redis配置：

```yaml
spring:
	redis:
    host: r-bp1nc7reqesxisgxpipd.redis.rds.aliyuncs.com
    port: 6379
    password: lfy:Lfy123456
    client-type: jedis
    jedis:
      pool:
        max-active: 10
  #    url: redis://lfy:Lfy123456@r-bp1nc7reqesxisgxpipd.redis.rds.aliyuncs.com:6379
#    lettuce:
#      pool:
#        max-active: 10
#        min-idle: 5
```

测试代码：

~~~java
@SpringBootTest
public class Boot05WebAdminApplicationTests {

```
@Autowired
StringRedisTemplate redisTemplate;
```

```
@Autowired
RedisConnectionFactory redisConnectionFactory;

@Test
void testRedis(){
    ValueOperations<String, String> operations = redisTemplate.opsForValue();

    operations.set("hello","world");

    String hello = operations.get("hello");
    System.out.println(hello);

    System.out.println(redisConnectionFactory.getClass());
}
```

}
~~~

## 5.2 统计案例

需求：统计访问页面的次数，保存在Redis中

URL统计拦截器：

~~~java
@Component
public class RedisUrlCountInterceptor implements HandlerInterceptor {

```
@Autowired
StringRedisTemplate redisTemplate;

@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String uri = request.getRequestURI();

    //默认每次访问当前uri就会计数+1
    redisTemplate.opsForValue().increment(uri);

    return true;
}
```

}
~~~

注册配置URL统计拦截器：

~~~java
@Configuration
public class AdminWebConfig implements WebMvcConfigurer{

```
@Autowired
RedisUrlCountInterceptor redisUrlCountInterceptor;
```

```
@Override
public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(redisUrlCountInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**",
                    "/js/**","/aa/**");
}
```

}
~~~

调用Redis内的统计数据：

~~~java
@Slf4j
@Controller
public class IndexController {

```
@Autowired
StringRedisTemplate redisTemplate;

@GetMapping("/main.html")
public String mainPage(HttpSession session,Model model){

    log.info("当前方法是：{}","mainPage");

    ValueOperations<String, String> opsForValue =
            redisTemplate.opsForValue();

    String s = opsForValue.get("/main.html");
    String s1 = opsForValue.get("/sql");

    model.addAttribute("mainCount",s);
    model.addAttribute("sqlCount",s1);

    return "main";
}
```

}
~~~

Filter、Interceptor 几乎拥有相同的功能？

- Filter是Servlet定义的原生组件，它的好处是脱离Spring应用也能使用
- Interceptor是Spring定义的接口，可以使用Spring的自动装配等功能