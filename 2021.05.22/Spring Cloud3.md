# 1. 分布式配置中心Config

## 1.1 简介

+ Spring Cloud Config解决了在分布式场景下多环境配置文件的管理和维护

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.22/pics/1.png)

+ 好处：
  + 集中管理配置文件
  + 不同环境不同配置，动态化的配置更新
  + 配置信息改变时，不需要重启即可更新配置信息到服务

## 1.2 简单案例

### 1.2.1 搭建config server

config server：

1. 使用github创建远程仓库，上传配置文件<font color='red'>(外部配置文件在云端)</font>
2. 搭建config server模块，导入相关依赖
3. 编写配置，设置gitee远程仓库地址
4. 测试访问远程配置文件


```xml
<dependencies>

    <!-- config-server -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>

</dependencies>
```

启动类

```java
@SpringBootApplication
@EnableConfigServer // 启用config server功能
public class ConfigServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApp.class,args);
    }
}
```

3. 编写配置，设置github远程仓库地址

```yaml
server:
  port: 9527

spring:
  application:
    name: config-server
  # spring cloud config
  cloud:
    config:
      server:
        # git的远程仓库地址
        git:
          uri: https://gitee.com/itheima_cch/itheima-configs.git
      label: master # 分支配置
```

4. 测试访问远程配置文件

输入网页地址访问，能看到外部配置文件的内容，说明config server可以访问外部配置文件了

### 1.2.2 搭建config client

config client：

1. 导入starter-config依赖
2. 配置config server地址，读取配置文件名称等信息
3. 获取配置值
4. 启动测试


```xml
<!--config client -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

2. 在bootstrap.yml中配置config server地址，读取配置文件名称等信息(bootstrap.yml优先级比application.yml高)

```yaml
# 配置config-server地址
# 配置获得配置文件的名称等信息
spring:
  cloud:
    config:
      # 配置config-server地址
      uri: http://localhost:9527
      # 配置获得配置文件的名称等信息
      name: config # 文件名
      profile: dev # profile指定，  config-dev.yml
      label: master # 分支
```

3. 获取配置值

```java
@Value("${itheima}")
private String itheima;
……
goods.setTitle(goods.getTitle() + ":" + port+":"+itheima);//将端口号，设置到了 商品标题上
```

### 1.2.3 刷新config client

1. 在config客户端引入actuator依赖
2. 获取配置信息类上，添加@RefreshScope注解
3. 添加配置management.endpoints.web.exposure.include: refresh
4. 使用curl工具发送post请求 curl -X POST http://localhost:8001/actuator/refresh

1. 在config客户端引入actuator依赖

```xml
<!--config client -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

2. 获取配置信息类上，添加@RefreshScope注解
3. 在bootstrap.yml中添加配置

```yaml
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

## 1.3 Config集成Eureka

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.22/pics/2.png)

pom.xml引入坐标

config-client配置：

```yaml
spring:
   cloud:
     config:
     #从注册中心去寻找config-server地址
       discovery:
         enabled: true
         service-id: config-server
```

config-server配置：

```yaml
eureka:
   client:
     service-url:
       defaultZone: http://localhost:8761/eureka/
```

添加注解@EnableEurekaClient

# 2. 消息总线Bus

## 2.1 简介

+ Spring Cloud Bus是用轻量的消息中间件将分布式的节点连接起来，可以用于广播配置文件的更改或者服务的监控管理。关键的思想就是，消息总线可以为微服务做监控，也可以实现应用程序之间相通信

+ Spring Cloud Bus可选的消息中间件包括RabbitMQ和Kafka

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.22/pics/3.png)

即发一次通知，所有客户端A、B、C都获取到最新的外部配置文件

## 2.2 RabbitMQ回顾

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.22/pics/4.png)

RabbitMQ提供了6种工作模式：简单模式、work queues、Publish/Subscribe发布与订阅模式、Routing 路由模式、Topics 主题模式、RPC 远程调用模式(远程调用，不太算 MQ，暂不作介绍)

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.22/pics/5.png)

## 2.3 简单案例

1. 分别在config-server和config-client中引入bus依赖：bus-amqp
2. 分别在config-server和config-client中配置RabbitMQ
3. 在config-server中设置暴露监控端点：bus-refresh
4. 启动测试

1. config-server的pom.xml

```xml
<!-- bus -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```

2. config-server的配置文件

```yaml
#配置rabbitmq信息
rabbitmq:
  host: localhost
  port: 5672
  username: guest
  password: guest
  virtual-host: /
```

3. config-server引入actuator坐标，设置暴露监控端点

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```yaml
# 暴露bus的刷新端点
management:
  endpoints:
    web:
      exposure:
        include: 'bus-refresh'
