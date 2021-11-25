# 1. 初识Spring Cloud

## 1.1 微服务

+ 微服务是系统架构上的一种设计风格，它的主旨是将一个原本独立的系统拆分成多个小型服务，这些小型服务都在各自独立的进程中运行，服务之间一般通过 HTTP 的 RESTfuL API 进行通信协作

+ 微服务是系统架构上的一种设计风格，它的主旨是将一个原本独立的系统拆分成多个小型服务，这些小型服务都在各自独立的进程中运行，服务之间一般通过 HTTP 的 RESTfuL API 进行通信协作

+ 由于有了轻量级的通信协作基础,所以这些微服务可以使用不同的语言来编写

## 1.2 Spring Cloud简介

•Spring Cloud是一系列框架的有序集合

•Spring Cloud并没有重复制造轮子，它只是将目前各家公司开发的比较成熟、经得起实际考验的服务框架组合起来。

•通过Spring Boot风格进行再封装屏蔽掉了复杂的配置和实现原理，最终给开发者留出了一套简单易懂、易部署和易维护的分布式系统开发工具包。 

•它利用Spring Boot的开发便利性巧妙地简化了分布式系统基础设施的开发，如服务发现注册、配置中心、消息总线、负载均衡、 断路器、数据监控等，都可以用Spring Boot的开发风格做到一键启动和部署。

•Spring Cloud项目官方网址：<https://spring.io/projects/spring-cloud> 

## 1.3 Spring Cloud和Dubbo对比

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.19/pics/1.png)

+ Spring Cloud与Dubbo都是实现微服务有效的工具
+ Dubbo只是实现了服务治理，而Spring Cloud子项目分别覆盖了微服务架构下的众多部件
+ Dubbo使用RPC通讯协议，Spring Cloud使用RESTful完成通信，Dubbo效率略高于Spring Cloud

# 2. Spring Cloud服务治理

## 2.1 Eureka

+ Eureka是Netflix公司开源的一个服务注册与发现的组件

+ Eureka和其他Netflix公司的服务组件(例如负载均衡、熔断器、网关等)一起，被Spring Cloud社区整合为Spring-Cloud-Netflix模块

+ Eureka包含两个组件：Eureka Server(注册中心)和Eureka Client(服务提供者、服务消费者)

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.19/pics/2.png)

## 2.2 Eureka使用步骤

1. 搭建Provider和Consumer服务
2. 使用RestTemplate完成远程调用
3. 搭建Eureka Server服务
4. 改造Provider和Consumer成为Eureka Client
5. Consumer服务通过从Eureka Server中抓取Provider地址完成 远程调用

### 2.2.1 搭建Provider和Consumer

创建新工程Spring-Cloud

Provider和Consumer模块的代码见1. Eureka快速入门- 环境搭建

### 2.2.2 使用RestTemplate完成远程调用

关于RestTemplate：

+ Spring提供的一种简单便捷的模板类，用于在java代码里访问restful服务
+ 其功能与HttpClient类似，但是RestTemplate实现更优雅，使用更方便

```java
//远程调用Goods服务中的findOne接口
使用RestTemplate
1. 定义Bean  restTemplate
2. 注入Bean
3. 调用方法
```

该部分代码见2. RestTemplate远程调用

### 2.2.3 搭建Eureka Server服务

步骤：

1. 创建eureka-server模块
2. 引入Spring Cloud和euraka-server相关依赖
3. 完成Eureka Server相关配置
4. 启动该模块

parent模块pom.xml中

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>
    <!--spring cloud 版本-->
    <spring-cloud.version>Greenwich.RELEASE</spring-cloud.version>
</properties>

<!--引入Spring Cloud 依赖-->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

euraka-server模块xml中

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- eureka-server -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
</dependencies>
```

euraka-server启动类加@EnableEurekaServer

```java
@SpringBootApplication
// 启用EurekaServer
@EnableEurekaServer
public class EurekaApp {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApp.class,args);
    }
}
```

Eureka Server配置

```yaml
server:
  port: 8761

# eureka 配置
# eureka 一共有4部分 配置
# 1. dashboard:eureka的web控制台配置
# 2. server:eureka的服务端配置
# 3. client:eureka的客户端配置
# 4. instance:eureka的实例配置

eureka:
  instance:
    hostname: localhost # 主机名
  client:
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka # eureka服务端地址，将来客户端使用该地址和eureka进行通信

    register-with-eureka: false # 是否将自己的路径 注册到eureka上。eureka server 不需要的，eureka provider client 需要
    fetch-registry: false # 是否需要从eureka中抓取路径。eureka server 不需要的，eureka consumer client 需要
