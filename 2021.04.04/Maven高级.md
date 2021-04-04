# 1. Maven依赖管理

## 1.1 依赖范围

依赖的jar默认情况可以在任何地方可用，可以通过`scope`标签设定其作用范围

这里的范围主要是指以下三种范围

1. 主程序范围有效（src/main目录范围内）
2. 测试程序范围内有效（src/test目录范围内）
3. 是否参与打包（package指令范围内）

此外：scope标签的取值有四种：compile,test,provided,runtime,system

这5种取值与范围的对应情况如下：

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/1.png)

## 1.2 依赖传递

依赖具有传递性，分两种

1. 直接依赖：在当前项目中通过依赖配置建立的依赖关系
2. 间接依赖：依赖的资源如果依赖其他资源，则表明当前项目间接依赖其他资源

注意：直接依赖和间接依赖其实也是一个相对关系

## 1.3 依赖冲突

由于依赖传递的存在，项目中会存在某些仅仅是版本不同，名称相同的Jar包，这就造成了依赖冲突

**解决**

1. 使用Maven提供的依赖调解原则
   + 第一声明者优先原则
   + 路径近者优先原则
2. 排除依赖
3. 锁定版本

声明优先：在pom文件中定义依赖，以先声明的依赖为准。其实就是根据坐标导入的顺序来确定最终使用哪个传递过来的依赖

路径优先：当依赖中出现相同资源时，层级越深，优先级越低，反之则越高

排除依赖：可以使用exclusions标签将传递过来的依赖排除出去(被排除的资源无需指定版本)

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/2.png)

版本锁定：采用直接锁定版本的方法确定依赖jar包的版本，版本锁定后则不考虑依赖的声明顺序或依赖的路径，以锁定的版本为准添加到工程中，此方法在企业开发中经常使用

版本锁定的使用方式：

1. 在dependencyManagement标签中锁定依赖的版本

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/3.png)

2. 在dependencies标签中声明需要导入的maven坐标

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/4.png)

# 2. 分模块开发与设计

## 2.1 项目中模块与模块的划分

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/5.png)

## 2.2 ssm_pojo

1. 新建模块，可删除resources和test目录
2. 将原项目的pojo内容复制到ssm_pojo模块中，compile编译成功即可

## 2.3 ssm_dao

1. 新建模块
2. 复制的内容有如下
   + dao层接口UserDao
   + 配置文件，与dao相关的有3个：UserDao.xml、applicationContext.xml、jdbc.properties

3. pom.xml文件中引入dao层相关的坐标，删除springmvc和junit的坐标
   + spring(重新打开，因为springmvc已经删除了)
   + mybatis
   + spring整合mybatis
   + mysql
   + druid
   + pagehelper(分页插件在配置中与SqlSessionFactoryBean绑定，需要保留)
   + <font color='red'>导入pojo模块的坐标(对ssm_pojo模块执行install指令，将其安装到本地仓库)</font>

4. applicationContext.xml修改
   + 去掉事务和事务管理

## 2.4 ssm_service

1. 新建模块
2. 复制的内容有如下
   + 业务层接口UserService与实现类UserServiceImpl
   + 配置文件，与service相关的有1个：applicationContext.xml

3. pom.xml文件中引入service层相关的坐标，删除springmvc、mybatis和dao层相关的坐标
   + spring(重新打开，因为springmvc已经删除了)
   + junit相关
   + 直接依赖ssm_dao(对ssm_dao模块执行install指令，将其安装到本地仓库)
   + 间接依赖ssm_pojo(由ssm_dao模块负责依赖关系的建立)

4. applicationContext.xml修改，只保留注解扫描和事务相关内容，并且将该配置文件改名为applicationContext-service.xml(dao模块改为applicationContext-dao.xml)
5. 修改单元测试test下引入的配置文件名称，由单个文件修改为多个文件

## 2.5 ssm_controller

1. 新建模块(使用原型webapp创建)
2. 复制的内容有如下
   + 表现层控制器类UserController与相关设置类(异常)
   + 配置文件，与controller相关的有2个：spring-mvc.xml、web.xml

3. pom.xml文件中引入controller层相关的坐标
   + spring(被springmvc所依赖)
   + springmvc
   + jackon
   + servlet
   + tomcat插件
   + 直接依赖ssm_service(对ssm_service模块执行install指令，将其安装到本地仓库)
   + 间接依赖ssm_dao、ssm_pojo

4. 修改web.xml配置文件中加载spring环境的配置文件名称，使用*通配，加载所有applicationContext-开始的配置文件

## 2.6 小节

- 模块中仅包含当前模块对应的功能类与配置文件
- spring核心配置根据模块功能不同进行独立制作
- 当前模块所依赖的模块通过导入坐标的形式加入当前模块后才可以使用
- web.xml需要加载所有的spring核心配置文件

