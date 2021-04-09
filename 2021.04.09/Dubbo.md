# 1. 分布式系统相关概念

## 1.1 互联网项目架构的特点

1. 用户多
2. 流量大，并发高
3. 海量数据
4. 易受攻击
5. 功能繁琐
6. 变更快

## 1.2 互联网项目架构的目标

衡量网站的性能指标：

+ 响应时间：指执行一个请求从开始到最后收到响应数据所花费的总体时间

+ 并发数：指系统同时能处理的请求数量

  + 并发连接数：指的是客户端向服务器发起请求，并建立了TCP连接。每秒钟服务器连接的总TCP数量

  + 请求数：也称为QPS(Query Per Second)指每秒多少请求

  + 并发用户数：单位时间内有多少用户

+ 吞吐量：指单位时间内系统能处理的请求数量
  + QPS: Query Per Second每秒查询数
  + TPS: Transactions Per Second每秒事务数
  + 一个事务是指一 个客户机向服务器发送请求然后服务器做出反应的过程。客户机在发送请求时开始计时，收到服务器响应后结束计时，以此来计算使用的时间和完成的事务个数
  + 一个页面的一次访问，只会形成一 个TPS; 但-次页面请求，可能产生多次对服务器的请求，就会有多个QPS


注意：QPS>=并发连接数>= TPS

互联网项目架构目标：

+ 高性能：提供快速的访问体验
+ 高可用：网站服务一直可以正常访问
+ 可伸缩：通过硬件的增加/减少，提高/降低处理能力
+ 高可扩展：系统间耦合低，方便地通过新增/移除的方式，增加/减少新的功能模块
+ 安全性：提供网站安全访问和数据加密，安全存储等策略
+ 敏捷性：随需应变，快速响应

## 1.3 集群和分布式

通俗表示

+ 集群：很多“人”一起，干一样的事
  + 一个业务模块，部署在多台服务器上
+ 分布式：很多"人”一起，干不样的事。这些不一样的事， 合起来是一件大事
  + 一个大的业务系统，拆分为小的业务模块，分别部署在不同的机器上

## 1.4 架构演进

### 1.4.1 单体架构

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/1.png)

优点：

+ 简单：开发部署都很方便，小型项目首选

缺点：

+ 项目启动慢
+ 可靠性差
+ 可伸缩性差
+ 扩展性和可维护性差
+ 性能低

### 1.4.2 垂直架构

垂直架构是指将单体架构中的多个模块拆分为多个独立的项目，形成多个独立的单体架构

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/2.png)

除了单体仍会有的缺点外，存在重复功能太多的问题

### 1.4.3 分布式架构

分布式架构是指在垂直架构的基础上,将公共业务模块抽取出来，作为独立的服务供其他调用者消费，以实现服务的共享和重用。底层通过RPC(远程过程调用实现)

RPC：Remote Procedure Call远程过程调用。有非常多的协议和技术来都实现了RPC的过程。比如：HTTP REST风格，Java RMI规范、WebService SOAP协议Hession等等

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/3.png)

存在的问题：服务提供方一旦产生变更，所有消费方都需要变更

### 1.4.4 SOA架构

SOA(Service- Oriented Architecture面向服务的架构)是一个组件模型，它将应用程序的不同功能单元(称为服务)进行拆分，并通过这些服务之间定义良好的接口和契约联系起来

ESB(Enterparise Servce Bus)企业服务总线，服务中介。主要是提供了一一个服务于服务之间的交互。ESB包含的功能如：负载均衡，流量控制，加密处理，服务的监控，异常处理，监控告急等等

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/4.png)

### 1.4.5 微服务架构

微服务架构是在SOA上做的升华，微服务架构强调的一个重点是“业务需要彻底的组件化和服务化”，原有的单个业务系统会拆分为多个可以独立开发、设计、运行的小应用。这些小应用之间通过服务完成交互和集成

微服务架构=80%的SOA服务架构思想+ 100%的组件化架构思想+ 80%的领域建模思想

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/5.png)

特点：

