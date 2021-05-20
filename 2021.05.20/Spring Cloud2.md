# 1. 声明式服务调用Feign

## 1.1 简介

+ Feign是一个声明式的REST客户端，它用了基于接口的注解方式，很方便实现客户端配置
+ Feign最初由Netflix公司提供，但不支持SpringMVC注解，后由SpringCloud对其封装，支持了SpringMVC注解，让使用者更易于接受

## 1.2 简单案例

1. 在消费端引入open-feign依赖
2. 编写Feign调用接口
3. 在启动类添加@EnableFeignClients注解，开启Feign功能
4. 测试调用

1. 在消费端引入open-feign依赖

```xml
<!--feign-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

2. 编写Feign调用接口

```java
/**
 *
 * feign声明式接口。发起远程调用的。
 *
 String url = "http://FEIGN-PROVIDER/goods/findOne/"+id;
 Goods goods = restTemplate.getForObject(url, Goods.class);
 *
 * 1. 定义接口
 * 2. 接口上添加注解 @FeignClient,设置value属性为服务提供者的应用名称
 * 3. 编写调用接口，接口的声明规则和提供方接口保持一致。
 * 4. 注入该接口对象，调用接口方法完成远程调用
 *
 */

@FeignClient(value = "FEIGN-PROVIDER",configuration = FeignLogConfig.class)
public interface GoodsFeignClient {

    @GetMapping("/goods/findOne/{id}")
    public Goods findGoodsById(@PathVariable("id") int id);

}
```

3. 在启动类添加@EnableFeignClients注解，开启Feign功能

```java
@EnableDiscoveryClient // 激活DiscoveryClient
@EnableEurekaClient
@SpringBootApplication

@EnableFeignClients //开启Feign的功能
public class ConsumerApp {


    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class,args);
    }
}
```

4. 测试调用

```java
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GoodsFeignClient goodsFeignClient;

    @GetMapping("/goods/{id}")
    public Goods findGoodsById(@PathVariable("id") int id){

        /*
        String url = "http://FEIGN-PROVIDER/goods/findOne/"+id;
        // 3. 调用方法
        Goods goods = restTemplate.getForObject(url, Goods.class);

        return goods;*/

        Goods goods = goodsFeignClient.findGoodsById(id);

        return goods;
    }


}
```

注意：IDEA会提示找不到GoodsFeignClient类型的Bean，忽略，内部会自动生成

## 1.3 超时配置

+ Feign底层依赖于Ribbon实现负载均衡和远程调用
+ Ribbon默认1秒超时
+ 超时配置

```yaml
# 设置Ribbon的超时时间
ribbon:
  ConnectTimeout: 1000 # 连接超时时间 默认1s
  ReadTimeout: 3000 # 逻辑处理的超时时间 默认1s
```

## 1.4 日志记录

1. 在消费端配置文件设置

```yaml
# 设置当前的日志级别 debug，feign只支持记录debug级别的日志
logging:
  level:
    com.itheima: debug
```

2. 定义Feign日志级别Bean

```java
@Configuration
public class FeignLogConfig {
    
    /*
        NONE,不记录
        BASIC,记录基本的请求行，响应状态码数据
        HEADERS,记录基本的请求行，响应状态码数据，记录响应头信息
        FULL;记录完成的请求 响应数据
     */

    @Bean
    public Logger.Level level(){
        return Logger.Level.FULL;
    }
}
```

3. 启用该Bean

```java
@FeignClient(value = "FEIGN-PROVIDER",configuration = FeignLogConfig.class)
```

# 2. 熔断器Hystrix

## 2.1 简介

+ Hystix是Netflix开源的一个延迟和容错库，用于隔离访问远程服务、第三方库，防止出现级联失败(雪崩)
+ 雪崩：一个服务失败，导致整条链路的服务都失败的情形

## 2.2 主要功能

+ 隔离

  + 线程池隔离

  ![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.20/pics/1.png)

  + 信号量隔离

  ![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.20/pics/2.png)

+ 降级

  当发生异常和超时，服务的提供方和消费方都应该有降级方案，返回默认数据

  ![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.20/pics/3.png)

+ 熔断

+ 限流

## 2.3 Hystrix降级-服务提供方

1. 在服务提供方，引入hystrix依赖
2. 定义降级方法，使用@HystrixCommand注解配置降级方法
3. 在启动类上开启Hystrix功能的注解，@EnableCircuitBreaker

1. 在服务提供方，引入hystrix依赖

```xml
<!-- hystrix -->
 <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
 </dependency>
