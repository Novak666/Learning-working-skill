# 1. Spring原始注解

## 1.1 原始注解概览

![1](C:\Users\HASEE\Desktop\pics\1.png)

## 1.2 注解扫描

使用注解进行开发时，需要在applicationContent.xml中配置组件扫描，作用是指定哪个包及其子包下的Bean需要进行扫描以识别使用注解配置的类、字段和方法。

配置文件中加入

```xml
<context:component-scan base-package="packageName"/>
```

- 说明：
  - 在进行包所扫描时，会对配置的包及其子包中所有文件进行扫描
  - 扫描过程是以文件夹递归迭代的形式进行的
  - 扫描过程仅读取合法的java文件
  - 扫描时仅读取spring可识别的注解
  - 扫描结束后会将可识别的有效注解转化为spring对应的资源加入IoC容器
- 注意：
  - 无论是注解格式还是XML配置格式，最终都是将资源加载到IoC容器中，差别仅仅是数据读取方式不同
  - 从加载效率上来说注解优于XML配置文件

## 1.3 注解—实例化Bean

- 名称：@Component    @Controller    @Service    @Repository

- 类型：**类注解**

- 位置：类定义上方

- 作用：设置该类为spring管理的bean

- 范例：

  ```java
  @Component
  public class ClassName{}
  ```

- 说明：

  @Controller、@Service 、@Repository是@Component的衍生注解，功能同@Component。@Controller用于web层，@Service用于service层，@Repository用于dao层。

- 相关属性

  - value（默认）：定义bean的访问id

## 1.4 注解—引用属性注入

- 名称：@Autowired、@Qualifier、@Resource

- 类型：**属性注解、方法注解**

- 位置：属性定义上方，方法定义上方

- 作用：设置对应属性的对象或对方法进行引用类型传参

- 范例：

  ```java
  @Autowired(required = false)
  @Qualifier("userDao")
  private UserDao userDao;
  ```

- 说明：

  - @Autowired默认按类型装配，指定@Qualifier后可以指定自动装配的bean的id，@Resource相当于前2个注解的功能

- 相关属性

  - required：定义该属性是否允许为null

+ @Resource相关属性
  - name：设置注入的bean的id
  - type：设置注入的bean的类型，接收的参数为Class类型

## 1.5 注解—非引用属性注入

- 名称：@Value

- 类型：**属性注解、方法注解**

- 位置：属性定义上方，方法定义上方

- 作用：设置对应属性的值或对方法进行传参

- 范例：

  ```java
  @Value("${jdbc.username}")
  private String username;
  ```

- 说明：

  - value值仅支持非引用类型数据，赋值时对方法的所有参数全部赋值
  - value值支持读取properties文件中的属性值，通过类属性将properties中数据传入类中
  - value值支持SpEL（例如@Value("${jdbc.driver}")）
  - @value注解如果添加在属性上方，可以省略set方法（set方法的目的是为属性赋值）

- 相关属性

  - value（默认）：定义对应的属性值或参数值

## 1.6 注解—作用域

- 名称：@Scope

- 类型：**类注解**

- 位置：类定义上方

- 作用：设置该类作为bean对应的scope属性

- 范例：

  ```java
  @Scope
  public class ClassName{}
  ```

- 相关属性

  - value（默认）：定义bean的作用域，默认为singleton

## 1.7 注解—bean的生命周期

- 名称：@PostConstruct、@PreDestroy

- 类型：**方法注解**

- 位置：方法定义上方

- 作用：设置该类作为bean对应的生命周期方法

- 范例：

  ```java
  @PostConstruct
  public void init() { System.out.println("init..."); }
  ```

# 2. Spring新注解

## 2.1 新注解概览

1. 非自定义的Bean的配置：<bean>
2. 加载properties文件的配置：<context:property-placeholder>
3. 组件扫描的配置：<context:component-scan>
4. 引入其他文件：<import>

![2](C:\Users\HASEE\Desktop\pics\2.png)

## 2.2 @Configuration和@ComponentScan

- 名称：@Configuration、@ComponentScan

- 类型：**类注解**

- 位置：类定义上方

- 作用：设置当前类为spring核心配置加载类

- 范例：

  ```java
  @Configuration
  @ComponentScan("scanPackageName")
  public class SpringConfigClassName{
  }
  ```

- 说明：

  - 核心配合类用于替换spring核心配置文件，此类可以设置空的，不设置变量与属性
  - bean扫描工作使用注解@ComponentScan替代

## 2.3 @Bean—加载第三方资源

- 名称：@Bean

- 类型：**方法注解**

- 位置：方法定义上方

- 作用：设置该方法的返回值作为spring管理的bean

- 范例：

  ```java
  @Bean("dataSource")
  public DruidDataSource createDataSource() {    return ……;    }
  ```