# 3. 聚合

## 3.1 多模块构建维护

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/6.png)

## 3.2 聚合

作用：聚合用于快速构建maven工程，一次性构建多个项目/模块

步骤：

创建一个新的模块，去掉src，只留一个pom.xml文件。并定义该模块用于进行构建管理

```xml
<packaging>pom</packaging>
```

列出需要管理的模块

```xml
<modules>
    <module>../ssm_controller</module>
    <module>../ssm_service</module>
    <module>../ssm_dao</module>
    <module>../ssm_pojo</module>
</modules>
```

注意：参与聚合操作的模块最终执行顺序与模块间的依赖关系有关，与配置顺序无关

# 4. 继承

作用：通过继承可以实现在子工程中沿用父工程中的配置

Maven中的继承与java中的继承相似，可以在子工程中配置继承关系

## 4.1 模块依赖关系的维护

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/7.png)

## 4.2 父模块

在父模块中要声明依赖管理

```xml
<!--声明此处进行依赖管理-->
<dependencyManagement>
    <!--具体的依赖-->
    <dependencies>
        <!--spring环境-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.9.RELEASE</version>
        </dependency>
        ……
    <dependencies>
        
    <build>
        <pluginManagement>
            <!--设置插件-->
            <plugins>
                <!--具体的插件配置-->
                <plugin>
                    <groupId>org.apache.tomcat.maven</groupId>
                    <artifactId>tomcat7-maven-plugin</artifactId>
                    <version>2.1</version>
                    <configuration>
                        <port>80</port>
                        <path>/</path>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
<dependencyManagement>
```

## 4.3 子模块

在子工程中声明其父工程坐标与对应的位置

```xml
<!--定义父工程-->
<parent>
    <groupId>com.itheima</groupId>
    <artifactId>ssm</artifactId>
    <version>1.0-SNAPSHOT</version>
    <!--填写父工程的pom文件-->
    <relativePath>../ssm/pom.xml</relativePath>
</parent>

<modelVersion>4.0.0</modelVersion>

<artifactId>ssm_pojo</artifactId>
```

使用时，子工程中无需声明依赖版本，版本参照父工程中依赖的版本

```xml
<dependencies>
    <!--spring环境-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
    </dependency>
    ……
</dependencies>
```

## 4.4 可继承的资源

groupId：项目组ID，项目坐标的核心元素

version：项目版本，项目坐标的核心因素

description：项目的描述信息

organization：项目的组织信息

inceptionYear：项目的创始年份

url：项目的URL地址

developers：项目的开发者信息

contributors：项目的贡献者信息

distributionManagement：项目的部署配置

issueManagement：项目的缺陷跟踪系统信息

ciManagement：项目的持续集成系统信息

scm：项目的版本控制系统西溪

malilingLists：项目的邮件列表信息

properties：自定义的Maven属性

dependencies：项目的依赖配置

dependencyManagement：项目的依赖管理配置

repositories：项目的仓库配置

build：包括项目的源码目录配置、输出目录配置、插件配置、插件管理配置等

reporting：包括项目的报告输出目录配置、报告插件配置等

## 4.5 继承和聚合

作用

- 聚合用于快速构建项目
- 继承用于快速配置

相同点：

- 聚合与继承的pom.xml文件打包方式均为pom，可以将两种关系制作到同一个pom文件中
- 聚合与继承均属于设计型模块，并无实际的模块内容

不同点：

- 聚合是在当前模块中配置关系，聚合可以感知到参与聚合的模块有哪些
- 继承是在子模块中配置关系，父模块无法感知哪些子模块继承了自己

# 5. 属性

## 5.1 版本统一

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/8.png)

## 5.2 自定义属性

作用

- 等同于定义变量，方便统一维护

定义格式：

```xml
<!--定义自定义属性-->
<properties>
    <spring.version>5.1.9.RELEASE</spring.version>
    <junit.version>4.12</junit.version>
</properties>
```

