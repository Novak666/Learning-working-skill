# 1. 企业开发简介

## 1.1 JavaEE规范

JavaEE规范是很多Java开发技术的总称。这些技术规范都是沿用自`J2EE`的。一共包括了13个技术规范。例如：`jsp/servlet`，`jndi`，`jaxp`，`jdbc`，`jni`，`jaxb`，`jmf`，`jta`，`jpa`，`EJB`等。

## 1.2 Web概述

首先，我们先来介绍资源的分类，它分为静态资源和动态资源。其中：

静态资源指的是，网站中提供给人们展示的资源是一成不变的，也就是说不同人或者在不同时间，看到的内容都是一样的。例如：我们看到的新闻，网站的使用手册，网站功能说明文档等等。而作为开发者，我们编写的`html`,`css`,`js`,图片，多媒体等等都可以称为静态资源。

动态资源它指的是，网站中提供给人们展示的资源是由程序产生的，在不同的时间或者用不同的人员由于身份的不同，所看到的内容是不一样的。例如：我们在12306上购买火车票，火车票的余票数由于时间的变化，会逐渐的减少，直到最后没有余票。作为开发人员，我们编写的`JSP`，`servlet`，`php`，`ASP`等都是动态资源。

## 1.3 系统结构简介

在我们前面课程的学习中，开发的都是`Java`工程。这些工程在企业中称之为项目或者产品。项目也好，产品也罢，它是有系统架构的，系统架构的划分有很多种方式。我们今天讨论的是基础结构上的划分。除此之外，还有技术选型划分，部署方式划分等等。

基础结构划分：C/S结构，B/S结构两类。

技术选型划分：Model1模型，Model2模型，MVC模型和三层架构+MVC模型。

部署方式划分：一体化架构，垂直拆分架构，分布式架构，流动计算架构，微服务架构。

# 2. Tomcat

## 2.1 Tomcat介绍

### 2.1.1 服务器简介

服务器的概念非常的广泛，它可以指代一台特殊的计算机（相比普通计算机运行更快、负载更高、价格更贵），也可以指代用于部署网站的应用。我们这里说的服务器，其实是web服务器，或者应用服务器。它本质就是一个软件，一个应用。作用就是发布我们的应用（工程），让用户可以通过浏览器访问我们的应用。

常见的应用服务器，请看下表：

| 服务器名称  | 说明                                                  |
| ----------- | ----------------------------------------------------- |
| weblogic    | 实现了javaEE规范，重量级服务器，又称为javaEE容器      |
| websphereAS | 实现了javaEE规范，重量级服务器。                      |
| JBOSSAS     | 实现了JavaEE规范，重量级服务器。免费的。              |
| Tomcat      | 实现了jsp/servlet规范，是一个轻量级服务器，开源免费。 |

### 2.1.2 Tomcat目录结构

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.01/pics/1.png)

## 2.2 Tomcat基本使用

### 2.2.1 启动和停止

Tomcat服务器的启动文件在二进制文件目录中：startup.bat和startup.sh，这两个文件就是Tomcat的启动文件。

Tomcat服务器的停止文件也在二进制文件目录中：shutdown.bat和shutdown.sh，这两个文件就是Tomcat的停止文件。

其中`.bat`文件是针对`windows`系统的运行程序，`.sh`文件是针对`linux`系统的运行程序。

### 2.2.2 启动问题

**第一个问题：启动一闪而过**

原因：没有配置环境变量。

解决办法：配置上JAVA_HOME环境变量

**第二个：java.net.BindException : JVM_Bind**

原因：端口被占用

解决办法：找到占用该端口的应用

​                    进程不重要：使用cmd命令：netstat -a -o 查看pid  在任务管理器中结束占用端口的进程。

​                    进程很重要：修改自己的端口号。修改的是Tomcat目录下`\conf\server.xml`中的配置。

### 2.2.3 IDEA集成Tomcat

1. 点击Run，Edit Configurations
2. 点击Defaults，Tomcat Server，Local
3. 点击Configure，选择tomcat的安装路径

### 2.2.4 Linux系统安装Tomcat

1. 新建一个目录放tomcat

```
mkdir /usr/local/tomcat
```

2. 上传压缩包并剪切到相应目录

```
mv apache-tomcat-8.5.32.tar.gz /usr/local/tomcat/
```

3. 解压

```
tar -xvf apache-tomcat-8.5.32.tar.gz
```

4. 进入bin目录并启动

```jdk
cd /usr/local/tomcat/apache-tomcat-8.5.32/bin
方式1:
	sh startup.sh
方式2:
	./startup.sh
```