+ 服务实现组件化：开发者可以自由选择开发技术。也不需要协调其他团队
+ 服务之间交互一 般使用REST API
+ 去中心化：每个微服务有自己私有的数据库持久化业务数据
+ 自动化部署：把应用拆分成为一个个独立的单个服务，方便自动化部署、测试、运维

Dubbo是SOA时代的产物，SpringCloud是微服务时代的产物

# 2. Dubbo概述

Dubbo是阿里巴巴公司开源的一个高性能、轻量级的Java RPC框架

致力于提供高性能和透明化的RPC远程服务调用方案，以及SOA服务治理方案

官网https://dubbo.apache.org

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/6.png)

Provider：暴露服务的服务提供方

Container：服务运行容器

Consumer：调用远程服务的服务消费方

Registry：服务注册与发现的注册中心

Monitor：统计服务的调用次数和调用时间的监控中心

# 3. Dubbo快速入门

## 3.1 zookeeper的安装

1. 有JDK7及以上环境

2. 把zookeeper的压缩包上传到linux系统

3. 解压

   在/opt下创建一个zookeeper目录，剪切压缩包，解压

   ```shell
   tar -zxvf apache-zookeeper-3.5.6-bin.tar.gz 
   ```

4. 进入到conf目录拷贝一个zoo_sample.cfg并完成配置

   ```shell
   #进入到conf目录
   cd /opt/zookeeper/apache-zooKeeper-3.5.6-bin/conf/
   #拷贝
   cp  zoo_sample.cfg  zoo.cfg
   ```

5. 在/opt/zookeeper下创建zookeeper的数据存储目录zkdata

6. 修改zoo.cfg

   ```shell
   vim /opt/zooKeeper/apache-zooKeeper-3.5.6-bin/conf/zoo.cfg
   ```

   修改存储目录：dataDir=/opt/zookeeper/zkdata

7. 启动zookeeper

   ```shell
   cd /opt/zookeeper/apache-zooKeeper-3.5.6-bin/bin/
   #启动
   ./zkServer.sh start
   #停止
   ./zkServer.sh stop
   ```

8. 查看zookeeper状态

   ```shell
   ./zkServer.sh status
   ```

## 3.2 Spring和SpringMVC的整合

步骤：

1. 创建服务提供者Provider模块
2. 创建服务消费者Consumer模块
3. 在服务提供者模块编写UserServicelmpl提供服务
4. 在服务消费者中的UserController远程调用UserServicelmpl提供的服务
5. 分别启动两个服务，测试

前置工作：创建一个空的project，设置jdk，settings里面设置Maven

具体代码见code

## 3.3 服务提供者

dubbo-service

1. pom.xml，打包方式改成war，独立对外提供服务，所以引入tomcat坐标

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itheima</groupId>
    <artifactId>dubbo-service</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <spring.version>5.1.9.RELEASE</spring.version>
        <dubbo.version>2.7.4.1</dubbo.version>
        <zookeeper.version>4.0.0</zookeeper.version>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- servlet3.0规范的坐标 -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
            <scope>provided</scope>
        </dependency>
        <!--spring的坐标-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <!--springmvc的坐标-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!--日志-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.21</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.21</version>
        </dependency>

        <!--Dubbo的起步依赖，版本2.7之后统一为rg.apache.dubb -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
            <version>${dubbo.version}</version>
        </dependency>
        <!--ZooKeeper客户端实现 -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>${zookeeper.version}</version>
        </dependency>
        <!--ZooKeeper客户端实现 -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>${zookeeper.version}</version>
        </dependency>

        <!--依赖公共的接口模块-->
        <dependency>
            <groupId>com.itheima</groupId>
            <artifactId>dubbo-interface</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <!--tomcat插件-->
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <version>2.1</version>
                <configuration>
                    <port>9000</port>
                    <path>/</path>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

2. applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:dubbo="http://dubbo.apache.org/schema/dubbo" xmlns:context="http://www.springframework.org/schema/context"
      xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">


   <!--<context:component-scan base-package="com.itheima.service" />-->

   <!--dubbo的配置-->
   <!--1.配置项目的名称,唯一-->
   <dubbo:application name="dubbo-service"/>
   <!--2.配置注册中心的地址-->
   <dubbo:registry address="zookeeper://192.168.149.135:2181"/>
   <!--3.配置dubbo包扫描-->
   <dubbo:annotation package="com.itheima.service.impl" />

