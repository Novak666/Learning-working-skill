# 1. SpringBoot2课程介绍

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.27/pics/1.png)

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.27/pics/2.png)

## 1.1 前期准备

+ 学习要求
  + 熟悉Spring基础
  + 熟悉Maven使用

+ 环境要求
  + Java8及以上
  + Maven3.3及以上

+ 学习资料
  + SpringBoot官网：https://spring.io/projects/spring-boot
  + SpringBoot官方文档：https://docs.spring.io/spring-boot/docs/

+ 课程文档地址1：https://blog.csdn.net/u011863024/article/details/113667634
+ 课程文档地址2：https://www.yuque.com/atguigu/springboot
+ 课程源码地址：https://gitee.com/leifengyang/springboot2

# 2. Spring能做什么

## 2.1 Spring的能力

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.27/pics/3.png)

- web开发
- 数据访问
- 安全控制
- 分布式
- 消息服务
- 移动开发
- 批处理

## 2.2 Spring5重大升级

+ 响应式编程

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.27/pics/4.png)

+ 内部源码设计

基于Java8的一些新特性，如：接口默认实现。重新设计源码架构

## 2.3 SpringBoot优点

+ Create stand-alone Spring applications
  + 创建独立Spring应用

+ Embed Tomcat, Jetty or Undertow directly (no need to deploy WAR files)
  + 内嵌web服务器

+ Provide opinionated ‘starter’ dependencies to simplify your build configuration
  + 自动starter依赖，简化构建配置

+ Automatically configure Spring and 3rd party libraries whenever possible
  + 自动配置Spring以及第三方功能

+ Provide production-ready features such as metrics, health checks, and externalized configuration
  + 提供生产级别的监控、健康检查及外部化配置

+ Absolutely no code generation and no requirement for XML configuration
  + 无代码生成、无需编写XML

SpringBoot是整合Spring技术栈的一站式框架

SpringBoot是简化Spring技术栈的快速开发脚手架

## 2.4 SpringBoot缺点

+ 人称版本帝，迭代快，需要时刻关注变化
+ 封装太深，内部原理复杂，不容易精通

# 3. 时代背景

## 3.1 微服务

[James Lewis and Martin Fowler (2014)](https://martinfowler.com/articles/microservices.html)  提出微服务完整概念。<https://martinfowler.com/microservices/>

In short, the **microservice architectural style** is an approach to developing a single application as a **suite of small services**, each **running in its own process** and communicating with **lightweight** mechanisms, often an **HTTP** resource API. These services are **built around business capabilities** and **independently deployable** by fully **automated deployment** machinery. There is a **bare minimum of centralized management** of these services, which may be **written in different programming languages** and use different data storage technologies.-- [James Lewis and Martin Fowler (2014)](https://martinfowler.com/articles/microservices.html)

- 微服务是一种架构风格
- 一个应用拆分为一组小型服务

- 每个服务运行在自己的进程内，也就是可独立部署和升级
- 服务之间使用轻量级HTTP交互

- 服务围绕业务功能拆分
- 可以由全自动部署机制独立部署

- 去中心化，服务自治。服务可以使用不同的语言、不同的存储技术

## 3.2 分布式

分布式的困难：

- 远程调用
- 服务发现
- 负载均衡
- 服务容错
- 配置管理
- 服务监控
- 链路追踪
- 日志管理
- 任务调度
- …

分布式解决方案：

- SpringBoot + SpringCloud

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.27/pics/5.png)

## 3.3 云原生

原生应用如何上云。 Cloud Native

上云的困难

- 服务自愈
- 弹性伸缩
- 服务隔离
- 自动化部署
- 灰度发布
- 流量治理
- …

上云的解决：

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.27/pics/6.png)

# 4. SpringBoot官方文档架构

官方参考文档学习：https://docs.spring.io/spring-boot/docs/current/reference/html/index.html

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.27/pics/7.png)

版本新特性：https://spring.io/projects/spring-boot#overview

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.27/pics/8.png)

