# 1. 校验框架

## 1.1 校验框架简介

### 1.1.1 表单校验作用

数据可以随意输入，导致错误的结果。后端表单校验的重要性

表单校验保障了数据有效性、安全性

### 1.1.2 表单校验分类

- 校验位置：
  - 客户端校验
  - 服务端校验
- 校验内容与对应方式：
  - 格式校验
    - 客户端：使用Js技术，利用正则表达式校验
    - 服务端：使用校验框架 
  - 逻辑校验
    - 客户端：使用ajax发送要校验的数据，在服务端完成逻辑校验，返回校验结果
    - 服务端：接收到完整的请求后，在执行业务操作前，完成逻辑校验

### 1.1.3 表单校验规则

- 长度：例如用户名长度，评论字符数量
- 非法字符：例如用户名组成
- 数据格式：例如Email格式、 IP地址格式
- 边界值：例如转账金额上限，年龄上下限
- 重复性：例如用户名是否重复

### 1.1.4 表单校验框架

- JSR（Java Specification Requests）：Java 规范提案  

  303：提供bean属性相关校验规则  

- JSR规范列表

  - 企业应用技术
    Contexts and Dependency Injection for Java (Web Beans 1.0) (JSR 299)
    Dependency Injection for Java 1.0 (JSR 330)@postConstruct, @PreDestroy
    Bean Validation 1.0 (JSR 303)
    Enterprise JavaBeans 3.1 (includes Interceptors 1.1) (JSR 318)
    Java EE Connector Architecture 1.6 (JSR 322)
    Java Persistence 2.0 (JSR 317)
    Common Annotations for the Java Platform 1.1 (JSR 250)
    Java Message Service API 1.1 (JSR 914)
    Java Transaction API (JTA) 1.1 (JSR 907)
    JavaMail 1.4 (JSR 919)
  - Web应用技术
    Java Servlet 3.0 (JSR 315)
    JavaServer Faces 2.0 (JSR 314)
    JavaServer Pages 2.2/Expression Language 2.2 (JSR 245)
    Standard Tag Library for JavaServer Pages (JSTL) 1.2 (JSR 52)
    Debugging Support for Other Languages 1.0 (JSR 45)
    模块化 (JSR 294)
    Swing应用框架 (JSR 296)
    JavaBeans Activation Framework (JAF) 1.1 (JSR 925)
    Streaming API for XML (StAX) 1.0 (JSR 173)
  - 管理与安全技术
    Java Authentication Service Provider Interface for Containers (JSR 196)
    Java Authorization Contract for Containers 1.3 (JSR 115)
    Java EE Application Deployment 1.2 (JSR 88)
    J2EE Management 1.1 (JSR 77)
    Java SE中与Java EE有关的规范
    JCache API (JSR 107)
    Java Memory Model (JSR 133)
    Concurrency Utilitie (JSR 166)
    Java API for XML Processing (JAXP) 1.3 (JSR 206)
    Java Database Connectivity 4.0 (JSR 221)
    Java Management Extensions (JMX) 2.0 (JSR 255)
    Java Portlet API (JSR 286)

- Web Service技术
  Java Date与Time API (JSR 310)
  Java API for RESTful Web Services (JAX-RS) 1.1 (JSR 311)
  Implementing Enterprise Web Services 1.3 (JSR 109)
  Java API for XML-Based Web Services (JAX-WS) 2.2 (JSR 224)
  Java Architecture for XML Binding (JAXB) 2.2 (JSR 222)
  Web Services Metadata for the Java Platform (JSR 181)
  Java API for XML-Based RPC (JAX-RPC) 1.1 (JSR 101)
  Java APIs for XML Messaging 1.3 (JSR 67)
  Java API for XML Registries (JAXR) 1.0 (JSR 93)

- JCP（Java Community Process）：Java社区

- Hibernate框架中包含一套独立的校验框架hibernate-validator  

  导入坐标

  ```xml
  <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-validator</artifactId>
      <version>6.1.0.Final</version>
  </dependency>
  ```

  **注意：**
  tomcat7 ：搭配hibernate-validator版本5.*.*.Final
  tomcat8.5↑ ：搭配hibernate-validator版本6.*.*.Final

## 1.2 快速入门

**1. 开启校验**

名称：@Valid 、 @Validated
类型：形参注解
位置：处理器类Controller中的实体类类型的方法形参前方
作用：设定对当前实体类类型参数进行校验
范例：  