</beans>
```

3. web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         version="2.5">

       
   <!-- spring -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath*:spring/applicationContext*.xml</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
</web-app>
```

4. 服务实现类

   注意：服务实现类上使用的Service注解是Dubbo提供的，用于对外发布服务

```java
package com.itheima.service.impl;

import com.itheima.service.UserService;
import org.apache.dubbo.config.annotation.Service;


//@Service//将该类的对象创建出来，放到Spring的IOC容器中  bean定义

@Service//将这个类提供的方法（服务）对外发布。将访问的地址 ip，端口，路径注册到注册中心中
public class UserServiceImpl implements UserService {

    public String sayHello() {
        return "hello dubbo hello!~";
    }
}
```

## 3.4 服务消费者

dubbo-web

1. pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itheima</groupId>
    <artifactId>dubbo-web</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <spring.version>5.1.9.RELEASE</spring.version>
        <dubbo.version>2.7.4.1</dubbo.version>
        <zookeeper.version>4.0.0</zookeeper.version>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- servlet3.0规范的坐标 -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
            <scope>provided</scope>
        </dependency>
        <!--spring的坐标-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <!--springmvc的坐标-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!--日志-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.21</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.21</version>
        </dependency>

        <!--Dubbo的起步依赖，版本2.7之后统一为rg.apache.dubb -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
            <version>${dubbo.version}</version>
        </dependency>
        <!--ZooKeeper客户端实现 -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>${zookeeper.version}</version>
        </dependency>
        <!--ZooKeeper客户端实现 -->
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-recipes</artifactId>
            <version>${zookeeper.version}</version>
        </dependency>

        <!--依赖公共的接口模块-->
        <dependency>
            <groupId>com.itheima</groupId>
            <artifactId>dubbo-interface</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <!--依赖service模块-->
       <!-- <dependency>
            <groupId>com.itheima</groupId>
            <artifactId>dubbo-service</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>-->

    </dependencies>

    <build>
        <plugins>
            <!--tomcat插件-->
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <version>2.1</version>
                <configuration>
                    <port>8000</port>
                    <path>/</path>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

2. springmvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd
         http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <mvc:annotation-driven/>
    <context:component-scan base-package="com.itheima.controller"/>

    <!--dubbo的配置-->
    <!--1.配置项目的名称,唯一-->
    <dubbo:application name="dubbo-web" >
        <dubbo:parameter key="qos.port" value="33333"/>
    </dubbo:application>
    <!--2.配置注册中心的地址-->
    <dubbo:registry address="zookeeper://192.168.149.135:2181"/>
    <!--3.配置dubbo包扫描-->
    <dubbo:annotation package="com.itheima.controller" />
    
</beans>
```

3. web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         version="2.5">

       
   <!-- spring -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath*:spring/applicationContext*.xml</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

</web-app>
```

4. 服务实现类

```java
package com.itheima.controller;

import com.itheima.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    //注入Service
    //@Autowired//本地注入

    /*
        1. 从zookeeper注册中心获取userService的访问url
        2. 进行远程调用RPC
        3. 将结果封装为一个代理对象。给变量赋值
     */

    @Reference//远程注入
    private UserService userService;

    @RequestMapping("/sayHello")
    public String sayHello(){
        return userService.sayHello();
    }

}
```

## 3.5 公共接口

dubbo-interface

```java
package com.itheima.service;

public interface UserService {
    public String sayHello();
}
```

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itheima</groupId>
    <artifactId>dubbo-interface</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