```

### 2.2.4 改造Provider和Consumer成为Eureka Client

1. 引eureka-client相关依赖
2. 完成eureka-client相关配置
3. 启动测试

pom.xml添加依赖

```xml
<dependencies>

    <!--spring boot web-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- eureka-client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

</dependencies>
```

euraka-client启动类加@EnableEurekaClient

```java
@EnableEurekaClient //该注解 在新版本中可以省略
@SpringBootApplication
public class ProviderApp {


    public static void main(String[] args) {
        SpringApplication.run(ProviderApp.class,args);
    }
}
```

euraka-client配置

```yaml
server:
  port: 8001


eureka:
  instance:
    hostname: localhost # 主机名
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka # eureka服务端地址，将来客户端使用该地址和eureka进行通信
spring:
  application:
    name: eureka-provider # 设置当前应用的名称。将来会在eureka中Application显示。将来需要使用该名称来获取路径
```

### 2.2.5 动态获取路径

euraka-consumer启动类添加注解@EnableDiscoveryClient

```java
@EnableDiscoveryClient // 激活DiscoveryClient
@EnableEurekaClient
@SpringBootApplication
public class ConsumerApp {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class,args);
    }
}
```

DiscoveryClient动态获取

```java
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/goods/{id}")
    public Goods findGoodsById(@PathVariable("id") int id){
        System.out.println("findGoodsById..."+id);

        /*
            //远程调用Goods服务中的findOne接口
            使用RestTemplate
            1. 定义Bean  restTemplate
            2. 注入Bean
            3. 调用方法
         */

        /*
            动态从Eureka Server 中获取 provider 的 ip 和端口
             1. 注入 DiscoveryClient 对象.激活
             2. 调用方法
         */

        //演示discoveryClient 使用
        List<ServiceInstance> instances = discoveryClient.getInstances("EUREKA-PROVIDER");

        //判断集合是否有数据
        if(instances == null || instances.size() == 0){
            //集合没有数据
            return null;
        }

        ServiceInstance instance = instances.get(0);
        String host = instance.getHost();//获取ip
        int port = instance.getPort();//获取端口

        System.out.println(host);
        System.out.println(port);

        String url = "http://"+host+":"+port+"/goods/findOne/"+id;
        // 3. 调用方法
        Goods goods = restTemplate.getForObject(url, Goods.class);

        return goods;
    }
}
```

## 2.3 Eureka相关配置

### 2.3.1 instance

```yaml
eureka:
  instance:
    hostname: localhost # 主机名
    prefer-ip-address: true # 是否将当前实例的ip注册到eureka server中。默认是false 注册主机名
    ip-address: 127.0.0.1 # 设置当前实例的ip
    instance-id: ${eureka.instance.ip-address}:${spring.application.name}:${server.port} # 修改web控制台显示名 实例id
    lease-renewal-interval-in-seconds: 3 # 每隔3秒发一次心跳包
    lease-expiration-duration-in-seconds: 9 # 如果9秒没有发心跳包，服务器呀，你把我干掉吧~
```

### 2.3.2 server

自我保护机制，不会剔除不活跃的服务

```yaml
server:
  enable-self-preservation: false # 关闭自我保护机制
  eviction-interval-timer-in-ms: 3000 # 检查服务的时间间隔
```

## 2.4 Eureka高可用

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.19/pics/3.png)

步骤：

1. 准备两个Eureka Server
2. 分别进行配置，相互注册
3. Eureka Client分别注册到这两个Eureka Server中

<font color='red'>注意修改hosts文件</font>

eureka-server1

```yaml
server:
  port: 8761

eureka:
  instance:
    hostname: eureka-server1 # 主机名
  client:
    service-url:
      defaultZone: http://eureka-server2:8762/eureka
    register-with-eureka: true # 是否将自己的路径 注册到eureka上。eureka server 不需要的，eureka provider client 需要
    fetch-registry: true # 是否需要从eureka中抓取路径。eureka server 不需要的，eureka consumer client 需要


spring:
  application:
    name: eureka-server-ha
```

eureka-server2

```yaml
server:
  port: 8762

eureka:
  instance:
    hostname: eureka-server2 # 主机名
  client:
    service-url:
      defaultZone: http://eureka-server1:8761/eureka

    register-with-eureka: true # 是否将自己的路径 注册到eureka上。eureka server 不需要的，eureka provider client 需要
    fetch-registry: true # 是否需要从eureka中抓取路径。eureka server 不需要的，eureka consumer client 需要
spring:
  application:
    name: eureka-server-ha
```

客户端修改配置

eureka-provider

```yaml
server:
  port: 8001