- 说明：

  - 因为第三方bean无法在其源码上进行修改，使用@Bean解决第三方bean的引入问题
  - 该注解用于替代XML配置中的静态工厂与实例工厂创建bean，不区分方法是否为静态或非静态
  - @Bean所在的类必须被spring扫描加载，否则该注解无法生效

- 相关属性

  - value（默认）：定义bean的访问id

## 2.4 @PropertySource—加载properties文件

- 名称：@PropertySource

- 类型：**类注解**

- 位置：类定义上方

- 作用：加载properties文件中的属性值

- 范例：

  ```java
  @PropertySource(value = "classpath:filename.properties")
  public class ClassName {
      @Value("${propertiesAttributeName}")
      private String attributeName;
  }
  ```

- 说明：

  - 不支持*通配格式，一旦加载，所有spring控制的bean中均可使用对应属性值

- 相关属性

  - value（默认）：设置加载的properties文件名
  - ignoreResourceNotFound：如果资源未找到，是否忽略，默认为false

## 2.5 @Import

- 名称：@Import

- 类型：**类注解**

- 位置：类定义上方

- 作用：导入第三方bean作为spring控制的资源

- 范例：

  ```java
  @Configuration
  @Import(OtherClassName.class)
  public class ClassName {
  }
  ```

- 说明：

  - @Import注解在同一个类上，仅允许添加一次，如果需要导入多个，使用数组的形式进行设定
  - 在被导入的类中可以继续使用@Import导入其他资源（了解）
  - @Bean所在的类可以使用导入的形式进入spring容器，无需声明为bean

## 2.6 加载核心配置类

**AnnotationConfigApplicationContext**

- 加载纯注解格式上下文对象，需要使用AnnotationConfigApplicationContext

  ```java
  AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
  ```

# 3. bean加载控制

## 3.1 @DependsOn

- 名称：@DependsOn

- 类型：类注解、方法注解

- 位置：bean定义的位置（类上或方法上）

- 作用：控制bean的加载顺序，使其在指定bean加载完毕后再加载

- 范例：

  ```java
  @DependsOn("beanId")
  public class ClassName {
  }
  ```

- 说明：

  - 配置在方法上，使@DependsOn指定的bean优先于@Bean配置的bean进行加载
  - 配置在类上，使@DependsOn指定的bean优先于当前类中所有@Bean配置的bean进行加载
  - 配置在类上，使@DependsOn指定的bean优先于@Component等配置的bean进行加载

- 相关属性

  - value（默认）：设置当前bean所依赖的bean的id

## 3.2 @Order

- 名称：@Order

- 类型：**配置类注解**

- 位置：配置类定义的位置（类上）

- 作用：控制配置类的加载顺序

- 范例：

  ```java
  @Order(1)
  public class SpringConfigClassName {
  }
  ```

## 3.3 @Lazy

- 名称：@Lazy

- 类型：**类注解、方法注解**

- 位置：bean定义的位置（类上或方法上）

- 作用：控制bean的加载时机，使其延迟加载

- 范例：

  ```java
  @Lazy
  public class ClassName {
  }
  ```

## 3.4 应用场景

@DependsOn

- 微信订阅号，发布消息和订阅消息的bean的加载顺序控制
- 双11活动期间，零点前是结算策略A，零点后是结算策略B，策略B操作的数据为促销数据。策略B加载顺序与促销数据的加载顺序

@Lazy

- 程序灾难出现后对应的应急预案处理是启动容器时加载时机

@Order

- 多个种类的配置出现后，优先加载系统级的，然后加载业务级的，避免细粒度的加载控制

# 4. 整合第三方技术

## 4.1 注解整合MyBatis说明

![3](C:\Users\HASEE\Desktop\pics\3.png)

## 4.2 注解整合MyBatis步骤

1. 修改mybatis外部配置文件(映射文件)格式为注解格式

2. 业务类使用@Component声明bean，使用@Autowired注入对象
3. 建立配置文件JDBCConfig与MyBatisConfig类，并将其导入到核心配置类SpringConfig
4. 开启注解扫描
5. 使用AnnotationConfigApplicationContext对象加载配置项

## 4.3 代码实现注解整合MyBatis

Dao层使用注解代替MyBatis映射配置文件

```java
public interface AccountDao {

    @Insert("insert into account(name,money)values(#{name},#{money})")
    void save(Account account);

    @Delete("delete from account where id = #{id} ")
    void delete(Integer id);

    @Update("update account set name = #{name} , money = #{money} where id = #{id} ")
    void update(Account account);

    @Select("select * from account")
    List<Account> findAll();

    @Select("select * from account where id = #{id} ")
    Account findById(Integer id);
}
```

Service层

