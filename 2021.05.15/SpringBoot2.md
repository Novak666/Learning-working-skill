# 1. Condition接口及相关注解

## 1.1 获取redisTemplate案例演示

问题：

我们之前用过springboot整合redis 实现的步骤：就是添加redis起步依赖之后，直接就可以使用从spring容器中获取注入RedisTemplate对象了，而不需要创建该对象放到spring容器中了。意味着Spring boot redis的起步依赖已经能自动的创建该redisTemplate对象加入到spring容器中了。这里应用的重要的一个点就是condition的应用

我们来演示下，是否加入依赖就可以获取redisTemplate，不加依赖就不会获取到redisTemplate

代码见itheima-springboot-demo01模块

1. 创建工程添加依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itheima</groupId>
    <artifactId>itheima-springboot-demo01-condition</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
    </parent>

    <dependencies>
        <!--加入springboot的starter起步依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>


</project>
```

2. com.itheima下创建启动类

```java
package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MySpringBootApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MySpringBootApplication.class, args);
        Object redisTemplate = context.getBean("redisTemplate");
        System.out.println(redisTemplate);
    }
}
```

3. 启动main方法，查看效果

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/1.png)

4. 注释依赖则报错

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/2.png)

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/3.png)

## 1.2 自定义实现类

需求：

在spring容器中有一个user的bean对象，如果导入了redisclient的坐标则加载该bean，如果没有导入则不加载该bean

实现步骤：

1. 定义一个接口condition的实现类
2. 实现方法 判断是否有字节码对象，有则返回true，没有则返回false
3. 定义一个User的pojo
4. 定义一个配置类用于创建user对象交给spring容器管理
5. 修改加入注解@conditional(value=Condition)
6. 测试打印

3. 创建POJO

```JAVA
public class User {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
```

4. 创建condition的接口实现类

```java
public class OnClassCondition implements Condition {
    /**
     * 返回true 则满足条件  返回false 则不满足条件
     *
     * @param context  上下文信息对象 可以获取环境的信息 和容器工程 和类加载器对象
     * @param metadata 注解的元数据 获取注解的属性信息
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        //1.获取当前的redis的类字节码对象
        try {
            //2.加载成功则说明存在 redis的依赖 返回true，
            Class.forName("redis.clients.jedis.Jedis");
            return true;
        } catch (ClassNotFoundException e) {
            // 如果加载不成功则redis依赖不存在 返回false
            e.printStackTrace();
            return false;
        }
    }
}
```

5. 定义配置类 在com.itheima.config下

```java
package com.itheima.config;

import com.itheima.condition.OnClassCondition;
import com.itheima.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/***
 * 描述
 * @author ljh
 * @packagename com.itheima.config
 * @version 1.0
 * @date 2020/3/2
 */
@Configuration
public class UserConfig {

    @Bean
    //conditinal 用于指定当某一个条件满足并返回true时则执行该方法创建bean交给spring容器
    @Conditional(value = OnClassCondition.class)
    //@ConditionalOnClass(name={"redis.clients.jedis.Jedis"}) SpringBoot自带注解
    public User user() {
        return new User();
    }
}
```

解释：

```properties
@Conditional(value = OnClassCondition.class) 当符合指定类的条件返回true的时候则执行被修饰的方法，放入spring容器中。
```

6. 测试

jedis坐标注释与不注释

```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>3.2.0</version>
</dependency>
```

## 1.3 优化

问题：

我们希望这个类注解可以进行动态的加载某一个类的全路径，不能写死为redis(硬编码)，将来可以进行重用

需求：

1. 可以自定义一个注解 用于指定具体的类全路径 表示有该类在类路径下时才执行注册
2. 在配置类中使用该自定义注解 动态的指定类路径
3. 在条件的实现类中进行动态的获取并加载类即可

实现步骤：

1. 自定义注解
2. 配置类使用注解
3. 条件实现类中修改方法实现

1. com.itheima.annotation下自定义注解

```java
package com.itheima.annotation;

import com.itheima.condition.OnClassCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/***
 * 描述
 * @author ljh
 * @packagename com.itheima.annotation
 * @version 1.0
 * @date 2020/3/2
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnClassCondition.class)  //注意
public @interface ConditionalOnClass {
    /**
     * 指定所有的类全路径的字符数组
     * @return
     */
    String[] name() default {};
}

```

2. 修改配置类

```java
@Configuration
public class UserConfig {

