# 1. 系统要求

- [Java 8](https://www.java.com/) & 兼容java14
- Maven 3.3+

- idea 2019.1.2

# 2. Maven设置

```xml
<mirrors>
      <mirror>
        <id>nexus-aliyun</id>
        <mirrorOf>central</mirrorOf>
        <name>Nexus aliyun</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public</url>
      </mirror>
  </mirrors>

  <profiles>
         <profile>
              <id>jdk-1.8</id>
              <activation>
                <activeByDefault>true</activeByDefault>
                <jdk>1.8</jdk>
              </activation>
              <properties>
                <maven.compiler.source>1.8</maven.compiler.source>
                <maven.compiler.target>1.8</maven.compiler.target>
                <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
              </properties>
         </profile>
  </profiles>
```

# 3. HelloWorld

需求：浏览发送/hello请求，响应 Hello，Spring Boot 2

## 3.1 创建Maven工程

com.atguigu

名称：boot-01-helloworld

## 3.2 引入依赖

pom.xml

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.4.RELEASE</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

## 3.3 创建主程序

```java
/**
 * 主程序类;主配置类
 * @SpringBootApplication：这是一个SpringBoot应用
 */
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
```

## 3.4 编写业务

```java
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String handle01(){
        return "Hello, Spring Boot 2!";
    }
}
```

## 3.5 测试

直接运行main方法

## 3.6 简化配置

application.properties

```properties
server.port=8888
```

## 3.7 简化部署

pom.xml

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

把项目打成jar包，直接在目标服务器执行即可

注意点：取消掉cmd的快速编辑模式

# 4. 依赖管理

## 4.1 父项目做依赖管理

```xml
依赖管理    
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.4.RELEASE</version>
</parent>

他的父项目
 <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>2.3.4.RELEASE</version>
  </parent>

几乎声明了所有开发中常用的依赖的版本号,自动版本仲裁机制
```

## 4.2 开发导入starter场景启动器

1. 见到很多 spring-boot-starter-* ： *就某种场景
2. 只要引入starter，这个场景的所有常规需要的依赖我们都自动引入
3. SpringBoot所有支持的场景
   https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-starter
4. 见到的  *-spring-boot-starter： 第三方为我们提供的简化开发的场景启动器
5. 所有场景启动器最底层的依赖

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter</artifactId>
  <version>2.3.4.RELEASE</version>
  <scope>compile</scope>
</dependency>
```

## 4.3 无需关注版本号，自动版本仲裁

1. 引入依赖默认都可以不写版本
2. 引入非版本仲裁的jar，要写版本号。

## 4.4 可以修改默认版本号

1. 查看spring-boot-dependencies里面规定当前依赖的版本用的key
2. 在当前项目里面重写配置

```xml
<properties>
	<mysql.version>5.1.43</mysql.version>
</properties>
```

# 5. 自动配置

## 5.1 自动配置好Tomcat，SpringMVC，Web等

- 自动配好Tomcat
  + 引入Tomcat依赖
  + 配置Tomcat

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-tomcat</artifactId>
  <version>2.3.4.RELEASE</version>
  <scope>compile</scope>
</dependency>
```

- 自动配好SpringMVC
  + 引入SpringMVC全套组件
  + 自动配好SpringMVC常用组件（功能）

+ 自动配好Web常见功能，如：字符编码问题
  + SpringBoot帮我们配置好了所有web开发的常见场景

## 5.2 默认的包结构

+ 默认的包结构

  + 主程序所在包及其下面的所有子包里面的组件都会被默认扫描进来
  + 无需以前的包扫描配置
  + 想要改变扫描路径，@SpringBootApplication(scanBasePackages=**"com.atguigu"**)

  - 或者@ComponentScan指定扫描路径

```java
@SpringBootApplication
等同于
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("com.atguigu.boot")
```

## 5.3 各种配置拥有默认值

- 各种配置拥有默认值
  + 默认配置最终都是映射到某个类上，如：MultipartProperties
  + 配置文件的值最终会绑定每个类上，这个类会在容器中创建对象

## 5.4 按需加载所有自动配置项

- 按需加载所有自动配置项
  + 非常多的starter
  + 引入了哪些场景这个场景的自动配置才会开启
  + SpringBoot所有的自动配置功能都在 spring-boot-autoconfigure 包里面

# 6. 组件添加

## 6.1 @Configuration

Full模式与Lite模式

- 配置类组件之间无依赖关系用Lite模式加速容器启动过程，减少判断

- 配置类组件之间有依赖关系，方法会被调用得到之前单实例组件，用Full模式

配置代码：

```java
/**
 * 1、配置类里面使用@Bean标注在方法上给容器注册组件，默认也是单实例的
 * 2、配置类本身也是组件
 * 3、proxyBeanMethods：代理bean的方法
 *      Full(proxyBeanMethods = true)、【保证每个@Bean方法被调用多少次返回的组件都是单实例的】
 *      Lite(proxyBeanMethods = false)【每个@Bean方法被调用多少次返回的组件都是新创建的】
 *      组件依赖必须使用Full模式默认。其他默认是否Lite模式
 *
 */

@Configuration(proxyBeanMethods = true) //告诉SpringBoot这是一个配置类 == 配置文件
public class MyConfig {

    @Bean //给容器中添加组件。以方法名作为组件的id。返回类型就是组件类型。返回的值，就是组件在容器中的实例
    public User user01(){
        User zhangsan = new User("zhangsan", 18);
        //user组件依赖了Pet组件
        zhangsan.setPet(tomcatPet());
        return zhangsan;
    }

    @Bean("tom")
    public Pet tomcatPet(){
        return new Pet("tomcat");
    }

}
```

测试代码：

```java
//@SpringBootApplication
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("com.atguigu.boot")
public class MainApplication {
    public static void main(String[] args) {
        //1、返回我们IOC容器
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

        //2、查看容器里面的组件
        String[] names = run.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }

        //3、从容器中获取组件

        Pet tom01 = run.getBean("tom", Pet.class);

        Pet tom02 = run.getBean("tom", Pet.class);

        System.out.println("组件："+(tom01 == tom02));

        //4、com.atguigu.boot.config.MyConfig$$EnhancerBySpringCGLIB$$51f1e1ca@1654a892
        MyConfig bean = run.getBean(MyConfig.class);
        System.out.println(bean);

        //如果@Configuration(proxyBeanMethods = true)代理对象调用方法。SpringBoot总会检查这个组件是否在容器中有。
        //保持组件单实例
        User user = bean.user01();
        User user1 = bean.user01();
        System.out.println(user == user1);


        User user01 = run.getBean("user01", User.class);
        Pet tom = run.getBean("tom", Pet.class);

        System.out.println("用户的宠物："+(user01.getPet() == tom));
    }
}
```

## 6.2 @Import

@Bean、@Component、@Controller、@Service、@Repository，它们是Spring的基本标签，在Spring Boot中并未改变它们原来的功能

@ComponentScan在Part7有用例

@Import({User.class, DBHelper.class})给容器中**自动创建出这两个类型的组件**、默认组件的名字就是全类名

```java
@Import({User.class, DBHelper.class})
@Configuration(proxyBeanMethods = false) //告诉SpringBoot这是一个配置类 == 配置文件
public class MyConfig {
}
```

测试代码：

```java
//1、返回我们IOC容器
ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

//...

//5、获取组件
String[] beanNamesForType = run.getBeanNamesForType(User.class);

for (String s : beanNamesForType) {
    System.out.println(s);
}

DBHelper bean1 = run.getBean(DBHelper.class);
System.out.println(bean1);
```

## 6.3 @Conditional

条件装配：满足Conditional指定的条件，则进行组件注入

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.28/pics/1.png)