```

2. 定义降级方法，使用@HystrixCommand注解配置降级方法

```java
/**
 * Goods Controller 服务提供方
 */

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Value("${server.port}")
    private int port;

    /**
     * 降级：
     *  1. 出现异常
     *  2. 服务调用超时
     *      * 默认1s超时
     *
     *  @HystrixCommand(fallbackMethod = "findOne_fallback")
     *      fallbackMethod：指定降级后调用的方法名称
     */

    @GetMapping("/findOne/{id}")
    @HystrixCommand(fallbackMethod = "findOne_fallback",commandProperties = {
            //设置Hystrix的超时时间，默认1s
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "3000")

    })
    public Goods findOne(@PathVariable("id") int id){

        //1.造个异常
        int i = 3/0;
        try {
            //2. 休眠2秒
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Goods goods = goodsService.findOne(id);

        goods.setTitle(goods.getTitle() + ":" + port);//将端口号，设置到了 商品标题上
        return goods;
    }


    /**
     * 定义降级方法：
     *  1. 方法的返回值需要和原方法一样
     *  2. 方法的参数需要和原方法一样
     */
    public Goods findOne_fallback(int id){
        Goods goods = new Goods();
        goods.setTitle("降级了~~~");

        return goods;
    }

}
```

3. 在启动类上开启Hystrix功能的注解，@EnableCircuitBreaker

```java
@EnableEurekaClient //该注解 在新版本中可以省略
@SpringBootApplication

@EnableCircuitBreaker // 开启Hystrix功能
public class ProviderApp {
    
    public static void main(String[] args) {
        SpringApplication.run(ProviderApp.class,args);
    }
}
```

## 2.4 Hystrix降级-服务消费方

1. feign组件已经集成了hystrix组件
2. 定义feign调用接口实现类，复写方法，即降级方法
3. 在@FeignClient注解中使用fallback属性设置降级处理类
4. 配置开启feign.hystrix.enabled = true

2. 定义feign调用接口实现类，复写方法，即降级方法

```java
/**
 * Feign 客户端的降级处理类
 * 1. 定义类 实现 Feign 客户端接口
 * 2. 使用@Component注解将该类的Bean加入SpringIOC容器
 */
@Component
public class GoodsFeignClientFallback implements GoodsFeignClient {
    @Override
    public Goods findGoodsById(int id) {
        Goods goods = new Goods();
        goods.setTitle("又被降级了~~~");
        return goods;
    }
}
```

3. 在@FeignClient注解中使用fallback属性设置降级处理类

```java
@FeignClient(value = "HYSTRIX-PROVIDER",fallback = GoodsFeignClientFallback.class)
public interface GoodsFeignClient {
    
    @GetMapping("/goods/findOne/{id}")
    public Goods findGoodsById(@PathVariable("id") int id);

}
```

4. 配置开启feign.hystrix.enabled = true

```yaml
# 开启feign对hystrix的支持
feign:
  hystrix:
    enabled: true