```

4. config-client类似修改

# 3. 消息驱动Stream

## 3.1 简介

+ Spring Cloud Stream是一个构建消息驱动微服务应用的框架
+ Stream解决了开发人员无感知的使用消息中间件的问题，因为Stream对消息中间件的进一步封装，可以做到代码层面对中间件的无感知，甚至于动态的切换中间件，使得微服务开发的高度解耦，服务可以关注更多自己的业务流程
+ Spring Cloud Stream目前支持两种消息中间件RabbitMQ和Kafka

类似于JDBC的思想

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.22/pics/6.png)

## 3.2 组件

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.22/pics/7.png)

+ Spring Cloud Stream构建的应用程序与消息中间件之间是通过绑定器Binder相关联的。绑定器对于应用程序而言起到了隔离作用，它使得不同消息中间件的实现细节对应用程序来说是透明的

+ binding是我们通过配置把应用和spring cloud stream的binder绑定在一起
+ output：发送消息Channel，内置Source接口
+ input：接收消息Channel，内置Sink接口

## 3.3 消息生产者

1. 创建消息生产者模块，引入依赖starter-stream-rabbit
2. 编写配置，定义binder和bindings
3. 定义消息发送业务类。添加@EnableBinding(Source.class)，注入MessageChannel output，完成消息发送
4. 编写启动类，测试


```xml
<dependencies>

    <!--spring boot web-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- stream -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
    </dependency>

</dependencies>
```

2. 编写配置，定义binder和bindings

```yaml
server:
  port: 8000

spring:
  cloud:
    stream:
      # 定义绑定器，绑定到哪个消息中间件上
      binders:
        itheima_binder: # 自定义的绑定器名称
          type: rabbit # 绑定器类型
          environment: # 指定mq的环境
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
                virtual-host: /
      bindings:
        output: # channel名称
          binder: itheima_binder #指定使用哪一个binder
          destination: itheima_exchange # 消息目的地
```

3. 定义消息发送业务类。添加@EnableBinding(Source.class)，注入MessageChannel output，完成消息发送

```java
@Component
@EnableBinding(Source.class)
public class MessageProducer {

    @Autowired
    private MessageChannel output;

    public void send(){
        String msessage = "hello stream~~~";

        //发送消息
        output.send(MessageBuilder.withPayload(msessage).build());

        System.out.println("消息发送成功~~~");

    }
}
```

4. 编写启动类，测试

```java
@RestController
public class ProducerController {

    @Autowired
    private MessageProducer producer;


    @RequestMapping("/send")
        public String sendMsg(){
        producer.send();
        return "success";
    }
}
```

```java
@SpringBootApplication
public class ProducerApp {
    public static void main(String[] args) {

        SpringApplication.run(ProducerApp.class,args);
    }
}
```

## 3.4 消息消费者

1. 创建消息消费者模块，引入依赖starter-stream-rabbit
2. 编写配置，定义binder，和bindings
3. 定义消息接收业务类。添加@EnableBinding(Sink.class)，使用@StreamListener(Sink.INPUT)，完成消息接收
4. 编写启动类，测试

1. 同3.3小节

2. 编写配置，定义binder，和bindings

```yaml
server:
  port: 9000

spring:
  cloud:
    stream:
      # 定义绑定器，绑定到哪个消息中间件上
      binders:
        itheima_binder: # 自定义的绑定器名称
          type: rabbit # 绑定器类型
          environment: # 指定mq的环境
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
                virtual-host: /
      bindings:
        input: # channel名称
          binder: itheima_binder #指定使用哪一个binder
          destination: itheima_exchange # 消息目的地
```

3. 定义消息接收业务类。添加@EnableBinding(Sink.class)，使用@StreamListener(Sink.INPUT)，完成消息接收

```java
/**
 * 消息接收类
 */
@EnableBinding({Sink.class})
@Component
public class MessageListener {

    @StreamListener(Sink.INPUT)
    public void receive(Message message){

        System.out.println(message);
        System.out.println(message.getPayload());
    }
}
```

# 4. 链路追踪Sleuth+Zipkin

## 4.1 简介

+ Spring Cloud Sleuth其实是一个工具，它在整个分布式系统中能跟踪一个用户请求的过程，捕获这些跟踪数据，就能构建微服务的整个调用链的视图，这是调试和监控微服务的关键工具
  + 耗时分析
  + 可视化错误
  + 链路优化

+ Zipkin是Twitter的一个开源项目，它致力于收集服务的定时数据，以解决微服务架构中的延迟问题，包括数据的收集、存储、查找和展现

## 4.2 简单案例

1. 安装启动zipkin。java –jar zipkin.jar
2. 访问zipkin web界面。http://localhost:9411/
3. 在服务提供方和消费方分别引入sleuth和zipkin依赖
4. 分别配置服务提供方和消费方
5. 启动测试

```xml
<!-- sleuth-zipkin -->
<!--<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>-->

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

```yaml
server:
  port: 9000

eureka:
  instance:
    hostname: localhost # 主机名
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
spring:
  application:
    name: feign-consumer # 设置当前应用的名称。将来会在eureka中Application显示。将来需要使用该名称来获取路径
  zipkin:
    base-url: http://localhost:9411/  # 设置zipkin的服务端路径

  sleuth:
    sampler:
      probability: 1 # 采集率 默认 0.1 百分之十。

logging:
  level:
    com.itheima: debug
```