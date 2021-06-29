# 1. 自动配置原理入门

## 1.1 引导加载自动配置类

查看@SpringBootApplication源码：

```java
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })

# public @interface SpringBootApplication{}
```

### 1.1.1 @SpringBootConfiguration

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
public @interface SpringBootConfiguration {
    @AliasFor(
        annotation = Configuration.class
    )
    boolean proxyBeanMethods() default true;
}
```

@Configuration代表当前是一个配置类

### 1.1.2 @ComponentScan

指定扫描哪些Spring注解

### 1.1.3 @EnableAutoConfiguration

```java
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {}
```

#### 1.1.3.1 @AutoConfigurationPackage

```java
@Import(AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage {
}
```

Debug查看

```java
@Override
public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
   register(registry, new PackageImports(metadata).getPackageNames().toArray(new String[0]));
}
```

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.29/pics/1.png)

1. 利用Registrar给容器中导入一系列组件

2. 将指定的一个包下的所有组件导入进来(MainApplication所在包下)

#### 1.1.3.2 @Import(AutoConfigurationImportSelector.class)

1. 利用getAutoConfigurationEntry(annotationMetadata);给容器中批量导入一些组件
2. 调用List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes)获取到所有需要导入到容器中的配置类
3. 利用工厂加载 Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader)；得到所有的组件
4. 从META-INF/spring.factories位置来加载一个文件
   + 默认扫描我们当前系统里面所有META-INF/spring.factories位置的文件
   + spring-boot-autoconfigure-2.3.4.RELEASE.jar包里面也有META-INF/spring.factories

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.29/pics/2.png)上面文件里面写死了spring-boot一启动就要给容器中加载的所有配置类

## 1.2 按需开启自动配置项

虽然我们127个场景的所有自动配置启动的时候默认全部加载。xxxxAutoConfiguration
按照条件装配规则（@Conditional），最终会按需配置

如AopAutoConfiguration类：

```java
@Configuration(
    proxyBeanMethods = false
)
@ConditionalOnProperty(
    prefix = "spring.aop",
    name = "auto",
    havingValue = "true",
    matchIfMissing = true
)
public class AopAutoConfiguration {
    public AopAutoConfiguration() {
    }
	...
}
```

## 1.3 修改默认配置

以DispatcherServletAutoConfiguration的内部类DispatcherServletConfiguration为例子：

```java
@Bean
@ConditionalOnBean(MultipartResolver.class)  //容器中有这个类型组件
@ConditionalOnMissingBean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME) //容器中没有这个名字 multipartResolver 的组件
public MultipartResolver multipartResolver(MultipartResolver resolver) {
	//给@Bean标注的方法传入了对象参数，这个参数的值就会从容器中找。
	//SpringMVC multipartResolver。防止有些用户配置的文件上传解析器不符合规范
	// Detect if the user has created a MultipartResolver but named it incorrectly
	return resolver;//给容器中加入了文件上传解析器；
}
```

SpringBoot默认会在底层配好所有的组件，但是**如果用户自己配置了以用户的优先**

总结：

- SpringBoot先加载所有的自动配置类  xxxxxAutoConfiguration
- 每个自动配置类按照条件进行生效，默认都会绑定配置文件指定的值。xxxxProperties里面拿。xxxProperties和配置文件进行了绑定

- 生效的配置类就会给容器中装配很多组件
- 只要容器中有这些组件，相当于这些功能就有了

- 定制化配置
  + 用户直接自己@Bean替换底层的组件
  + 用户去看这个组件是获取的配置文件什么值就去修改

**xxxxxAutoConfiguration —> 配置组件 —> xxxxProperties里面拿值 ----> application.properties**

## 1.4 最佳实践

- 引入场景依赖
  + <https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-starter>
- 查看自动配置了哪些（选做）
  + 自己分析，引入场景对应的自动配置一般都生效了
  + 配置文件中debug=true开启自动配置报告。Negative（不生效）\Positive（生效）

- 是否需要修改

  + 参照文档修改配置项

    + <https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html#common-application-properties>

    + 自己分析。xxxxProperties绑定了配置文件的哪些。

  + 自定义加入或者替换组件
    + @Bean、@Component…

  + 自定义器  **XXXXXCustomizer**
  + ......

# 2. 开发小技巧

## 2.1 Lombok

简化JavaBean开发，用注解方式代替构造器、getter/setter、toString()等代码

引入依赖：

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

IDEA中File->Settings->Plugins，搜索安装Lombok插件

User

```java
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class User {

    private String name;
    private Integer age;

    private Pet pet;

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

}
```

简化日志开发，@Slf4j

```java
@Slf4j
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String handle01(){
        log.info("请求进来了....");
        return "Hello, Spring Boot 2!";
    }

}
```

## 2.2 dev-tools

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

在IDEA中，项目或者页面修改以后：Ctrl+F9

## 2.3 Spring Initailizr（项目初始化向导）

### 2.3.1 创建新工程

在IDEA中，菜单栏New -> Project -> Spring Initailizr

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.29/pics/3.png)

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.29/pics/4.png)

### 2.3.2 自动依赖引入

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.29/pics/5.png)

### 2.3.3 自动创建项目结构

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.29/pics/6.png)

### 2.3.4 自动编写好主配置类

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/SpringBoot2/2021.06.29/pics/7.png)

# 3. 配置文件

## 3.1 properties

同以前的properties用法

## 3.2 yaml

### 3.2.1 简介

YAML 是 "YAML Ain't Markup Language"（YAML 不是一种标记语言）的递归缩写。在开发的这种语言时，YAML 的意思其实是："Yet Another Markup Language"（仍是一种标记语言）

非常适合用来做以数据为中心的配置文件

### 3.2.2 基本语法

- key: value；kv之间有空格
- 大小写敏感

- 使用缩进表示层级关系
- 缩进不允许使用tab，只允许空格

- 缩进的空格数不重要，只要相同层级的元素左对齐即可
- '#'表示注释

- 字符串无需加引号，如果要加，''与""表示字符串内容 会被 转义/不转义

### 3.2.3 数据类型

- 字面量：单个的、不可再分的值。date、boolean、string、number、null

```yaml
k: v
```

- 对象：键值对的集合。map、hash、set、object

```yaml
#行内写法：  