    @Bean
    //conditinal 用于指定当某一个条件满足并返回true时则执行该方法创建bean交给spring容器
//    @Conditional(value = OnClassCondition.class)
    @ConditionalOnClass(name = "redis.clients.jedis.Jedis")
    public User user() {
        return new User();
    }
}
```

3. 修改实现类

```java
public class OnClassCondition implements Condition {
    /**
     * 返回true 则满足条件  返回false 则不满足条件
     *
     * @param context  上下文信息对象 可以获取环境的信息 和容器工程 和类加载器对象
     * @param metadata 注解的元数据 获取注解的属性信息
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        //1.获取当前的redis的类字节码对象
        /*try {
            //2.加载成功则说明存在 redis的依赖 返回true，
            Class.forName("redis.clients.jedis.Jedis");
            return true;
        } catch (ClassNotFoundException e) {
            // 如果加载不成功则redis依赖不存在 返回false
            e.printStackTrace();
            return false;
        }*/
        //获取注解的信息
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnClass.class.getName());
        //获取注解中的name的方法的数据值
        String[] values = (String[]) annotationAttributes.get("name");
        for (String value : values) {
            try {
                Class.forName(value);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
```

## 1.4 相关的条件的注解说明

常用的注解如下：

ConditionalOnBean   当spring容器中有某一个bean时使用

ConditionalOnClass  当判断当前类路径下有某一个类时使用

ConditionalOnMissingBean 当spring容器中没有某一个bean时才使用

ConditionalOnMissingClass 当当前类路径下没有某一个类的时候才使用

ConditionalOnProperty 当配置文件中有某一个key value的时候才使用

....

condition用于自定义某一写条件类，用于当达到某一个条件时使用。关联的注解为@conditional结合起来使用。当然我们springboot本身已经提供了一系列的注解供我们使用

# 2. 切换内置的web容器(修改依赖坐标即可)

我们知道在springboot启动的时候如果我们使用web起步依赖，那么我们默认就加载了tomcat的类嵌入了tomcat了，不需要额外再找tomcat

代码见itheima-springboot-demo02模块

加载配置tomcat的原理：

1. 加入pom.xml中起步依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

2. 查看依赖图

web起步依赖依赖于spring-boot-starter-tomcat，这个为嵌入式的tomcat的包

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/4.png)

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/5.png)

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/6.png)

启动时为tomcat

3. 可以尝试修改web容器：

如上，我们可以通过修改web容器，根据业务需求使用性能更优越的等等其他的web容器。这里我们演示使用jetty作为web容器

在pom.xml中排出tomcat依赖，添加jetty依赖即可：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <groupId>org.springframework.boot</groupId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

再次启动如下图所示：

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/7.png)

# 3. @SpringBootApplication

@SpringBootApplication包含3个注解：

@SpringbootConfiguration注解是一个@configuration注解，那么意味着我们的启动类被注解修饰后，意味着它本身也是一个配置类，该配置类就可以当做spring中的applicationContext.xml的文件，用于加载配置使用

@ComponentScan注解作用类似于组件扫描包xml中的context-componet-scan，如果不指定扫描路径，那么就扫描该注解修饰的启动类所在的<font color='red'>包以及子包</font>

@EnableAutoConfiguration，那么这种@Enable*开头就是springboot中定义的一些动态启用某些功能的注解，他的底层实现原理实际上用的就是@import注解导入一些配置，自动进行配置，加载Bean

# 4. 加载第三方Bean

需求：

1. 定义两个工程demo2，demo3，demo3中有bean
2. demo2依赖了demo3
3. 我们希望demo2直接获取加载demo3中的bean

demo2和demo3见代码itheima-springboot-demo02-enable模块和itheima-springboot-demo03-enable模块

1. 修改demo2工程的pom.xml，加入demo3的依赖

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/8.png)

demo2加载第三方的依赖中的bean

```java
package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class DemoEnable2Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoEnable2Application.class, args);
        //获取加载第三方的依赖中的bean
        Object user = context.getBean("user");
        System.out.println(user);
    }
}
```

测试发现出错，因为2个项目的包名不一样，扫描不到

2. 解决该错误的方式

第一种使用组件扫描 扫描包路径放大

第二种使用import注解进行导入配置类的方式即可

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/9.png)

**优化：**

1. 在demo03中com.config下，创建一个自定义注解@EnableUser(非常直观)：

```java
package com.config；

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UserConfig.class)
public @interface EnableUser {
    
}
```

2. 在demo2中使用该注解即可

# 5. @import的4种加载Bean的方式

import注解用于导入其他的配置，让spring容器进行加载和初始化。import的注解有以下的几种方式使用：

- 直接导入Bean
- 导入配置类
- 导入ImportSelector的实现类，通常用于加载配置文件中的Bean
- 导入ImportBeanDefinitionRegistrar实现类(将一个类放到Spring容器中)

代码见itheima-springboot-demo03-enable模块

ImportSelector

```java
public class MyImportSelector implements ImportSelector {