**注意**

提前安装JDK

## 2.3 Tomcat发布JavaWeb应用

### 2.3.1 JavaWeb项目的创建

New Module选择Java Enterprise，勾选Web Application

### 2.3.2 JavaWeb目录介绍

src：存放Java代码

web：存放项目资源（html css js jsp 图片）

WEB-INF：存放配置（web.xml）

### 2.3.3 IDEA发布JavaWeb项目

1. 点击Run，Edit Configurations

2. 访问路径，在右边的Application context设置

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.01/pics/2.jpg)

3. 中间改成Update resources，下面设置好JRE

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.01/pics/3.jpg)

4. 启动

点击Application Servers

### 2.3.5 war包发布JavaWeb项目

1. 在项目的web路径下打成war包

```
jar -cvf 包名.war 当前目录中哪些资源要打包
```

2. 将war包剪切到tomcat的webapps目录下

3. 启动tomcat即可，会自动解压war包

## 2.4 Tomcat配置

### 2.4.1 修改端口

tomcat默认端口是8080，修改成http默认端口80就不用每次都输入了

在conf目录下的server.xml中

```xml
<Connector port="80" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />
```

### 2.4.2 配置虚拟目录

作用：发布任意目录下项目，不一定在webapps中

**第一种方式：**在server.xml的<Host>标签中加一个`<Context path="" docBase=""/>`标签。
path：访问资源URI。URI名称可以随便起，但是必须在前面加上一个/

docBase：资源所在的磁盘物理地址。

### 2.4.3 配置虚拟主机

在server.xml的<Engine>标签中添加一个<Host>标签

name：访问的虚拟主机的名称

appBase：项目存放目录

unparkWARs：是否自动解压

autoDeploy：是否自动部署

<Host>标签中还有一个<Context>标签

```xml
<Host name="www.itcast.cn" appBase="D:\itcastapps" unpackWARs="true" autoDeploy="true">
	<Context path="" docBase="项目名"/>
</Host>

<Host name="www.itheima.com" appBase="D:\itheimaapps" unpackWARs="true" autoDeploy="true"/>
```

最后在windows的hosts文件中绑定域名 127.0.0.1

# 3. HTTP协议

## 3.1 HTTP概述

HTTP的全称是：Hyper Text Transfer Protocol，意为 超文本传输协议。

## 3.2 HTTP协议组成

### 3.2.1 请求部分

请求行： 永远位于请求的第一行
请求消息头： 从第二行开始，到第一个空行结束
请求的正文： 从第一个空行后开始，到正文的结束

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.01/pics/4.jpg)

### 3.2.2 响应部分

响应行： 永远位于响应的第一行
响应消息头： 从第二行开始，到第一个空行结束
响应的正文： 从第一个空行后开始，到正文的结束

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.01/pics/5.jpg)

### 3.2.3 消息头比较

消息头名称首字母大写，多个单词每个单词的首字母都大写。
多个单词用<b><font color='red'>-</font></b>分隔
名称和值之间用<b><font color='red'>冒号加空格</font></b>分隔
多个值之间用<b><font color='red'>逗号加空格</font></b>分隔
两个头之间用<b><font color='red'>回车</font></b>分隔

## 3.3 请求部分详解

### 3.3.1 请求行详解

请求行：`GET /myapp/2.html HTTP/1.1`

| 内容          | 说明                       |
| ------------- | -------------------------- |
| GET           | 请求的方式。（还有POST）   |
| /myapp/2.html | 请求的资源。               |
| HTTP/1.1      | 使用的协议，及协议的版本。 |

### 3.3.2 请求消息头详解

| 内容                   | 说明                                                         |
| ---------------------- | ------------------------------------------------------------ |
| Accept                 | 告知服务器，客户浏览器所支持的MIME类型。                     |
| Accept-Encoding        | 告知服务器，客户浏览器所支持的压缩编码格式。最常用的就是gzip压缩。 |
| Accept-Language        | 告知服务器，客户浏览器所支持的语言。一般都是zh_CN或en_US等。 |
| Referer                | 告知服务器，当前请求的来源。<br/>只有当前请求有来源的时候，才有这个消息头。从地址栏输入的没有来源。<br/>作用：1 投放广告  2 防盗链 |
| Content-Type           | 告知服务器，请求正文的MIME类型。                             |
| Content-Length         | 告知服务器，请求正文的长度。                                 |
| User-Agent             | 浏览器相关信息                                               |
| Connection: Keep-Alive | 连接的状态：保持连接                                         |
| If-Modified-Since      | 告知服务器，客户浏览器缓存文件的最后修改时间。               |
| Cookie（********）     | 会话管理相关，非常的重要。                                   |

