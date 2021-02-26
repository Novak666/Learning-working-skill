# 1. Maven简介

## 1.1 Maven概念

**Maven的本质是一个项目管理工具，将项目开发和管理过程抽象成一个项目对象模型（POM）**

Maven是一个项目管理工具，它包含了一个项目对象模型 (POM：Project Object Model)，一组标准集合，一个项目生命周期(Project Lifecycle)，一个依赖管理系统(Dependency Management System)，和用来运行定义在生命周期阶段(phase)中插件(plugin)目标(goal)的逻辑

Maven说我们需要编写一个pom.xml文件，Maven通过加载这个配置文件就可以知道我们项目的相关信息了！到这里我们知道了Maven离不开一个叫pom.xml的文件。因为这个文件代表就一个项目。

> 提个问题大家思考，如果我们做8个项目，对应的是1个文件，还是8个文件？肯定是8个！

那Maven是如何帮我们进行项目资源管理的呢？这就需要用到Maven中的第二个东西：**依赖管理**。这也是它的第二个核心！

所谓依赖管理就是maven对项目所有依赖资源的一种管理，它和项目之间是一种双向关系，**即当我们做项目的时候maven的依赖管理可以帮助你去管理你所需要的其他资源，当其他的项目需要依赖我们项目的时候，maven也会把我们的项目当作一种资源去进行管理，这就是一种双向关系**。

那maven的依赖管理它管理的这些资源存在哪儿呢？主要有三个位置：本地仓库，私服，中央仓库

## 1.2 Maven能解决的问题

管理jar包，一键构建，自动运行单元测试，维护方便，利于整合发

1. 项目构建：提供标准的，跨平台的自动化构建项目的方式
2. 依赖管理：方便快捷的管理项目依赖的资源（jar包），避免资源间的版本冲突等问题
3. 统一开发结构：提供标准的，统一的项目开发结构

# 2. Maven环境搭建

## 2.1 安装目录结构

解压安装包到任意目录即可完成Maven的安装

目录结构

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.26/pics/1.png)

各目录结构说明：

bin：可执行程序目录

boot：maven自身的启动加载器

conf：maven配置文件的存放目录

lib：maven运行所需库的存放目录

## 2.2 配置环境变量

新建系统变量：MAVEN_HOME 路径

Path变量新建：%MAVEN_HOME%\bin

cmd窗口输入mvn -v查看Maven安装成功

## 2.3 修改参数配置

conf/settings.xml

修改本地仓库地址

```xml
<localRepository>本地仓库地址</localRepository>
```

添加镜像站点

```xml
<mirror>
    <id>nexus-aliyun</id>
    <mirrorOf>central</mirrorOf>
    <name>Nexus aliyun</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public</url>
</mirror>
```

## 2.4 Maven工程目录结构

src/main/java：核心代码

src/main/resources：配置文件

src/test/java：测试代码

src/test/resources：测试配置文件

src/main/webapp：页面资源，js，css，图片等

# 3. Maven仓库

仓库是用来存储jar包的

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.26/pics/2.png)

**中央仓库**：在maven软件中内置一个远程仓库地址https://repo1.maven.org/maven2 ，它是中央仓库，服务于整个互联网，它是由Maven团队自己维护，里面存储了非常全的jar包，它包含了世界上大部分流行的开源项目构件

**远程仓库（私服）**：各公司/部门等小范围内存储资源的仓库，如果本地需要插件或者jar包，本地仓库没有，默认去远程仓库下载，远程仓库也可以从中央仓库获取资源

**本地仓库**：用来存储从远程仓库或中央仓库下载的插件和jar包，项目使用一些插件或jar包，优先从本地仓库查找

可以在MAVE_HOME/conf/settings.xml文件中配置本地仓库位置

**私服的作用：**

1. 保存具有版权的资源，包含购买或自主研发的jar
2. 一定范围内共享资源，能做到仅对内不对外开放

# 4. Maven命令

## 4.1 clean

mvn clean会删除target目录及内容<font color='red'>（接手他人项目前）</font>

## 4.2 compile

mvn compile编译src/main/java下的文件生成target目录及内容

## 4.3 test

mvn test除了编译，还能执行src/test/java下的测试代码

## 4.4 package

对于java工程执行package打成jar包，对于web工程打成war包

packaging：定义资源的打包方式，取值一般有如下三种

（1）jar：该资源打成jar包，默认是jar

（2）war：该资源打成war包

（3）pom：该资源是一个父资源（表明使用maven分模块管理），打包时只生成一个pom.xml不生成jar或其他包结构

## 4.5 install

执行install将Maven打成jar包或war包发布到本地仓库，同时，前面的步骤也会执行

# 5. Maven项目模型概念

Maven包含了一个项目对象模型 (Project Object Model)，一组标准集合，一个项目生命周期(Project Lifecycle)，一个依赖管理系统(Dependency Management System)，和用来运行定义在生命周期阶段(phase)中插件(plugin)目标(goal)的逻辑

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.26/pics/3.png)

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.26/pics/4.png)

## 5.1 项目对象模型(Project Object Model)

一个Maven工程都有一个pom.xml文件，通过pom.xml文件定义项目信息、项目的坐标、项目依赖、插件目标等

## 5.2 依赖管理系统(Dependency Management System)

通过Maven的依赖管理对项目所依赖的jar包进行统一管理

Maven坐标：

**groupId**：定义当前资源隶属组织名称（通常是域名反写，如：org.mybatis；com.itheima）

**artifactId**：定义当前资源的名称（通常是项目或模块名称，如：crm，sms）

**version**：定义当前资源的版本号

查询Maven某一个资源的坐标，https://mvnrepository.com/

## 5.3 一个项目生命周期(Project Lifecycle)

Clean Lifecycle（清理生命周期）在进行真正的构建之前进行一些清理工作（clean）
Default Lifecycle（默认生命周期）构建的核心部分，编译，测试，打包，部署等等（compile，test，package，install，deploy）
Site Lifecycle（站点生命周期）生成项目报告，站点，发布站点

## 5.4 插件(plugin)目标(goal)

Maven管理项目生命周期过程都是基于插件完成的

# 6. IDEA创建Maven项目

## 6.1 IDEA集成Maven插件

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.26/pics/5.png)

## 6.2 使用原型创建Maven的Java项目

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.26/pics/6.png)

手动补齐目录结构

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.26/pics/7.png)

## 6.3 不使用原型创建Maven的Java项目

和上面差不多

## 6.4 使用原型创建Maven的web项目

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.26/pics/8.png)

后面补齐java目录，Mark Directory as Sources Root

以创建一个Servlet为例

在web.xml中添加配置

```xml
<web-app>
  <servlet>
    <servlet-name>MyServlet</servlet-name>
    <servlet-class>com.edu.hdu.servlet.MyServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>MyServlet</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>
</web-app>
```

在pom.xml中添加坐标

```xml
<dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.5</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>jsp-api</artifactId>
      <version>2.0</version>
      <scope>provided</scope>
    </dependency>
  </dependencies>
```

tomcat7插件 运行命令：tomcat7:run

```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
</plugin>
```

java代码

```java
public class MyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/hello.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
```