```java
@RequestMapping(value = "/addemployee")
public String addEmployee(@Valid Employee employee) {
    System.out.println(employee);
}
```

**2.设置校验规则**

名称：@NotNull
类型：属性注解 等
位置：实体类属性上方
作用：设定当前属性校验规则
范例：
    每个校验规则所携带的参数不同，根据校验规则进行相应的调整
    具体的校验规则查看对应的校验框架进行获取

```java
public class Employee{
    @NotNull(message = "姓名不能为空")
    private String name;//员工姓名
}  
```

**3.获取错误信息**

通过形参Errors获取校验结果数据，通过Model接口将数据封装后传递到页面显示

```java
@RequestMapping(value = "/addemployee")
public String addEmployee(@Valid Employee employee, Errors errors, Model model){
    System.out.println(employee);
    if(errors.hasErrors()){
        for(FieldError error : errors.getFieldErrors()){
            model.addAttribute(error.getField(),error.getDefaultMessage());
        }
        return "addemployee.jsp";
    }
    return "success.jsp";
}  
```

jsp页面获取后台封装的校验结果信息

```html
<form action="/addemployee" method="post">
    员工姓名：<input type="text" name="name"><span style="color:red">${name}</span><br/>
    员工年龄：<input type="text" name="age"><span style="color:red">${age}</span><br/>
    <input type="submit" value="提交">
</form>
```

## 1.3 多规则校验

- 同一个属性可以添加多个校验器

```java
@NotNull(message = "请输入您的年龄")
@Max(value = 60,message = "年龄最大值不允许超过60岁")
@Min(value = 18,message = "年龄最小值不允许低于18岁")
private Integer age;//员工年龄
```

- 3种判定空校验器的区别

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.01/pics/1.png)

## 1.4 嵌套校验

名称：@Valid
类型：属性注解
位置：实体类中的引用类型属性上方
作用：设定当前应用类型属性中的属性开启校验
范例：

```java
public class Employee {
    //实体类中的引用类型通过标注@Valid注解，设定开启当前引用类型字段中的属性参与校验
    @Valid
    private Address address;
}
```

注意：开启嵌套校验后，被校验对象内部需要添加对应的校验规则

## 1.5 分组校验

- 同一个模块，根据执行的业务不同，需要校验的属性会有不同
  - 新增用户
  - 修改用户
- 对不同种类的属性进行分组，在校验时可以指定参与校验的字段所属的组类别
  - 定义组（通用）
  - 为属性设置所属组，可以设置多个
  - 开启组校验

用@Validated注解

```java
public interface GroupOne {
}
```

```java
public String addEmployee(@Validated({GroupOne.class}) Employee employee){
}  
```

```java
@NotEmpty(message = "姓名不能为空",groups = {GroupOne.class})
private String name;//员工姓名
```

# 2. SSM整合

## 2.1 整合流程简介

最重要的5个步骤

1. Spring
2. MyBatis
3. Spring整合MyBatis
4. SpringMVC
5. Spring整合SpringMVC

细化：

SSM（Spring+SpringMVC+MyBatis）

- Spring
  - 框架基础
- MyBatis
  - mysql+druid+pagehelper
- Spring整合MyBatis
- junit测试业务层接口
- SpringMVC
  - rest风格（postman测试请求结果）
  - 数据封装json（jackson）
- Spring整合SpringMVC
  - Controller调用Service
- 其他
  - 表现层数据封装
  - 自定义异常

案例的表结构：

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.01/pics/2.png)

## 2.2 项目结构搭建

- 创建项目，组织项目结构，创建包

- 创建表与实体类

- 创建三层架构对应的模块、接口与实体类，建立关联关系

  + 数据层接口（代理自动创建实现类）

  - 业务层接口+业务层实现类
  - 表现层类

搭建完的结构如下：

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.01/pics/3.png)

## 2.3 Spring整合MyBatis

+ 创建Spring配置文件
  + 组件扫描
+ 创建MyBatis映射文件
+ 整合MyBatis到Spring环境中
  + SqlSessionFactoryBean
  + 数据源(druid+jdbc.properties)
  + 映射扫描
  + 注解事务
  + 分页插件
+ 创建单元测试