调用格式：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>${spring.version}</version>
</dependency>
```

## 5.3 内置属性

作用

- 使用maven内置属性，快速配置

调用格式：

```xml
${basedir}
${version}
```

## 5.4 Setting属性

作用

- 使用Maven配置文件setting.xml中的标签属性，用于动态配置

调用格式：

```xml
${settings.localRepository} 
```

## 5.5 Java系统属性

作用

- 读取Java系统属性

调用格式

```
${user.home} 
```

系统属性查询方式

```
mvn help:system
```

## 5.6 环境变量属性

作用

- 使用Maven配置文件setting.xml中的标签属性，用于动态配置

调用格式

```
${env.JAVA_HOME} 
```

环境变量属性查询方式

```
mvn help:system
```

# 6. 版本管理

## 6.1 不同版本

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/9.png)

## 6.2 版本区分

- SNAPSHOT（快照版本）
  - 项目开发过程中，为方便团队成员合作，解决模块间相互依赖和时时更新的问题，开发者对每个模块进行构建的时候，输出的临时性版本叫快照版本（测试阶段版本）
  - u快照版本会随着开发的进展不断更新
- RELEASE（发布版本）
  - u项目开发到进入阶段里程碑后，向团队外部发布较为稳定的版本，这种版本所对应的构件文件是稳定的，即便进行功能的后续开发，也不会改变当前发布版本内容，这种版本称为发布版本

## 6.3 工程版本号约定

约定规范：

- <主版本>.<次版本>.<增量版本>.<里程碑版本>
- 主版本：表示项目重大架构的变更，如：spring5相较于spring4的迭代
- 次版本：表示有较大的功能增加和变化，或者全面系统地修复漏洞
- 增量版本：表示有重大漏洞的修复
- 里程碑版本：表明一个版本的里程碑（版本内部）。这样的版本同下一个正式版本相比，相对来说不是很稳定，有待更多的测试

范例：

- 5.1.9.RELEASE

# 7. 资源配置

## 7.1 资源配置的多文件维护

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/10.png)

## 7.2 配置文件引用pom.xml属性

在任意配置文件中加载pom文件中定义的属性，即pom.xml集中管理配置文件

自定义属性值

```xml
<!--定义自定义属性-->
<properties>
    <spring.version>5.1.9.RELEASE</spring.version>
    <junit.version>4.12</junit.version>
    <jdbc.url>jdbc:mysql://127.0.0.1:3306/ssm_db</jdbc.url>
</properties>
```

调用格式

```
${jdbc.url} 
```

开启配置文件加载pom属性(build标签中)

```xml
<!--配置资源文件对应的信息-->
<resources>
    <resource>
        <!--设定配置文件对应的位置目录，支持使用属性动态设定路径-->
        <directory>${project.basedir}/src/main/resources</directory>
        <!--开启对配置文件的资源加载过滤-->
        <filtering>true</filtering>
    </resource>
</resources>

<!--配置测试资源文件对应的信息-->
<testResources>
    <testResource>
        <!--设定配置文件对应的位置目录，支持使用属性动态设定路径-->
        <directory>${project.basedir}/src/test/resources</directory>
        <!--开启对配置文件的资源加载过滤-->
        <filtering>true</filtering>
    </testResource>
</testResources>
```

# 8. 多环境开发配置

## 8.1 多环境兼容

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/11.png)

## 8.2 多环境配置

```xml
<!--创建多环境-->
<profiles>
    <!--定义具体的环境：生产环境-->
    <profile>
        <!--定义环境对应的唯一名称-->
        <id>pro_env</id>
        <!--定义环境中专用的属性值-->
        <properties>
            <jdbc.url>jdbc:mysql://127.1.1.1:3306/ssm_db</jdbc.url>
        </properties>
        <!--设置默认启动-->
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
    </profile>
    <!--定义具体的环境：开发环境-->
    <profile>
        <id>dev_env</id>
        ……
    </profile>
</profiles>
```

## 8.3 加载指定环境

调用格式

```
mvn 指令 –P 环境定义id
```

范例

```
mvn install –P pro_env
```

# 9. 跳过测试

## 9.1 应用场景

+ 整体模块功能未开发

+ 模块中某个功能未开发完毕

+ 单个功能更新调试导致其他功能失败

+ 快速打包

+ ……

## 9.2 idea界面操作跳过测试

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.04/pics/12.png)

## 9.3 命令跳过测试

```
mvn 指令 –D skipTests
```

注意：执行的指令生命周期必须包含测试环节

## 9.4 配置跳过测试

build标签中

```xml
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.22.1</version>
    <configuration>
        <skipTests>true</skipTests><!--设置跳过测试-->
        <includes> <!--包含指定的测试用例-->
            <include>**/User*Test.java</include>
        </includes>
        <excludes><!--排除指定的测试用例-->
            <exclude>**/User*TestCase.java</exclude>
        </excludes>
    </configuration>