</project>
```

<font color='red'>测试注意：要关闭CentOS防火墙</font>

查看防火墙状态

```shell
firewall-cmd --state
```

停止firewall

```shell
systemctl stop firewalld.service
```

禁止firewall开机启动

```shell
systemctl disable firewalld.service
```

## 3.6 小节

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/7.png)

# 4. Dubbo高级特性

## 4.1 dubbo-admin简介

+ dubbo-admin管理平台，是图形化的服务管理页面
+ 从注册中心中获取到所有的提供者/消费者进行配置管理
+ 路由规则、动态配置、服务降级、访问控制、权重调整、负载均衡等管理功能
+ dubbo-admin是一个前后端分离的项目。前端使用vue，后端使用springboot
+ 安装dubbo-admin其实就是部署该项目

## 4.2 dubbo-admin的安装与使用

1. 环境准备

   要保证开发环境有jdk，maven，nodejs

   因为前端工程是用vue开发的，所以需要安装node.js，node.js中自带了npm，后面我们会通过npm启动

   下载地址

   https://nodejs.org/en/

2. 下载dubbo-admin

   https://github.com/apache/dubbo-admin

   解压到任意文件夹

3. 修改配置文件

   进入…\dubbo-admin-develop\dubbo-admin-server\src\main\resources目录，修改配置文件application.properties

   ```shell
   # centers in dubbo2.7
   admin.registry.address=zookeeper://192.168.23.129:2181
   admin.config-center=zookeeper://192.168.23.129:2181
   admin.metadata-report.address=zookeeper://192.168.23.129:2181
   ```

4. 打包项目

   在dubbo-admin-develop目录执行打包命令

   ```shell
   mvn clean package
   ```

5. 启动后端

   切换到目录...\dubbo-Admin-develop\dubbo-admin-distribution\target，执行下面的命令启动dubbo-admin，dubbo-admin后台由SpringBoot构建

   ```shell
   java -jar .\dubbo-admin-0.1.jar
   ```

6. 前台后端

   dubbo-admin-ui目录下执行命令

   ```shell
   npm run dev
   ```

7. 访问

   浏览器输入，用户名密码都是root

   http://localhost:8081/

<font color='red'>注意：以上命令要在powershell下运行</font>

我们需要打开我们的生产者配置文件加入下面配置

```xml
    <!-- 元数据配置 -->
    <dubbo:metadata-report address="zookeeper://192.168.23.129:2181" />
```

重新启动生产者，再次打开dubbo-admin

## 4.3 序列化

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/8.png)

+ dubbo内部已经将序列化和反序列化的过程内部封装了

+ 我们只需要在定义pojo类时实现serializable接口即可

+ 一般会定义一个公共的pojo模块，让生产者和消费者都依赖该模块

1. dubbo-interface的UserService接口内多增加一个方法findUserById

```java
public interface UserService {

    public String sayHello();
    /**
     * 查询用户
     */
    public User findUserById(int id);
}
```

2. dubbo-interface的pom.xml引入pojo依赖

```xml
<dependencies>
    <dependency>
        <groupId>com.itheima</groupId>
        <artifactId>dubbo-pojo</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

3. dubbo-service的实现方法findUserById

```java
//@Service//将该类的对象创建出来，放到Spring的IOC容器中  bean定义
@Service//将这个类提供的方法（服务）对外发布。将访问的地址 ip，端口，路径注册到注册中心中
public class UserServiceImpl implements UserService {

    public String sayHello() {
        return "hello dubbo hello!~";
    }

    @Override
    public User findUserById(int id) {
        //模拟查询User对象
        User user = new User(1, "zhangsan", "123");
        return user;
    }
}
```

4. dubbo-web增加方法find调用dubbo-service的findUserById

```java
@RestController
@RequestMapping("/user")
public class UserController {

    //注入Service
    //@Autowired//本地注入

    /*
        1. 从zookeeper注册中心获取userService的访问url
        2. 进行远程调用RPC
        3. 将结果封装为一个代理对象。给变量赋值

     */

    @Reference//远程注入
    private UserService userService;


    @RequestMapping("/sayHello")
    public String sayHello(){
        return userService.sayHello();
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @RequestMapping("/find")
    public User find(int id){
        return userService.findUserById(id);
    }

}
```

## 4.4 地址缓存

注册中心挂了，服务是否可以正常访问？

1. 可以，因为dubbo服务消费者在第一-次调用时，会将服务提供方地址缓存到本地，以后在调用则不会访问注册中心
2. 当服务提供者地址发生变化时，注册中心会通知服务消费者

## 4.5 超时

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/9.png)

+ 服务消费者在调用服务提供者的时候发生了阻塞、等待的情形,这个时候,服务消费者会直等待下去