    //返回的是类的全路径的字符串数组  用来从例如：properties  xml 等其他的配置文件中加载 配置好的一些类的全路径 这些类交给spring容器进行管理 并进行配置
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //将来这个数据是从配置文件中读取到的，Spring容器根据类的全路径反射生成新对象
        return new String[]{"com.itheima.pojo.Role","com.itheima.pojo.User"};
    }
}
```

ImportBeanDefinitionRegistrar

```java
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    //registerBeanDefinitions 手动将java对象注册给spring容器  例如：将User类型的对象给spring容器管理
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(User.class);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        registry.registerBeanDefinition("abcdefg",beanDefinition);
    }
}
```

测试类

```java
@SpringBootApplication
//获取第三方的Bean的方式   放大包扫描    1种
//@ComponentScan(basePackages = "com")

//获取第三方的Bean的方式   直接导入配置类    2种
//@Import(UserConfig.class)

//@Import(MyImportSelector.class)// importSelector
@Import(MyImportBeanDefinitionRegistrar.class)// ImportBeanDefinitionRegistrar

//获取第三方的Bean的方式   直接使用开启类型的注解 Enable*    3种
//@EnableAutoUser
public class Demo2EnableApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Demo2EnableApplication.class, args);

        //直接获取user对象 user对象已经在demo3中的容器中了
        //Object user = context.getBean("user");
        //Object role = context.getBean("role");

        User user = (User)context.getBean("abcdefg");
        //Role role = context.getBean(Role.class);

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }

        System.out.println(user);
        //System.out.println(role);
    }
}
```

# 6. SpringBoot自定义starter

## 6.1 需求

当一个测试工程(itheima-test-starter)加入redis客户端的坐标的时候，自动配置jedis的bean加载到spring容器中

## 6.2 总体步骤

1. 自定义一个工程作为起步依赖，itheima-redis-springboot-starter
2. 创建一个工程itheima-test-starter依赖上面的工程，自动配置jedis，直接可以使用

## 6.3 具体步骤

1. 创建工程itheima-redis-springboot-starter

该工程创建不需要启动类，不需要测试类，只需要spring-boot-starter以及jedis的依赖坐标

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itheima</groupId>
    <artifactId>itheima-redis-springboot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
    </parent>

    <dependencies>
        <!--springboot的starter-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <!--redis的依赖jedis-->
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>3.2.0</version>
        </dependency>
    </dependencies>


</project>
```

2. 创建自动配置类(读取配置文件，和@ComponentScan不一样)

```java
@Configuration
//启用 POJO 和配置yaml的映射关系 自动进行映射 
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnClass(Jedis.class)
public class RedisAutoConfiguration {
    
    @Bean//默认的情况下 不配置bean的名字 使用方法的名字
    //容器中没有名字为jedis的bean的时候 才执行以下的方法
    @ConditionalOnMissingBean(name = "jedis")
    public Jedis jedis(RedisProperties redisProperties){
        System.out.println("host:"+redisProperties.getHost()+";port:"+redisProperties.getPort());
        return new Jedis(redisProperties.getHost(),redisProperties.getPort());
    }
    
}
```

3. 创建pojo

```java
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {
    private String host = "localhost";
    private Integer port = 6379;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
```