1. 创建Spring配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"  xmlns:context="http://www.springframework.org/schema/context"  xmlns:tx="http://www.springframework.org/schema/tx"  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  xsi:schemaLocation="http://www.springframework.org/schema/beans  http://www.springframework.org/schema/beans/spring-beans.xsd  http://www.springframework.org/schema/context  http://www.springframework.org/schema/context/spring-context.xsd  http://www.springframework.org/schema/tx  http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!--开启bean注解扫描-->
    <context:component-scan base-package="com.itheima"/>

</beans>
```

2. 创建MyBatis映射文件

先在resources下建立结构目录，例如com、itheima、dao，然后合并

MyBatis映射文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.itheima.dao.UserDao">

    <!--添加-->
    <insert id="save" parameterType="user">
        insert into user(userName,password,realName,gender,birthday)values(#{userName},#{password},#{realName},#{gender},#{birthday})
    </insert>

    <!--删除-->
    <delete id="delete" parameterType="int">
        delete from user where uuid = #{uuid}
    </delete>

    <!--修改-->
    <update id="update" parameterType="user">
        update user set userName=#{userName},password=#{password},realName=#{realName},gender=#{gender},birthday=#{birthday} where uuid=#{uuid}
    </update>

    <!--查询单个-->
    <select id="get" resultType="user" parameterType="int">
        select * from user where uuid = #{uuid}
    </select>

    <!--分页查询-->
    <select id="getAll" resultType="user">
        select * from user
    </select>

    <!--登录-->
    <select id="getByUserNameAndPassword" resultType="user" >
        select * from user where userName=#{userName} and password=#{password}
    </select>

</mapper>
```

接口UserDao.java

```java
//注意：数据层操作不要和业务层操作的名称混淆，通常数据层仅反映与数据库间的信息交换，不体现业务逻辑
public User getByUserNameAndPassword(@Param("userName") String userName,@Param("password") String password);
```

注意：

在不使用@Param注解的时候，函数的参数只能为一个，并且在查询语句取值时只能用#{}。如果想传递多个参数，parameterType参数类型为map（此处为别名）或者为JavaBean。

而使用@Param注解则可以使用多个参数，无需再设置parameterType，并且在查询语句中使用时可以使用#{}或者${}

修改业务层UserServiceImpl.java

```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean save(User user) {
        return userDao.save(user);
    }

    @Override
    public boolean update(User user) {
        return userDao.update(user);
    }

    @Override
    public boolean delete(Integer uuid) {
        return userDao.delete(uuid);
    }

    @Override
    public User get(Integer uuid) {
        return userDao.get(uuid);
    }

    @Override
    public PageInfo<User> getAll(int page,int size) {
        PageHelper.startPage(page,size);
        List<User> all = userDao.getAll();
        return new PageInfo<User>(all);
    }

    @Override
    public User login(String userName, String password) {
        return userDao.getByUserNameAndPassword(userName,password);
    }
}
```

applicationContext.xml中整合MyBatis，并添加分页插件和事务

```xml
<!--开启注解式事务-->
<tx:annotation-driven transaction-manager="txManager"/>

<!--加载properties文件-->
<context:property-placeholder location="classpath*:jdbc.properties"/>

<!--数据源-->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="driverClassName" value="${jdbc.driver}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>

<!--整合mybatis到spring中-->
<bean class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="typeAliasesPackage" value="com.itheima.domain"/>
    <!--分页插件-->
    <property name="plugins">
        <array>
            <bean class="com.github.pagehelper.PageInterceptor">
                <property name="properties">
                    <props>
                        <prop key="helperDialect">mysql</prop>
                        <prop key="reasonable">true</prop>
                    </props>
                </property>
            </bean>
        </array>
    </property>
</bean>

<!--映射扫描-->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="com.itheima.dao"/>
</bean>

<!--事务管理器-->
<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
```

接口UserService.java添加事务注解

```java
@Transactional(readOnly = true)
public interface UserService {
    /**
     * 添加用户
     * @param user
     * @return
     */
    @Transactional(readOnly = false)
    public boolean save(User user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    @Transactional(readOnly = false)
    public boolean update(User user);

    /**
     * 删除用户
     * @param uuid
     * @return
     */
    @Transactional(readOnly = false)
    public boolean delete(Integer uuid);

    /**
     * 查询单个用户信息
     * @param uuid
     * @return
     */
    public User get(Integer uuid);

    /**
     * 查询全部用户信息
     * @return
     */
    public PageInfo<User> getAll(int page, int size);

    /**
     * 根据用户名密码进行登录
     * @param userName
     * @param password
     * @return
     */
    public User login(String userName,String password);
}
```