+ 在某个峰值时刻，大量的请求都在同时请求服务消费者，会造成线程的大量堆积，势必会造成雪崩
+ dubbo利用超时机制来解决这个问题，设置一个超时时间，在这个时间段内，无法完成服务访问,则自动断开连接
+ 使用timeout属性配置超时时间，默认值1000，单位毫秒

dubbo-service(建议)

```java
//timeout 超时时间 单位毫秒  retries 重试次数
@Service(timeout = 3000,retries=0)
```

dubbo-web

```java
//timeout 超时时间 单位毫秒  retries 重试次数
@Reference(timeout = 3000,retries=0)
```

## 4.6 重试

+ 设置了超时时间，在这个时间段内，无法完成服务访问,则自动断开连接
+ 如果出现网络抖动，则这一次请求就会失败
+ dubbo提供重试机制来避免类似问题的发生
+ 通过retries属性来设置重试次数，默认为2次

注意：超时了才会重试

## 4.7 多版本

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/10.png)

灰度发布：当出现新功能时,会让一部分用户先使用新功能，用户反馈没问题时，再将所有用户迁移到新功能。

dubbo中使用version属性来设置和调用同一个接口的不同版本

生产者配置

```java
@Service(version="v2.0")
public class UserServiceImp12 implements UserService {...}
```

消费者配置

```java
@Reference(version = "v2.0")//远程注入
private UserService userService;
```

## 4.8 负载均衡

负载均衡策略：

1. Random：按权重随机，默认值。按权重设置随机概率
2. RoundRobin：按权重轮询
3. LeastActive：最少活跃调用数，相同活跃数的随机
4. ConsistentHash：一致性Hash，相同参数的请求总是发到同一提供者

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/11.png)

服务提供者配置

```java
@Service(weight = 100)
public class UserServiceImp12 implements UserService {...}
```

applicationContext.xml配置parameter key，不同端口

```xml
<!--dubbo的配置-->
<dubbo:protocol port="20882"/>
<!--1.配置项目的名称,唯一-->
<dubbo:application name="dubbo-service">
   <dubbo:parameter key="qos.port" value="4444"/>
</dubbo:application>
<!--2.配置注册中心的地址-->
<dubbo:registry address="zookeeper://192.168.23.129:2181"/>
<!--3.配置dubbo包扫描-->
<dubbo:annotation package="com.itheima.service.impl" />

<dubbo:metadata-report address="zookeeper://192.168.23.129:2181" />
```

消费者配置

```java
//@Reference(loadbalance = "roundrobin")
//@Reference(loadbalance = "leastactive")
//@Reference(loadbalance = "consistenthash")
@Reference(loadbalance = "random")//默认 按权重随机
private UserService userService;
```

## 4.9 集群容错

集群容错模式：

1. Failover Cluster：失败重试，默认值。当出现失败，重试其它服务器，默认重试2次，使用retries配置。一般用于读操作
2. Failfast Cluster：快速失败，发起一次调用，失败立即报错。通常用于写操作
3. Failsafe Cluster：失败安全，出现异常时，直接忽略。返回一个空结果
4. Failback Cluster：失败自动恢复，后台记录失败请求，定时重发
5. Forking Cluster：并行调用多个服务器，只要一个成功即返回
6. Broadcast Cluster：广播调用所有提供者,逐个调用，任意一台报错则报错

消费者配置

```java
@Reference(cluster = "failover")//远程注入
private UserService userService;
```

## 4.10 服务降级

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.09/pics/12.png)

服务降级：当服务器压力剧增的情况下，根据实际业务情况及流量，对一些服务和页面有策略的不处理或换种简单的方式处理，从而释放服务器资源以保证**核心交易**正常运作或高效运作

服务降级方式：

1. mock= force:return null：表示消费方对该服务的方法调用都直接返回null值，不发起远程调用，用来屏蔽不重要服务不可用时对调用方的影响
2. mock=fail:return null：表示消费方对该服务的方法调用在失败后，再返回null值,不抛异常，用来容忍不重要服务不稳定时对调用方的影响

消费方配置

```java
//远程注入
@Reference(mock = "force:return null")//不再调用userService的服务
private UserService userService;
```