k: {k1:v1,k2:v2,k3:v3}

#或

k: 
  k1: v1
  k2: v2
  k3: v3
```

- 数组：一组按次序排列的值。array、list、queue

```yaml
#行内写法：  

k: [v1,v2,v3]

#或者

k:
 - v1
 - v2
 - v3
```

### 3.2.4 示例

JavaBean：

```java
@Data
public class Person {
    private String userName;
    private Boolean boss;
    private Date birth;
    private Integer age;
    private Pet pet;
    private String[] interests;
    private List<String> animal;
    private Map<String, Object> score;
    private Set<Double> salarys;
    private Map<String, List<Pet>> allPets;
}

@Data
public class Pet {
    private String name;
    private Double weight;
}
```

yaml配置文件：

```yaml
person:
#  单引号会将 \n作为字符串输出   双引号会将\n 作为换行输出
#  双引号不会转义，单引号会转义
  boss: true
  birth: 2019/12/9
  age: 18
#  interests: [篮球,足球]
  interests:
    - 篮球
    - 足球
    - 18
  animal: [阿猫,阿狗]
#  score:
#    english: 80
#    math: 90
  score: {english:80,math:90}
  salarys:
    - 9999.98
    - 9999.99
  pet:
    name: 阿狗
    weight: 99.99
  allPets:
    sick:
      - {name: 阿狗,weight: 99.99}
      - name: 阿猫
        weight: 88.88
      - name: 阿虫
        weight: 77.77
    health:
      - {name: 阿花,weight: 199.99}
      - {name: 阿明,weight: 199.99}
  user-name: zhangsan
```

测试controller：

```java
@RestController
public class HelloController {
    @Autowired
    Person person;

    @RequestMapping("/person")
    public Person person(){

        String userName = person.getUserName();
        System.out.println(userName);
        return person;
    }
}
```

## 3.3 提示功能

自定义的类和配置文件绑定一般没有提示。若要提示，添加如下依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>

<!-- 下面插件作用是工程打包时，不将spring-boot-configuration-processor打进包内，让其只在编码的时候有用 -->
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludes>
                    <exclude>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-configuration-processor</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