## 2.4 整合junit

单元测试整合junit

```java
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = "classpath:applicationContext.xml")  
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testDelete(){  
        User user = new User();  userService.delete(3);
    }
}
```

## 2.5 SpringMVC准备工作

+ web.xml加载SpringMVC
+ rest风格
+ 数据封装为json数据

web.xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

  <filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>UTF-8</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>

  <servlet>
    <servlet-name>DispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath*:spring-mvc.xml</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>DispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>

</web-app>
```

spring-mvc.xml配置

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <mvc:annotation-driven/>

    <context:component-scan base-package="com.itheima.controller"/>

</beans>
```

Controller层(Restful风格)

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping
    public boolean save(User user){
        System.out.println("save ..." + user);
        return true;
    }

    @PutMapping
    public boolean update(User user){
        System.out.println("update ..." + user);
        return true;
    }

    @DeleteMapping("/{uuid}")
    public boolean delete(@PathVariable Integer uuid){
        System.out.println("delete ..." + uuid);
        return true;
    }

    @GetMapping("/{uuid}")
    public User get(@PathVariable Integer uuid){
        System.out.println("get ..." + uuid);
        return null;
    }

    @GetMapping("/{page}/{size}")
    public List getAll(@PathVariable Integer page, @PathVariable Integer size){
        System.out.println("getAll ..." + page+","+size);
        return null;
    }

    @PostMapping("/login")
    public User login(String userName,String password){
        System.out.println("login ..." + userName + " ," +password);
        return null;
    }

}
```

## 2.6 Spring整合SpringMVC

web.xml加载Spring环境

```xml
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>classpath*:applicationContext.xml</param-value>
</context-param>

<!--启动服务器时，通过监听器加载spring运行环境-->
<listener>
  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

Controller调用Service

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public boolean save(User user){
        return userService.save(user);
    }

    @PutMapping
    public boolean update(User user){
        return userService.update(user);
    }

    @DeleteMapping("/{uuid}")
    public boolean delete(@PathVariable Integer uuid){
        return userService.delete(uuid);
    }

    @GetMapping("/{uuid}")
    public User get(@PathVariable Integer uuid){
        return userService.get(uuid);
    }

    @GetMapping("/{page}/{size}")
    public PageInfo<User> getAll(@PathVariable Integer page, @PathVariable Integer size){
        return userService.getAll(page,size);
    }

    @PostMapping("/login")
    public User login(String userName,String password){
        return userService.login(userName,password);
    }

}
```

## 2.7 表现层的数据封装

+ 前端接收到表现层返回的数据类型
  + 操作单个是否成功  true/false
  + 单个数据                  1，100，true
  + 对象数据                   json对象
  + 集合数据                   json数组

统一返回数据的格式

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.01/pics/4.png)

```java
public class Result {
    //    操作结果编码
    private Integer code;
    //    操作数据结果
    private Object data;
    //    消息
    private String message;

    public Result(Integer code) {
        this.code = code;
    }