eureka:
  instance:
    hostname: localhost # 主机名
    prefer-ip-address: true # 将当前实例的ip注册到eureka server 中。默认是false 注册主机名
    ip-address: 127.0.0.1 # 设置当前实例的ip
    instance-id: ${eureka.instance.ip-address}:${spring.application.name}:${server.port} # 设置web控制台显示的 实例id
    lease-renewal-interval-in-seconds: 3 # 每隔3 秒发一次心跳包
    lease-expiration-duration-in-seconds: 9 # 如果9秒没有发心跳包，服务器呀，你把我干掉吧~
  client:
    service-url:
      defaultZone: http://eureka-server1:8761/eureka,http://eureka-server2:8762/eureka # eureka服务端地址，将来客户端使用该地址和eureka进行通信
spring:
  application:
    name: eureka-provider # 设置当前应用的名称。将来会在eureka中Application显示。将来需要使用该名称来获取路径
```

eureka-consumer

```yaml
server:
  port: 9000


eureka:
  instance:
    hostname: localhost # 主机名
  client:
    service-url:
      defaultZone:  http://eureka-server1:8761/eureka,http://eureka-server2:8762/eureka  # eureka服务端地址，将来客户端使用该地址和eureka进行通信
spring:
  application:
    name: eureka-consumer # 设置当前应用的名称。将来会在eureka中Application显示。将来需要使用该名称来获取路径
```

## 2.5 Consul

+ Consul是由HashiCorp基于Go语言开发的，支持多数据中心，分布式高可用的服务发布和注册服务软件

+ 用于实现分布式系统的服务发现与配置
+ 使用起来也较为简单。具有天然可移植性(支持Linux、windows和Mac OS X)；安装包仅包含一个可执行文件，方便部署
+ 官网地址：https://www.consul.io

运行，解压后进入目录.\consul agent

## 2.6 Consul使用步骤

1. 搭建Provider和Consumer服务
2. 使用RestTemplate完成远程调用
3. 将Provider服务注册到Consul中
4. Consumer服务通过从Consul中抓取Provider地址完成远程调用

代码见code，主要是配置文件不同

## 2.7 Nacos

+ Nacos(Dynamic Naming and Configuration Service)是阿里巴巴2018年7月开源的项目
+ 它专注于服务发现和配置管理领域，致力于帮助您发现、配置和管理微服务。Nacos支持几乎所有主流类型的“服务”的发现、配置和管理
+ 一句话概括就是Nacos = Spring Cloud注册中心 + Spring Cloud配置中心
+ 官网：https://nacos.io/
+ 下载地址：https://github.com/alibaba/nacos/releases

代码见code，主要是配置文件不同

# 3. Ribbon客户端负载均衡

## 3.1 简介

+ Ribbon是Netflix提供的一个基于HTTP和TCP的客户端负载均衡工具

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.19/pics/4.png)

+ 服务器端负载均衡
  + 负载均衡算法在服务器端
  + 由负载均衡器维护服务地址列表

+ 客户端负载均衡
  + 负载均衡算法在客户端
  + 客户端维护服务地址列表

## 3.2 简化远程调用

```java
* 1. 在声明restTemplate的Bean时候，添加一个注解：@LoadBalanced
* 2. 在使用restTemplate发起请求时，需要定义url时，host:port可以替换为服务提供方的应用名称
```

RestTemplateConfig

```java
@Configuration
public class RestTemplateConfig {


    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

OrderController

```java
@GetMapping("/goods2/{id}")
public Goods findGoodsById2(@PathVariable("id") int id){


    String url = "http://EUREKA-PROVIDER/goods/findOne/"+id;
    // 3. 调用方法
    Goods goods = restTemplate.getForObject(url, Goods.class);


    return goods;
}
```

## 3.3 负载均衡

Ribbon负责均衡策略：

1. 随机RandomRule

2. 轮询RoundRobinRule

3. 最小并发BestAvailableRule

4. 过滤AvailabilityFilteringRule

5. 响应时间WeightedResponseTimeRule

6. 轮询重试RetryRule

7. 性能可用性ZoneAvoidanceRule

设置负载均衡策略

1. 编码

```java
@Configuration
public class MyRule {
    
    @Bean
    public IRule rule(){
        return new RandomRule();
    }
}
```

```java
//@RibbonClient(name="EUREKA-PROVIDER",configuration = MyRule.class)
public class ConsumerApp {
    
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class,args);
    }
}
```

2. 配置

```yaml
# 配置的方式设置Ribbon的负载均衡策略
EUREKA-PROVIDER: # 设置的服务提供方的应用名称
  ribbon:
    NFloadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule # 策略类
```