### 3.3.3 请求正文详解

第一：只有post请求方式，才有请求的正文。get方式的正文是在地址栏中的。
第二：表单的输入域有name属性的才会被提交。不分get和post的请求方式。
第三：表单的enctype属性取值决定了请求正文的体现形式。概述的含义是：请求正文的MIME编码类型。

| enctype取值                       | 请求正文体现形式                                   | 示例                                                         |
| --------------------------------- | -------------------------------------------------- | ------------------------------------------------------------ |
| application/x-www-form-urlencoded | key=value&key=value                                | username=test&password=1234                                  |
| multipart/form-data               | 此时变成了多部分表单数据。多部分是靠分隔符分隔的。 | -----------------------------7df23a16c0210<br/>Content-Disposition: form-data; name="username"<br/><br/>test<br/>-----------------------------7df23a16c0210<br/>Content-Disposition: form-data; name="password"<br/><br/>1234<br/>-----------------------------7df23a16c0210<br/>Content-Disposition: form-data; name="headfile"; filename="C:\Users\zhy\Desktop\请求部分.jpg"<br/>Content-Type: image/pjpeg<br/>-----------------------------7df23a16c0210 |

## 3.4 响应部分详解

### 3.4.1 响应行详解

响应行：`HTTP/1.1 200 OK`

| 内容     | 说明             |
| -------- | ---------------- |
| HTTP/1.1 | 使用协议的版本。 |
| 200      | 响应状态码       |
| OK       | 状态码描述       |

常用状态码介绍：

| 状态码  | 说明                                             |
| ------- | ------------------------------------------------ |
| 200     | 一切都OK>                                        |
| 302/307 | 请求重定向(客户端行为，两次请求，地址栏发生改变) |
| 304     | 请求资源未发生变化，使用缓存                     |
| 404     | 请求资源未找到                                   |
| 500     | 服务器错误                                       |

### 3.4.2 响应消息头详解

| 消息头                  | 说明                                                         |
| ----------------------- | ------------------------------------------------------------ |
| Location                | 请求重定向的地址，常与302,307配合使用。                      |
| Server                  | 服务器相关信息。                                             |
| Content-Type            | 告知客户浏览器，响应正文的MIME类型。                         |
| Content-Length          | 告知客户浏览器，响应正文的长度。                             |
| Content-Encoding        | 告知客户浏览器，响应正文使用的压缩编码格式。常用的gzip压缩。 |
| Content-Language        | 告知客户浏览器，响应正文的语言。zh_CN或en_US等等。           |
| Content-Disposition     | 告知客户浏览器，以下载的方式打开响应正文。                   |
| Refresh                 | 定时刷新                                                     |
| Last-Modified           | 服务器资源的最后修改时间。                                   |
| Set-Cookie（*******）   | 会话管理相关，非常的重要                                     |
| Expires:-1              | 服务器资源到客户浏览器后的缓存时间                           |
| Catch-Control: no-catch | 不要缓存，//针对http协议1.1版本                              |
| Pragma:no-catch         | 不要缓存，//针对http协议1.0版本                              |

### 3.4.3 响应正文详解

就和我们在浏览器上右键查看源文件看到的内容是一样的。

```html
<html>
    <head>
        <link rel="stylesheet" href="css.css" type="text/css">
        <script type="text/javascript" src="demo.js"></script>
    </head>
    <body>
        <img src="1.jpg" />
    </body>
</html>
```

# 4. 案例演示

## 4.1 静态资源案例

**实现步骤：**

1. 在IDEA中创建一个JavaWeb项目
2. 将静态资源拷贝到项目的web目录下
3. 在web.xml配置文件中，修改默认主页

添加一个<welcome-file-list>标签

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.01/pics/6.png)

4. 启动tomcat，测试

## 4.2 动态资源案例

**Servlet简介：**

Servlet是运行在Java服务器端的程序，用于接收和响应来自客户端基于HTTP协议的请求。

**实现步骤：**

1. 在IDEA中创建一个JavaWeb项目

2. 将静态资源拷贝到项目的web目录下

3. 在web.xml配置文件中，修改默认主页

4. 在项目的src路径下编写一个类，实现Servlet接口

5. 重写service方法，输出一句话即可

6. 在web.xml配置文件中，配置Servlet相关信息

   ![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.01/pics/7.png)

7. 启动tomcat，测试

**注意：**

JDK版本不要太高，否则IDEA版本也要更新