</plugin>
```

# 10. 私服

## 10.1 私服简介

Maven仓库分为本地仓库和远程仓库，而远程仓库又分为Maven中央仓库、其他远程仓库和私服(私有服务
器)。其中，中央仓库是由Maven官方提供的，而私服就需要我们自己搭建了

Maven私服就是公司局域网内的Maven远程仓库，每个员工的电脑上安装Maven软件并且连接Maven私服，程序员可以将自己开发的项目打成jar并发布到私服，其它项目组成员就可以从私服下载所依赖的jar。私服还充当一个代理服务器的角色，当私服上没有jar包时会从Maven中央仓库自动下载

## 10.2 Nexus

Nexus是一个Maven仓库管理器(其实就是一个软件)，Nexus可以充当Maven私服，同时Nexus还提供强大的仓
库管理、构件搜索等功能

## 10.3 搭建Nexus私服



## 10.4 仓库分类

通过前面的仓库列表可以看到，nexus默认内置了很多仓库，这些仓库可以划分为4种类型，每种类型的仓库用于
存放特定的jar包，具体说明如下：

1. hosted，宿主仓库，部署自己的jar到这个类型的仓库，包括Releases和Snapshots两部分，Releases为公司内
   部发布版本仓库、 Snapshots为公司内部测试版本仓库
2. proxy，代理仓库，用于代理远程的公共仓库，如maven中央仓库，用户连接私服，私服自动去中央仓库下载jar包或者插件
3. group，仓库组，用来合并多个hosted/proxy仓库，通常我们配置自己的maven连接仓库组
4. virtual(虚拟)：兼容Maven1版本的jar或者插件

## 10.5 将项目发布到Maven私服

1. 配置本地仓库访问私服的权限(Maven的settings.xml文件)

   在services标签中

```xml
<servers>
    <server>
        <id>heima-release</id>
        <username>admin</username>
        <password>admin123</password>
    </server>
    <server>
        <id>heima-snapshots</id>
        <username>admin</username>
        <password>admin123</password>
    </server>
</servers>
```

注意：一定要在idea工具中引入的Maven的settings.xml文件中配置

2. 配置项目的pom.xml文件

```xml
<distributionManagement>
    <repository>
        <id>heima-release</id>
        <url>http://localhost:8081/repository/heima-release/</url>
    </repository>
    <snapshotRepository>
        <id>heima-snapshots</id>
        <url>http://localhost:8081/repository/heima-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

3. 发布项目到私服

   命令

```
mvn deploy
```

## 10.6 从私服下载jar包到本地仓库

1. 在Maven的settings.xml文件中配置下载模板

   在profiles标签中

```xml
<profiles>
	<!--配置私服下载jar包的模板 开始-->
	<profile>  
    <!--profile的id-->
    <id>dev</id>  
    <repositories>  
      <repository> 
        <id>nexus</id>  
        <!--仓库地址，即nexus仓库组的地址-->
        <url>http://localhost:8081/nexus/content/groups/public/</url>  
        <!--是否下载releases构件-->
        <releases>  
          <enabled>true</enabled>  
        </releases>  
        <!--是否下载snapshots构件-->
        <snapshots>  
          <enabled>true</enabled>  
        </snapshots>  
      </repository>  
    </repositories> 
     <pluginRepositories> 
        <!-- 插件仓库，maven的运行依赖插件，也需要从私服下载插件 -->
        <pluginRepository> 
            <id>public</id> 
            <name>Public Repositories</name> 
            <url>http://localhost:8081/nexus/content/groups/public/</url> 
        </pluginRepository> 
    </pluginRepositories> 
</profile> 
	<!--配置私服下载jar包的模板 结束-->
</profiles>
```

2. 在Maven的settings.xml文件中配置激活下载模板

```xml
<activeProfiles>
    <activeProfile>dev</activeProfile>
</activeProfiles>
```

## 10.7 将第三方jar包安装到本地仓库

但是并不是所有的jar包都可以从中央仓库下载到，比如常用的Oracle数据库驱动的jar包在中央仓库就不存在。此时需要到Oracle的官网下载驱动jar包，然后将此jar包通过Maven命令安装到我们本地的Maven仓库或者Maven私服中

1. 下载Oracle的jar包
2. mvn install命令安装

mvn install:install-file -Dfile=ojdbc14-10.2.0.4.0.jar -DgroupId=com.oracle -DartifactId=ojdbc14 –Dversion=10.2.0.4.0 -Dpackaging=jar

## 10.8 将第三方jar包安装到Maven私服

1. 下载Oracle的jar包
2. 在Maven的settings.xml配置文件中配置第三方仓库的server信息

```xml
<servers>
    <server>
        <id>thirdparty</id>
        <username>admin</username>
        <password>admin123</password>
    </server>
</servers>
```

3. 执行mvn deploy命令进行安装

mvn deploy:deploy-file -Dfile=ojdbc14-10.2.0.4.0.jar -DgroupId=com.oracle -DartifactId=ojdbc14 –Dversion=10.2.0.4.0 -Dpackaging=jar –Durl=http://localhost:8081/nexus/content/repositories/thirdparty/ -DrepositoryId=thirdparty