    public Result(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```

```java
public class Code {
//    操作结果编码
    public static final Integer SAVE_OK = 20011;
    public static final Integer UPDATE_OK = 20021;
    public static final Integer DELETE_OK = 20031;
    public static final Integer GET_OK = 20041;

    public static final Integer SAVE_ERROR = 20010;
    public static final Integer UPDATE_ERROR = 20020;
    public static final Integer DELETE_ERROR = 20030;
    public static final Integer GET_ERROR = 20040;

//    系统错误编码

//    操作权限编码

//    校验结果编码

}
```

改变Controller

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Result save(User user){
        boolean flag = userService.save(user);
        return new Result(flag ? Code.SAVE_OK:Code.SAVE_ERROR);
    }

    @PutMapping
    public Result update(User user){
        boolean flag = userService.update(user);
        return new Result(flag ? Code.UPDATE_OK:Code.UPDATE_ERROR);
    }

    @DeleteMapping("/{uuid}")
    public Result delete(@PathVariable Integer uuid){
        boolean flag = userService.delete(uuid);
        return new Result(flag ? Code.DELETE_OK:Code.DELETE_ERROR);
    }

    @GetMapping("/{uuid}")
    public Result get(@PathVariable Integer uuid){
        User user = userService.get(uuid);
        return new Result(null != user ?Code.GET_OK: Code.GET_ERROR,user);
    }

    @GetMapping("/{page}/{size}")
    public Result getAll(@PathVariable Integer page, @PathVariable Integer size){
        PageInfo<User> all = userService.getAll(page, size);
        return new Result(null != all ?Code.GET_OK: Code.GET_ERROR,all);
    }


    @PostMapping("/login")
    public Result login(String userName,String password){
        User user = userService.login(userName,password);
        return new Result(null != user ?Code.GET_OK: Code.GET_ERROR,user);
    }

}
```

## 2.8 自定义异常

- 设定自定义异常，封装程序执行过程中出现的问题，便于表现层进行统一的异常拦截并进行处理
  - BusinessException
  - SystemException
- 自定义异常消息返回时需要与业务正常执行的消息按照统一的格式进行处理

定义异常类BusinessException

```java
public class BusinessException extends RuntimeException {
    //自定义异常中封装对应的错误编码，用于异常处理时获取对应的操作编码
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BusinessException(Integer code) {
        this.code = code;
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause, Integer code) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(Throwable cause, Integer code) {
        super(cause);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
```

Controller

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    ……

    @GetMapping("/{uuid}")
    public Result get(@PathVariable Integer uuid){
        User user = userService.get(uuid);
        //模拟出现异常，使用条件控制，便于测试结果
        if (uuid == 10 ) throw new BusinessException("查询出错啦，请重试！",Code.GET_ERROR);
        return new Result(null != user ?Code.GET_OK: Code.GET_ERROR,user);
    }

    ……

}
```

异常处理类(注解形式)

```java
@Component
@ControllerAdvice
public class ProjectExceptionAdivce {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    //对出现异常的情况进行拦截，并将其处理成统一的页面数据结果格式
    public Result doBusinessException(BusinessException e){
        return new Result(e.getCode(),e.getMessage());
    }

}
```

# 3. 纯注解整合SSM

web.xml

applicationContext.xml

spring-mvc.xml

UserDao.xml

jdbc.properties

## 3.1 替换UserDao.xml

UserDao.java

```java
public interface UserDao {
    /**
     * 添加用户
     * @param user
     * @return
     */
    @Insert("insert into user(userName,password,realName,gender,birthday)values(#{userName},#{password},#{realName},#{gender},#{birthday})")
    public boolean save(User user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    @Update("update user set userName=#{userName},password=#{password},realName=#{realName},gender=#{gender},birthday=#{birthday} where uuid=#{uuid}")
    public boolean update(User user);

    /**
     * 删除用户
     * @param uuid
     * @return
     */
    @Delete("delete from user where uuid = #{uuid}")
    public boolean delete(Integer uuid);

    /**
     * 查询单个用户信息
     * @param uuid
     * @return
     */
    @Select("select * from user where uuid = #{uuid}")
    public User get(Integer uuid);

    /**
     * 查询全部用户信息
     * @return
     */
    @Select("select * from user")
    public List<User> getAll();


    /**
     * 根据用户名密码查询个人信息
     * @param userName 用户名
     * @param password 密码信息
     * @return
     */
    @Select("select * from user where userName=#{userName} and password=#{password}")
    //注意：数据层操作不要和业务层操作的名称混淆，通常数据层仅反映与数据库间的信息交换，不体现业务逻辑
    public User getByUserNameAndPassword(@Param("userName") String userName,@Param("password") String password);
}
```

## 3.2 替换applicationContext.xml

创建config下的SpringConfig配置类，现将applicationContext.xml中的关于JDBC和MyBatis的内容各自放入新创建的JdbcConfig和MyBatisConfig配置类中

JdbcConfig类

```java
public class JdbcConfig {
    //使用注入的形式，读取properties文件中的属性值，等同于<property name="*******" value="${jdbc.driver}"/>
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    //定义dataSource的bean，等同于<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
    @Bean("dataSource")
    public DataSource getDataSource(){
        //创建对象
        DruidDataSource ds = new DruidDataSource();
        //手工调用set方法，等同于set属性注入<property name="driverClassName" value="******"/>
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }
}
```

MyBatisConfig类

```java
public class MyBatisConfig {
    //定义MyBatis的核心连接工厂bean，等同于<bean class="org.mybatis.spring.SqlSessionFactoryBean">
    @Bean
    //参数使用自动装配的形式加载dataSource，为set注入提供数据，dataSource来源于JdbcConfig中的配置
    public SqlSessionFactoryBean getSqlSessionFactoryBean(@Autowired DataSource dataSource,@Autowired Interceptor interceptor){
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        //等同于<property name="typeAliasesPackage" value="com.itheima.domain"/>
        ssfb.setTypeAliasesPackage("com.itheima.domain");
        //等同于<property name="dataSource" ref="dataSource"/>
        ssfb.setDataSource(dataSource);
//        //等同于<bean class="com.github.pagehelper.PageInterceptor">
//        Interceptor interceptor = new PageInterceptor();
//        Properties properties = new Properties();
//        properties.setProperty("helperDialect","mysql");
//        properties.setProperty("reasonable","true");
//        //等同于<property name="properties">
//        interceptor.setProperties(properties);
        ssfb.setPlugins(interceptor);
        return ssfb;
    }