```java
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    public void save(Account account) {
        accountDao.save(account);
    }

    public void update(Account account){
        accountDao.update(account);
    }

    public void delete(Integer id) {
        accountDao.delete(id);
    }

    public Account findById(Integer id) {
        return accountDao.findById(id);
    }

    public List<Account> findAll() {
        return accountDao.findAll();
    }
}
```

核心配置文件

```java
@Configuration
@ComponentScan("com.itheima")
@PropertySource("classpath:jdbc.properties")
@Import({JDBCConfig.class,MyBatisConfig.class})
public class SpringConfig {
}
```

MyBatis配置

```java
public class MyBatisConfig {

    @Bean
    public SqlSessionFactoryBean getSqlSessionFactoryBean(@Autowired DataSource dataSource){
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        ssfb.setTypeAliasesPackage("com.itheima.domain");
        ssfb.setDataSource(dataSource);
        return ssfb;
    }

    @Bean
    public MapperScannerConfigurer getMapperScannerConfigurer(){
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setBasePackage("com.itheima.dao");
        return msc;
    }

}
```

JDBC配置

```java
public class JDBCConfig {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    @Bean("dataSource")
    public DataSource getDataSource(){
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }
}
```

对比原先的配置xml文件方式

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <!--加载perperties配置文件的信息-->
    <context:property-placeholder location="classpath:*.properties"/>

    <!--加载druid资源-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <!--配置service作为spring的bean,注入dao-->
    <bean id="accountService" class="com.itheima.service.impl.AccountServiceImpl">
        <property name="accountDao" ref="accountDao"/>
    </bean>

    <!--spring整合mybatis后控制的创建连接用的对象-->
    <bean class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="typeAliasesPackage" value="com.itheima.domain"/>
    </bean>

    <!--加载mybatis映射配置的扫描，将其作为spring的bean进行管理-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.itheima.dao"/>
    </bean>

</beans>
```

## 4.4 注解整合Junit

1. Spring接管Junit的运行权，使用Spring专用的Junit类加载器

2. 为Junit测试用例设定对应的spring容器：

- 从Spring5.0以后，要求Junit的版本必须是4.12及以上
- Junit仅用于单元测试，不能将Junit的测试类配置成spring的bean，否则该配置将会被打包进入工程中 

导入Spring整合Junit坐标

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.1.9.RELEASE</version>
</dependency>
```

Spring整合Junit测试用例注解格式

```java
//设定spring专用的类加载器
@RunWith(SpringJUnit4ClassRunner.class)
//设定加载的spring上下文对应的配置
@ContextConfiguration(classes = SpringConfig.class)
public class UserServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testFindById(){
        Account ac = accountService.findById(2);
//        System.out.println(ac);
        Assert.assertEquals("Jock1",ac.getName());
    }

    @Test
    public void testFindAll(){
        List<Account> list = accountService.findAll();
        Assert.assertEquals(3,list.size());
    }

}
```

# 5. IoC底层核心原理

## 5.1 核心接口

有很多，图略

## 5.2 组件扫描器

开发过程中，需要根据需求加载必要的bean，排除指定bean

![4](C:\Users\HASEE\Desktop\pics\4.png)

## 5.3 设定组件扫描加载过滤器

- 名称：@ComponentScan

- 类型：**类注解**

- 位置：类定义上方

- 作用：设置spring配置加载类扫描规则

- 范例：

  ```java
  @ComponentScan(
      value="com.itheima",	           //设置基础扫描路径
      excludeFilters =                          //设置过滤规则，当前为排除过滤
  	@ComponentScan.Filter(            //设置过滤器
  	    type= FilterType.ANNOTATION,  //设置过滤方式为按照注解进行过滤
  	    classes=Repository.class)     //设置具体的过滤项，过滤所有@Repository修饰的bean
      )
  ```

​	includeFilters：设置包含性过滤器

​	excludeFilters：设置排除性过滤器

​	type：设置过滤器类型

## 5.4 自定义组件过滤器

- 名称：TypeFilter

- 类型：**接口**

- 作用：自定义类型过滤器

- 范例：

  <font color='red'>false表示不拦截</font>

  ```java
  public class MyTypeFilter implements TypeFilter {
      public boolean match(MetadataReader mr, MetadataReaderFactory mrf) throws IOException {
          ClassMetadata cm = metadataReader.getClassMetadata();
          tring className = cm.getClassName();
          if(className.equals("com.itheima.dao.impl.BookDaoImpl")){
              return false;
          }
          return false;
      }
  }
  ```

  ```java
  @ComponentScan(
          value = "com.itheima",
          excludeFilters = @ComponentScan.Filter(
                  type= FilterType.CUSTOM,
                  classes = MyTypeFilter.class
          )
  )
  ```

## 5.5 自定义导入器

- bean只有通过配置才可以进入spring容器，被spring加载并控制
- 配置bean的方式如下：
  - XML文件中使用<bean/>标签配置
  - 使用@Component及衍生注解配置