```

## 2.5 Hystrix熔断

+ Hystrix熔断机制，用于监控微服务调用情况，当失败的情况达到预定的阈值(5秒失败20次)，会打开断路器，拒绝所有请求，直到服务恢复正常为止

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.20/pics/4.png)

相关参数：

+ circuitBreaker.sleepWindowInMilliseconds：监控时间
+ circuitBreaker.requestVolumeThreshold：失败次数
+ circuitBreaker.errorThresholdPercentage：失败率

```java
@HystrixCommand(fallbackMethod = "findOne_fallback",commandProperties = {
        //设置Hystrix的超时时间，默认1s
        @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "3000"),
        //监控时间 默认5000 毫秒
        @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value = "5000"),
        //失败次数。默认20次
        @HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value = "20"),
        //失败率 默认50%
        @HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value = "50")

})
```

## 2.6 Hystrix熔断监控

+ Hystrix提供了Hystrix-dashboard功能，用于实时监控微服务运行状态，但是Hystrix-dashboard只能监控一个微服务
+ Netflix还提供了 Turbine，进行聚合监控

**搭建Turbine见单独文档**

# 3. 网关Gateway

## 3.1 简介

+ 网关旨在为微服务架构提供一种简单而有效的统一的API路由管理方式

+ 在微服务架构中，不同的微服务可以有不同的网络地址，各个微服务之间通过互相调用完成用户请求，客户端可能通过调用N个微服务的接口完成一个用户请求

  存在的问题：

  + 客户端多次请求不同的微服务，增加客户端的复杂性
  + 认证复杂，每个服务都要进行认证
  + http请求不同服务次数增加，性能不高

+ 网关就是系统的入口，封装了应用程序的内部结构，为客户端提供统一服务，一些与业务本身功能无关的公共逻辑可以在这里实现，诸如认证、鉴权、监控、缓存、负载均衡、流量管控、路由转发等
+ 在目前的网关解决方案里，有Nginx+Lua、Netflix Zuul、Spring Cloud Gateway等等

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.20/pics/5.png)

## 3.2 简单案例

1. 搭建网关模块，引入依赖：starter-gateway
2. 编写启动类
3. 编写配置文件
4. 启动测试

1. 搭建网关模块，引入依赖：starter-gateway

```xml
<dependencies>
    <!--引入gateway 网关-->

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>

    <!-- eureka-client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>
```

2. 编写启动类

```java
@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApp.class,args);
    }

}
```

3. 编写配置文件

```yaml
server:
  port: 80

spring:
  application:
    name: api-gateway-server

  cloud:
    # 网关配置
    gateway:
      # 路由配置：转发规则
      routes: #集合。
      # id: 唯一标识。默认是一个UUID
      # uri: 转发路径
      # predicates: 条件,用于请求网关路径的匹配规则
      # filters：配置局部过滤器的

      - id: gateway-provider
        # 静态路由
        # uri: http://localhost:8001/
        # 动态路由
        uri: lb://GATEWAY-PROVIDER
        predicates:
        - Path=/goods/**
        filters:
        - AddRequestParameter=username,zhangsan

      - id: gateway-consumer
        # uri: http://localhost:9000
        uri: lb://GATEWAY-CONSUMER
        predicates:
        - Path=/order/**
        # 微服务名称配置
      discovery:
        locator:
          enabled: true # 设置为true 请求路径前可以添加微服务名称
          lower-case-service-id: true # 允许为小写

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

## 3.3 静态路由

见3.2

## 3.4 动态路由

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.20/pics/6.png)

将网关作为eureka的客户端拉取服务的uri即可

1. 引入eureka-client配置

加注解

配置文件

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

2. 修改uri属性：uri: lb://eureka中的服务名称

见3.2

## 3.5 微服务名称配置

```yaml
discovery:
  locator:
    enabled: true # 设置为true 请求路径前可以添加微服务名称
    lower-case-service-id: true # 允许为小写
```

见3.2

## 3.6 过滤器

+ Gateway支持过滤器功能，对请求或响应进行拦截，完成一些通用操作
+ Gateway 提供两种过滤器方式：pre和post
+ pre过滤器，在转发之前执行，可以做参数校验、权限校验、流量监控、日志输出、协议转换等
+ post过滤器，在响应之前执行，可以做响应内容、响应头的修改，日志的输出，流量监控等
+ Gateway还提供了两种类型过滤器
  + GatewayFilter：局部过滤器，针对单个路由
  + GlobalFilter ：全局过滤器，针对所有路由

## 3.7 局部过滤器

+ 在Spring Cloud Gateway组件中提供了大量内置的局部过滤器，对请求和响应做过滤操作
+ 遵循约定大于配置的思想，只需要在配置文件配置局部过滤器名称，并为其指定对应的值，就可以让其生效

例如

```yaml
filters:
- AddRequestParameter=username,zhangsan
```

## 3.8 全局过滤器

+ GlobalFilter全局过滤器，不需要在配置文件中配置，系统初始化时加载，并作用在每个路由上
+ Spring Cloud Gateway核心的功能也是通过内置的全局过滤器来完成

自定义全局过滤器步骤：

1. 定义类实现GlobalFilter和Ordered接口
2. 复写方法
3. 完成逻辑处理

```java
@Component
public class MyFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("自定义全局过滤器执行了~~~");

        return chain.filter(exchange);//放行
    }

    /**
     * 过滤器排序
     * @return 数值越小 越先执行
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
```