    //定义MyBatis的映射扫描，等同于<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    @Bean
    public MapperScannerConfigurer getMapperScannerConfigurer(){
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        //等同于<property name="basePackage" value="com.itheima.dao"/>
        msc.setBasePackage("com.itheima.dao");
        return msc;
    }

    @Bean("pageInterceptor")
    public Interceptor getPageInterceptor(){
        Interceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect","mysql");
        properties.setProperty("reasonable","true");
        //等同于<property name="properties">
        interceptor.setProperties(properties);
        return interceptor;
    }

}
```

SpringConfig类

```java
@Configuration
//等同于<context:component-scan base-package="com.itheima">
@ComponentScan(value = "com.itheima",excludeFilters =
    //等同于<context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    @ComponentScan.Filter(type= FilterType.ANNOTATION,classes = {Controller.class}))
//等同于<context:property-placeholder location="classpath*:jdbc.properties"/>
@PropertySource("classpath:jdbc.properties")
//等同于<tx:annotation-driven />，bean的名称默认取transactionManager
@EnableTransactionManagement
@Import({MyBatisConfig.class,JdbcConfig.class})
public class SpringConfig {
    //等同于<bean id="txManager"/>
    @Bean("transactionManager")
    //等同于<bean class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    public DataSourceTransactionManager getTxManager(@Autowired DataSource dataSource){
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        //等同于<property name="dataSource" ref="dataSource"/>
        tm.setDataSource(dataSource);
        return tm;
    }
}
```

## 3.3 替换spring-mvc.xml  

SpringMvcConfig类

```java
@Configuration
//等同于<context:component-scan base-package="com.itheima.controller"/>
@ComponentScan("com.itheima.controller")
//等同于<mvc:annotation-driven/>，还不完全相同
@EnableWebMvc
public class SpringMvcConfig {
}
```

- @EnableWebMvc  

1. 支持ConversionService的配置，可以方便配置自定义类型转换器
2. 支持@NumberFormat注解格式化数字类型
3. 支持@DateTimeFormat注解格式化日期数据，日期包括Date,Calendar,JodaTime（JodaTime要导包）
4. 支持@Valid的参数校验(需要导入JSR-303规范)
5. 配合第三方jar包和SpringMVC提供的注解读写XML和JSON格式数据  

## 3.4 替换web.xml

ServletContainersInitConfig

```java
public class ServletContainersInitConfig extends AbstractDispatcherServletInitializer {

    //创建Servlet容器时，使用注解的方式加载SPRINGMVC配置类中的信息，并加载成WEB专用的ApplicationContext对象
    //该对象放入了ServletContext范围，后期在整个WEB容器中可以随时获取调用
    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(SpringMvcConfig.class);
        return ctx;
    }

    //注解配置映射地址方式，服务于SpringMVC的核心控制器DispatcherServlet
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    //基本等同于<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(SpringConfig.class);
        return ctx;
    }

    //乱码处理作为过滤器，在servlet容器启动时进行配置，相关内容参看Servlet零配置相关课程
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //触发父类的onStartup
        super.onStartup(servletContext);
        //1.创建字符集过滤器对象
        CharacterEncodingFilter cef = new CharacterEncodingFilter();
        //2.设置使用的字符集
        cef.setEncoding("UTF-8");
        //3.添加到容器（它不是ioc容器，而是ServletContainer）
        FilterRegistration.Dynamic registration = servletContext.addFilter("characterEncodingFilter", cef);
        //4.添加映射
        registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/*");
    }
}
```

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.04.01/pics/5.png)