- 企业开发过程中，通常需要配置大量的bean，需要一种快速高效配置大量bean的方式

**ImportSelector**

- 名称： ImportSelector

- 类型：**接口**

- 作用：自定义bean导入器，<font color='red'>即不使用XML和@Component，也能导入Spring容器</font>

- 范例：

  ```java
  public class MyImportSelector implements ImportSelector {
      public String[] selectImports(AnnotationMetadata icm) {
  //      1.编程形式加载一个类
  //      return new String[]{"com.itheima.dao.impl.BookDaoImpl"};
  
  //      2.加载import.properties文件中的单个类名
  //      ResourceBundle bundle = ResourceBundle.getBundle("import");
  //      String className = bundle.getString("className");
  
  //      3.加载import.properties文件中的多个类名
          ResourceBundle bundle = ResourceBundle.getBundle("import");
          String className = bundle.getString("className");
          return className.split(",");
      }
  }
  ```

  ```java
  @Configuration
  @ComponentScan("com.itheima")
  @Import(MyImportSelector.class)
  public class SpringConfig {
  }
  ```

  import.properties

  ```xml
  path=com.itheima.dao.impl.*
  ```

  ```java
  public class CustomerImportSelector implements ImportSelector {
      ……
  }
  ```

## 5.6 自定义注册器

- 名称：ImportBeanDefinitionRegistrar

- 类型：**接口**

- 作用：自定义bean定义注册器

- 范例：

  ```java
  public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
      @Override
      public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
          //自定义注册器
          //1.开启类路径bean定义扫描器，需要参数bean定义注册器BeanDefinitionRegistry，需要制定是否使用默认类型过滤器
          ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry,false);
          //2.添加包含性加载类型过滤器（可选，也可以设置为排除性加载类型过滤器）
          scanner.addIncludeFilter(new TypeFilter() {
              @Override
              public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                  //所有匹配全部成功，此处应该添加实际的业务判定条件
                  return true;
              }
          });
          //设置扫描路径
          scanner.scan("com.itheima");
      }
  }
  ```

  ```java
  @Import(MyImportBeanDefinitionRegistrar.class)
  ```

  本例子中自定义注册器的作用相当于@ComponentScan("com.itheima")

  ```java
  public class CustomeImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
      ……
  }
  ```

## 5.7 bean初始化过程解析

![5](C:\Users\HASEE\Desktop\pics\5.png)

- BeanFactoryPostProcessor
  - 作用：定义了在bean工厂对象创建后，bean对象创建前执行的动作，用于对工厂进行创建后业务处理
  - 运行时机：当前操作用于对工厂进行处理，仅运行一次
- BeanPostProcessor
  - 作用：定义了所有bean初始化前后进行的统一动作，用于对bean进行创建前业务处理与创建后业务处理
  - 运行时机：当前操作伴随着每个bean的创建过程，每次创建bean均运行该操作
- InitializingBean
  - 作用：定义了每个bean的初始化前进行的动作，属于非统一性动作，用于对bean进行创建前业务处理
  - 运行时机：当前操作伴随着任意一个bean的创建过程，保障其个性化业务处理
- 注意：<font color='red'>上述操作均需要被spring容器加载放可运行(import)</font>

BeanFactoryPostProcessor示例代码

```java
public class MyBeanFactory implements BeanFactoryPostProcessor {
    @Override
    //工厂后处理bean接口核心操作
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("bean工厂制作好了，还有什么事情需要处理");
    }
}
```

BeanPostProcessor示例代码

```java
public class MyBean implements BeanPostProcessor {
    @Override
    //所有bean初始化前置操作
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("bean之前巴拉巴拉");
        System.out.println(beanName);
        return bean;
    }

    @Override
    //所有bean初始化后置操作
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("bean之后巴拉巴拉");
        return bean;
    }
}
```

InitializingBean示例代码

```java
public class EquipmentDaoImpl implements EquipmentDao,InitializingBean {

    public void save() {
        System.out.println("equipment dao running...");
    }

    @Override
    //定义当前bean初始化操作，功效等同于init-method属性配置
    public void afterPropertiesSet() throws Exception {
        SqlSessionFactoryBean fb;
        System.out.println("EquipmentDaoImpl......bean ...init......");
    }
}
```

配置

```java
//@Import({CustomeImportBeanDefinitionRegistrar.class,MyBeanFactory.class, MyBean.class})
```

![6](C:\Users\HASEE\Desktop\pics\6.png)

+ FactoryBean
  + 对单一的bean的初始化过程进行封装，达到简化配置的目的

**FactoryBean与BeanFactory区别**

- FactoryBean：封装单个bean的创建过程
- BeanFactory：Spring容器顶层接口，定义了bean相关的获取操作