4. 在resources下创建META-INF/spring.factories文件并定义内容如下

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.itheima.redis.config.RedisAutoConfiguration
```

5. 创建测试工程itheima-test-starter，添加依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itheima</groupId>
    <artifactId>itheima-test-starter</artifactId>
    <version>1.0-SNAPSHOT</version>


    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
    </parent>

    <dependencies>
        <!--springboot的起步依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <!--加入itheima的redis的起步依赖-->
        <dependency>
            <groupId>com.itheima</groupId>
            <artifactId>itheima-redis-springboot-starter</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

</project>
```

6. 创建itheima-test-starter工程启动类

```java
@SpringBootApplication
public class ItheimaRedisTestApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ItheimaRedisTestApplication.class, args);
        //Object jedis = context.getBean("jedis");
        //System.out.println(jedis);
    }

    /*@Bean
    public Jedis jedis(){
        return new Jedis("localhost",666);
    }*/

}
```

7. 配置application.yml

8. 测试，注释和不注释以下代码，运行结果不同

```java
@Bean
public Jedis jedis(){
    return new Jedis("localhost",666);
}
```

# 7. SpringBoot的监控

时常我们在使用的项目的时候，想知道相关项目的一些参数和调用状态，而SpringBoot自带监控功能Actuator，可以帮助实现对程序内部运行情况监控，比如监控状况、Bean加载情况、配置属性、日志信息等

## 7.1 Actuator

1. 创建工程，在pom.xml中添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

总体如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.itheima</groupId>
    <artifactId>itheima-actuator</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>itheima-actuator</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>


    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

2. 编写启动类

```java
@SpringBootApplication
public class ItheimaActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItheimaActuatorApplication.class, args);
    }

    @RestController
    @RequestMapping("/test")
    class TestController {

        @GetMapping("/index")
        public String show() {
            return "hello world";
        }
    }

}
```

3. 配置application.properties

```properties
# 配置健康端点开启所有详情信息
management.endpoint.health.show-details=always 
# 设置开放所有web相关的端点信息
management.endpoints.web.exposure.include=*
# 设置info前缀的信息设置
info.name=zhangsan
info.age=18
```

4. 在浏览器输入地址：http://localhost:8080/actuator

监控路径列表说明：

以下展示部分列表

| **路径**        | **描述**                                                     |
| --------------- | ------------------------------------------------------------ |
| /beans          | 描述应用程序上下文里全部的Bean，以及它们的关系               |
| /env            | 获取全部环境属性                                             |
| /env/{name}     | 根据名称获取特定的环境属性值                                 |
| /health         | 报告应用程序的健康指标，这些值由HealthIndicator的实现类提供  |
| /info           | 获取应用程序的定制信息，这些信息由info打头的属性提供         |
| /mappings       | 描述全部的URI路径，以及它们和控制器(包含Actuator端点)的映射关系 |
| /metrics        | 报告各种应用程序度量信息，比如内存用量和HTTP请求计数         |
| /metrics/{name} | 报告指定名称的应用程序度量值                                 |
| /trace          | 提供基本的HTTP请求跟踪信息(时间戳、HTTP头等)                 |

## 7.2 admin

- admin server 用于收集统计所有相关client的注册过来的信息进行汇总展示
- admin client 每一个springboot工程都是一个client 相关的功能展示需要汇总到注册汇总到server

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/10.png)

1. 创建admin server工程itheima-admin-server

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>
   <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>2.1.13.RELEASE</version>
      <relativePath/> <!-- lookup parent from repository -->
   </parent>
   <groupId>com.itheima</groupId>
   <artifactId>ithima-admin-server</artifactId>
   <version>0.0.1-SNAPSHOT</version>
   <name>ithima-admin-server</name>
   <description>Demo project for Spring Boot</description>

   <properties>
      <java.version>1.8</java.version>
      <spring-boot-admin.version>2.1.6</spring-boot-admin.version>
   </properties>

   <dependencies>
      <dependency>
         <groupId>de.codecentric</groupId>
         <artifactId>spring-boot-admin-starter-server</artifactId>
      </dependency>

      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
      </dependency>


      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-test</artifactId>
         <scope>test</scope>
      </dependency>
   </dependencies>

   <dependencyManagement>
      <dependencies>
         <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-dependencies</artifactId>
            <version>${spring-boot-admin.version}</version>
            <type>pom</type>
            <scope>import</scope>
         </dependency>
      </dependencies>
   </dependencyManagement>

   <build>
      <plugins>
         <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
         </plugin>
      </plugins>
   </build>