用@ConditionalOnMissingBean举例说明

配置代码：

```java
@ConditionalOnMissingBean(name = "tom")
public class MyConfig {

    @Bean //给容器中添加组件。以方法名作为组件的id。返回类型就是组件类型。返回的值，就是组件在容器中的实例
    public User user01(){
        User zhangsan = new User("zhangsan", 18);
        //user组件依赖了Pet组件
        zhangsan.setPet(tomcatPet());
        return zhangsan;
    }

    @Bean("tom22")
    public Pet tomcatPet(){
        return new Pet("tomcat");
    }

}
```

测试代码：

```java
boolean tom = run.containsBean("tom");
System.out.println("容器中Tom组件："+tom);

boolean user01 = run.containsBean("user01");
System.out.println("容器中user01组件："+user01);

boolean tom22 = run.containsBean("tom22");
System.out.println("容器中tom22组件："+tom22);
```

# 7. 原生配置文件引入

## 7.1 @ImportResource

比如，公司使用bean.xml文件生成配置bean，然而你为了省事，想继续复用bean.xml，@ImportResource粉墨登场

bean.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <bean id="haha" class="com.atguigu.boot.bean.User">
        <property name="name" value="zhangsan"></property>
        <property name="age" value="18"></property>
    </bean>

    <bean id="hehe" class="com.atguigu.boot.bean.Pet">
        <property name="name" value="tomcat"></property>
    </bean>
</beans>
```

配置代码：

```java
@ImportResource("classpath:beans.xml")
public class MyConfig {
...
}
```

测试代码：

```java
boolean haha = run.containsBean("haha");
boolean hehe = run.containsBean("hehe");
System.out.println("haha："+haha);
System.out.println("hehe："+hehe);
```

# 8. 配置绑定

@ConfigurationProperties

绑定配置文件

## 8.1 @ConfigurationProperties + @Component

application.properties

```properties
mycar.brand=BYD
mycar.price=100000
```

Car

```java
@Component
@ConfigurationProperties(prefix = "mycar")
public class Car {
...
}
```

测试代码：

```java
@Autowired
Car car;

@RequestMapping("/car")
public Car car(){
    return car;
}
```

## 8.2 @EnableConfigurationProperties + @ConfigurationProperties(源码中常用)

car

```java
//@Component
@ConfigurationProperties(prefix = "mycar")
public class Car {
...
}
```

配置代码：

```java
@EnableConfigurationProperties(Car.class)
//1、开启Car配置绑定功能
//2、把这个Car这个组件自动注册到容器中
public class MyConfig {
...
}

```