</project>
```

2. 创建启动类

```java
@SpringBootApplication
@EnableAdminServer
public class IthimaAdminServerApplication {

   public static void main(String[] args) {
      SpringApplication.run(IthimaAdminServerApplication.class, args);
   }

}
```

注意：@EnableAdminServer 该注解用于启用Server功能

3. 修改application.properties文件

```properties
server.port=9000
```

4. 创建admin client 工程itheima-admin-client

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>
   <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>2.1.13.RELEASE</version>
      <relativePath/> <!-- lookup parent from repository -->
   </parent>
   <groupId>com.itheima</groupId>
   <artifactId>ithima-admin-client</artifactId>
   <version>0.0.1-SNAPSHOT</version>
   <name>ithima-admin-client</name>
   <description>Demo project for Spring Boot</description>

   <properties>
      <java.version>1.8</java.version>
      <spring-boot-admin.version>2.1.6</spring-boot-admin.version>
   </properties>

   <dependencies>
      <dependency>
         <groupId>de.codecentric</groupId>
         <artifactId>spring-boot-admin-starter-client</artifactId>
      </dependency>

      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
      </dependency>

      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-test</artifactId>
         <scope>test</scope>
      </dependency>
   </dependencies>

   <dependencyManagement>
      <dependencies>
         <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-dependencies</artifactId>
            <version>${spring-boot-admin.version}</version>
            <type>pom</type>
            <scope>import</scope>
         </dependency>
      </dependencies>
   </dependencyManagement>

   <build>
      <plugins>
         <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
         </plugin>
      </plugins>
   </build>

</project>
```

5. 创建启动类

```java
@SpringBootApplication
public class IthimaAdminClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(IthimaAdminClientApplication.class, args);
    }

    @RestController
    @RequestMapping("/user")
    class TestController {

        @RequestMapping("/findAll")
        public String a() {
            return "aaaa";
        }
    }

}
```

6. 配置application.properties

```properties
# 配置注册到的admin server的地址
spring.boot.admin.client.url=http://localhost:9000
# 启用健康检查 默认就是true
management.endpoint.health.enabled=true
# 配置显示所有的监控详情
management.endpoint.health.show-details=always
# 开放所有端点
management.endpoints.web.exposure.include=*
# 设置系统的名称
spring.application.name=abc
```

7. 启动两个系统，访问路径http://localhost:9000/

# 8. SpringBoot部署项目

在springboot项目中，我们部署项目有两种方式：

- jar包直接通过java命令运行执行
- war包存储在tomcat等servlet容器中执行

## 8.1 jar包部署

1. 新建项目用于测试，pom.xml，注意加spring-boot-maven-plugin插件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itheima</groupId>
    <artifactId>itheima-demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

2. 定义启动类

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

3. 执行Maven打包命令(idea右侧)

4. 复制jar包到任意目录，可以直接执行java命令执行系统(jar包在target目录下)，命令如java -jar itheima-demo-1.0-SNAPSHOT.jar

## 8.2 war包部署

1. pom.xml文件中修改打包方式和相关依赖配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itheima</groupId>
    <artifactId>itheima-demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.4.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <!-- To build an executable war use one of the profiles below -->
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
    <build>
        <finalName>demo</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

2. 修改启动类配置 需要继承SpringBootServletInitializer

```java
@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @RestController
    class TestController{

        @RequestMapping("/hello")
        public String hello(){
            return "hello";
        }
    }
}
```

3. 执行命令打包，之后变成一个war包

4. 复制该war包到tomcat中

5. 浏览器输入地址测试即可

小结：推荐使用jar，特别在微服务领域中使用jar的方式简单许多

# 9. 启动过程

有待后续学习，SpringBoot初始化的Run流程的链接地址：

https://www.processon.com/view/link/59812124e4b0de2518b32b6e

# 10. 自动配置原理

源码分析流程截图：

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/11.png)

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/12.png)

![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/13.png)

![14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/14.png)

![15](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/15.png)

![16](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/16.png)

![17](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/17.png)

1. @import注解 导入配置
2. selectImports导入类中的方法中加载配置返回Bean定义的字符数组
3. 加载META-INF/spring.factories 中获取Bean定义的全路径名返回

# 11. 小节

![18](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.05.15/pics/18.png)