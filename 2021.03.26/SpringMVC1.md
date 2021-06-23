# 1. SpringMVC简介

## 1.1 三层架构

- 表现层：负责数据展示
- 业务层：负责业务处理
- 数据层：负责数据操作

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.26/pics/1.png)

## 1.2 MVC模型

MVC（Model View Controller），一种用于设计创建Web应用程序表现层的模式

- Model（模型）：数据模型，用于封装数据
- View（视图）：页面视图，用于展示数据
  + jsp
  + html

+ Controller（控制器）：处理用户交互的调度器，用于根据用户需求处理程序逻辑
  - Servlet
  - SpringMVC

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.26/pics/2.png)

## 1.3 SpringMVC

+ SpringMVC是一种基于Java实现MVC模型的轻量级Web框架

优点

+ 使用简单
+ 性能突出（相比现有的框架技术）
+ 灵活性强

# 2. 简单案例

## 2.1 案例步骤

1. 导入坐标(pom.xml)

```xml
<!-- servlet3.1规范的坐标 -->
<dependency>
  <groupId>javax.servlet</groupId>
  <artifactId>javax.servlet-api</artifactId>
  <version>3.1.0</version>
  <scope>provided</scope>
</dependency>
<!--jsp坐标-->
<dependency>
  <groupId>javax.servlet.jsp</groupId>
  <artifactId>jsp-api</artifactId>
  <version>2.1</version>
  <scope>provided</scope>
</dependency>
<!--spring的坐标-->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-context</artifactId>
  <version>5.1.9.RELEASE</version>
</dependency>
<!--spring web的坐标-->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-web</artifactId>
  <version>5.1.9.RELEASE</version>
</dependency>
<!--springmvc的坐标-->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-webmvc</artifactId>
  <version>5.1.9.RELEASE</version>
</dependency>
```

2. 创建spring-mvc.xml配置文件，用@Controller定义表现层Bean

```java
//设置当前类为Spring的控制器类
@Controller
public class UserController {

    public void save(){
        System.out.println("user mvc controller is running ...");
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">
    <!--扫描加载所有的控制类类-->
    <context:component-scan base-package="com.itheima"/>

</beans>
```

3. 配置DispatcherServlet(web.xml)

```xml
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
```

4. 设置@Controller访问路径和返回页面

```java
//设置当前类为Spring的控制器类
@Controller
public class UserController {
    //设定当前方法的访问映射地址
    @RequestMapping("/save")
    //设置当前方法返回值类型为String，用于指定请求完成后跳转的页面
    public String save(){
        System.out.println("user mvc controller is running ...");
        //设定具体跳转的页面
        return "success.jsp";
    }
}
```

## 2.2 案例工作流程

- 服务器启动
  1. 加载web.xml中DispatcherServlet
  2. 读取spring-mvc.xml中的配置，加载所有com.itheima包中所有标记为bean的类
  3. 读取bean中方法上方标注@RequestMapping的内容
- 处理请求
  1. DispatcherServlet配置拦截所有请求 /
  2. 使用请求路径与所有加载的@RequestMapping的内容进行比对
  3. 执行对应的方法
  4. 根据方法的返回值在webapp目录中查找对应的页面并展示  

## 2.3 SpringMVC 技术架构图

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.26/pics/3.png)

核心组件：

- DispatcherServlet：前端控制器， 是整体流程控制的中心，由其调用其它组件处理用户的请求， 有
  效的降低了组件间的耦合性
- HandlerMapping：处理器映射器， 负责根据用户请求找到对应具体的Handler处理器
- Handler：处理器，业务处理的核心类，通常由开发者编写，描述具体的业务
- HandlAdapter：处理器适配器，通过它对处理器进行执行
- View Resolver：视图解析器， 将处理结果生成View视图
- View：视图，最终产出结果， 常用视图如jsp、 html  

重点关注：

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.26/pics/4.png)

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.26/pics/5.png)

1. 用户发送请求至前端控制器DispatcherServlet。

2. DispatcherServlet收到请求调用HandlerMapping处理器映射器。

3. 处理器映射器找到具体的处理器(可以根据xml配置、注解进行查找)，生成处理器对象及处理器拦截器(如果有则生成)一并返回给DispatcherServlet。

4. DispatcherServlet调用HandlerAdapter处理器适配器。

5. HandlerAdapter经过适配调用具体的处理器(Controller，也叫后端控制器)。

6. Controller执行完成返回ModelAndView。

7. HandlerAdapter将controller执行结果ModelAndView返回给DispatcherServlet。

8. DispatcherServlet将ModelAndView传给ViewReslover视图解析器。

9. ViewReslover解析后返回具体View。

10. DispatcherServlet根据View进行渲染视图（即将模型数据填充至视图中），DispatcherServlet响应用户。

## 2.4 SpringMVC的@RequestMapping注解

@RequestMapping

作用：用于建立请求 URL 和处理请求方法之间的对应关系

位置：

​      类上，请求URL 的第一级访问目录。此处不写的话，就相当于应用的根目录

​      方法上，请求 URL 的第二级访问目录，与类上的使用@ReqquestMapping标注的一级目录一起组成访问虚拟路径

属性：

​      value：用于指定请求的URL。它和path属性的作用是一样的

​      method：用于指定请求的方式

​      params：用于指定限制请求参数的条件。它支持简单的表达式。要求请求参数的key和value必须和配置的一模一样

例如：

​      params = {"accountName"}，表示请求参数必须有accountName

​      params = {"moeny!100"}，表示请求参数中money不能是100

# 3. 基本配置

## 3.1 Controller加载控制

SpringMVC的处理器对应的bean必须按照规范格式开发，未避免加入无效的bean可通过bean加载过滤器进
行包含设定或排除设定，表现层bean标注通常设定为@Controller

**xml方式**

```xml
<context:component-scan base-package="com.itheima">
    <context:include-filter
                            type="annotation"
                            expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
```

说明

+ <font color='red'>业务层与数据层的bean加载由Spring控制，参照spring的加载方式</font>
+ <font color='red'>表现层的bean加载由SpringMVC单独控制</font>
  + 表现层处理bean使用@Controller声明
  + bean加载控制使用包含性过滤器
  + 过滤器类型为通过的注解，过滤的注解名称为Controller

## 3.2 静态资源加载

DispatcherServlet的url-pattern默认是/

```xml
<!--放行指定类型静态资源配置方式-->
<mvc:resources mapping="/img/**" location="/img/"/>
<mvc:resources mapping="/js/**" location="/js/"/>
<mvc:resources mapping="/css/**" location="/css/"/>

<!--SpringMVC提供的通用资源放行方式-->
<mvc:default-servlet-handler/>
```

## 3.3 中文乱码问题

SpringMVC提供专用的中文字符过滤器，用于处理乱码问题

配置在 **web.xml** 里面

```XML
<!--乱码处理过滤器，与Servlet中使用的完全相同，差异之处在于处理器的类由Spring提供-->
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
```

## 3.4 注解驱动

spring-mvc.xml替换成注解形式

```java
@Configuration
@ComponentScan(value = "com.itheima",includeFilters =
    @ComponentScan.Filter(type=FilterType.ANNOTATION,classes = {Controller.class})
    )
public class SpringMVCConfiguration implements WebMvcConfigurer{
    //注解配置放行指定资源格式
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/img/**").addResourceLocations("/img/");
//        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
//        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
//    }

    //注解配置通用放行资源的格式
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();;
    }
}
```

web.xml替换成注解形式

```java
public class ServletContainersInitConfig extends AbstractDispatcherServletInitializer {
    //创建Servlet容器时，使用注解的方式加载SPRINGMVC配置类中的信息，并加载成WEB专用的ApplicationContext对象
    //该对象放入了ServletContext范围，后期在整个WEB容器中可以随时获取调用
    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(SpringMVCConfiguration.class);
        return ctx;
    }

    //注解配置映射地址方式，服务于SpringMVC的核心控制器DispatcherServlet
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }

    //乱码处理作为过滤器，在servlet容器启动时进行配置，相关内容参看Servlet零配置相关课程
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        CharacterEncodingFilter cef = new CharacterEncodingFilter();
        cef.setEncoding("UTF-8");
        FilterRegistration.Dynamic registration = servletContext.addFilter("characterEncodingFilter", cef);
        registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST,DispatcherType.FORWARD,DispatcherType.INCLUDE),false,"/*");
    }
}
```

# 4. SpringMVC的请求

获得请求参数的类型：

1. 基本类型参数

2. POJO类型参数

3. 数组类型参数

4. 集合类型参数

## 4.1 获得基本类型参数

Controller中的业务方法的参数名称要与请求参数的name一致，参数值会自动映射匹配。并且能自动做类型转换（自动的类型转换是指从String向其他类型的转换）

http://localhost/user/requestParam1?name=itheima&age=14

```java
//方法传递普通类型参数，数量任意，类型必须匹配
//http://localhost/user/requestParam1?name=itheima
//http://localhost/user/requestParam1?name=itheima&age=14
@RequestMapping("/requestParam1")
public String requestParam1(String name,int age){
    System.out.println(name+","+age);
    return "page.jsp";
}
```

## 4.2 @RequestParam注解

当请求的参数名称与Controller的业务方法参数名称不一致时，就需要通过@RequestParam注解进行显示的绑定

类型： 形参注解

位置：处理器类中的方法形参前方

作用：绑定请求参数与对应处理方法形参间的关系

http://localhost/user/requestParam2?userName=Jock

```java
//方法传递普通类型参数，使用@RequestParam参数匹配URL传参中的参数名称与方法形参名称
//http://localhost/user/requestParam2?userName=Jock
@RequestMapping("/requestParam2")
public String requestParam2(@RequestParam(value = "userName",required = true) String name){
    System.out.println(name);
    return "page.jsp";
}
```

## 4.3 获得POJO类型参数

POJO类

```java
public class User {
    private String name;
    private Integer age;

    private Address address;

    private List<String> nick;

    private List<Address> addresses;

    public List<Address> getAddresses() {
        return addresses;
    }

    public Map<String,Address> addressMap;

    public Map<String, Address> getAddressMap() {
        return addressMap;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", nick=" + nick +
                ", addresses=" + addresses +
                ", addressMap=" + addressMap +
                '}';
    }

    public void setAddressMap(Map<String, Address> addressMap) {
        this.addressMap = addressMap;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<String> getNick() {
        return nick;
    }

    public void setNick(List<String> nick) {
        this.nick = nick;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
```

```java
public class Address {
    private String province;
    private String city;
    private String address;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "provice='" + province + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

}
```

http://localhost/user/requestParam3?name=Jock&age=39

```java
//方法传递POJO类型参数，URL地址中的参数作为POJO的属性直接传入对象
//http://localhost/user/requestParam3?name=Jock&age=39
@RequestMapping("/requestParam3")
public String requestParam3(User user){
    System.out.println(user);
    return "page.jsp";
}
```

http://localhost/user/requestParam4?name=Jock&age=39

```java
//当方法参数中具有POJO类型参数与普通类型参数，URL地址传入的参数不仅给POJO对象属性赋值，也给方法的普通类型参数赋值
//http://localhost/user/requestParam4?name=Jock&age=39
@RequestMapping("/requestParam4")
public String requestParam4(User user,int age){
    System.out.println("user="+user+",age="+age);
    return "page.jsp";
}
```

http://localhost/user/requestParam5?address.city=beijing

```java
//使用对象属性名.属性名的对象层次结构可以为POJO中的POJO类型参数属性赋值
//http://localhost/user/requestParam5?address.city=beijing
@RequestMapping("/requestParam5")
public String requestParam5(User user){
    System.out.println(user.getAddress().getCity());
    return "page.jsp";
}
```

http://localhost/user/requestParam6?nick=Jock1&nick=Jockme&nick=zahc

```java
//通过URL地址中同名参数，可以为POJO中的集合属性进行赋值，集合属性要求保存简单数据
//http://localhost/user/requestParam6?nick=Jock1&nick=Jockme&nick=zahc
@RequestMapping("/requestParam6")
public String requestParam6(User user){
    System.out.println(user);
    return "page.jsp";
}
```

http://localhost/user/requestParam7?addresses[0].city=beijing&addresses[1].province=hebei

```java
//POJO中List对象保存POJO的对象属性赋值，使用[数字]的格式指定为集合中第几个对象的属性赋值
//http://localhost/user/requestParam7?addresses[0].city=beijing&addresses[1].province=hebei
@RequestMapping("/requestParam7")
public String requestParam7(User user){
    System.out.println(user.getAddresses());
    return "page.jsp";
}
```

http://localhost/user/requestParam8?addressMap['job'].city=beijing&addressMap['home'].province=henan

```java
//POJO中Map对象保存POJO的对象属性赋值，使用[key]的格式指定为Map中的对象属性赋值
//http://localhost/user/requestParam8?addressMap['job'].city=beijing&addressMap['home'].province=henan
@RequestMapping("/requestParam8")
public String requestParam8(User user){
    System.out.println(user.getAddressMap());
    return "page.jsp";
}
```

## 4.4 获得数组类型参数

Controller中的业务方法数组名称与请求参数的名称一致，且请求参数数量＞1个，参数值会自动映射匹配

http://localhost/user/requestParam9?nick=Jockme&nick=zahc

```java
//方法传递普通类型的数组参数，URL地址中使用同名变量为数组赋值
//http://localhost/user/requestParam9?nick=Jockme&nick=zahc
@RequestMapping("/requestParam9")
public String requestParam9(String[] nick){
    System.out.println(nick[0]+","+nick[1]);
    return "page.jsp";
}
```

## 4.5 获得集合类型参数

保存简单类型数据，请求参数名与处理器方法形参名保持一致，且请求参数数量＞1个
http://localhost/user/requestParam10?nick=Jockme&nick=zahc

```java
//方法传递保存普通类型的List集合时，无法直接为其赋值，需要使用@RequestParam参数对参数名称进行转换
//http://localhost/user/requestParam10?nick=Jockme&nick=zahc
@RequestMapping("/requestParam10")
public String requestParam10(@RequestParam("nick") List<String> nick){
    System.out.println(nick);
    return "page.jsp";
}
```

## 4.6 日期类型转换

方式1：@DateTimeFormat

http://localhost/user/requestParam11?date=1999-09-09

```java
//数据类型转换，使用自定义格式化器或@DateTimeFormat注解设定日期格式
//两种方式都依赖springmvc的注解启动才能运行
//http://localhost/user/requestParam11?date=1999-09-09
@RequestMapping("/requestParam11")
public String requestParam11(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
    System.out.println(date);
    return "page.jsp";
}
```

spring-mvc.xml文件中

```xml
<!--    自定义格式转换器案例，配合带@DateTimeFormat的requestParam11使用-->
<mvc:annotation-driven/>
```

方式2：自定义格式转换器

```java
@RequestMapping("/requestParam11")
public String requestParam11(Date date){
    System.out.println(date);
    return "page.jsp";
}
```

spring-mvc.xml文件中

```xml
<!--    自定义格式转换器案例，配合不带@DateTimeFormat的requestParam11使用-->
<!--开启注解驱动，加载自定义格式化转换器对应的类型转换服务-->
<mvc:annotation-driven conversion-service="conversionService"/>
<!--自定义格式化转换器-->
<bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
    <!--覆盖格式化转换器定义规则，该规则是一个set集合，对格式化转换器来说是追加和替换的思想，而不是覆盖整体格式化转换器-->
    <property name="formatters">
        <set>
            <!--具体的日期格式化转换器-->
            <bean class="org.springframework.format.datetime.DateFormatter">
                <!--具体的规则，不具有通用性，仅适用于当前的日期格式化转换器-->
                <property name="pattern" value="yyyy-MM-dd"/>
            </bean>
        </set>
    </property>
</bean>
```

## 4.7 自定义类型转换器

SpringMVC默认已经提供了一些常用的类型转换器，例如客户端提交的字符串转换成int型进行参数设置。但是不是所有的数据类型都提供了转换器，没有提供的就需要自定义转换器

1. 定义转换器类实现Converter接口

```java
//自定义类型转换器，实现Converter接口，接口中指定的泛型即为最终作用的条件
//本例中的泛型填写的是String，Date，最终出现字符串转日期时，该类型转换器生效
public class MyDateConverter implements Converter<String, Date> {
    //重写接口的抽象方法，参数由泛型决定
    public Date convert(String source) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        //类型转换器无法预计使用过程中出现的异常，因此必须在类型转换器内部捕获，不允许抛出，框架无法预计此类异常如何处理
        try {
            date = df.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
```

2. 在配置文件中声明转换器

```xml
<!--1.将自定义Converter注册为Bean，受SpringMVC管理-->
<bean id="myDateConverter" class="com.itheima.converter.MyDateConverter"/>
<!--2.设定自定义Converter服务bean-->
<bean id="conversionService"
      class="org.springframework.context.support.ConversionServiceFactoryBean">
    <!--3.注入所有的自定义Converter，该设定使用的是同类型覆盖的思想-->
    <property name="converters">
        <!--4.set保障同类型转换器仅保留一个，去重规则以Converter<S,T>的泛型为准-->
        <set>
            <!--5.具体的类型转换器-->
            <ref bean="myDateConverter"/>
        </set>
    </property>
</bean>
```

3. 在<annotation-driven>中引用转换器

```xml
<!--开启注解驱动，加载自定义格式化转换器对应的类型转换服务-->
<mvc:annotation-driven conversion-service="conversionService"/>
```

http://localhost/user/requestParam12?date=1999-09-09

```java
//数据类型转换，使用自定义类型转换器，需要配置后方可使用
//http://localhost/user/requestParam12?date=1999-09-09
@RequestMapping("/requestParam12")
public String requestParam12(Date date){
    System.out.println(date);
    return "page.jsp";
}
```

## 4.8 获取Restful风格的参数

Restful是一种软件架构风格、设计风格，而不是标准，只是提供了一组设计原则和约束条件。主要用于客户端和服务器交互类的软件，基于这个风格设计的软件可以更简洁，更有层次，更易于实现缓存机制等。

Restful风格的请求是使用“url+请求方式”表示一次请求目的的，HTTP 协议里面四个表示操作方式的动词如下：

GET：用于获取资源

POST：用于新建资源

PUT：用于更新资源

DELETE：用于删除资源  

例如：

/user/1    GET ：       得到 id = 1 的 user

/user/1   DELETE：  删除 id = 1 的 user

/user/1    PUT：       更新 id = 1 的 user

/user       POST：      新增 user

上述url地址/user/1中的1就是要获得的请求参数，在SpringMVC中可以使用占位符进行参数绑定。地址/user/1可以写成/user/{id}，占位符{id}对应的就是1的值。在业务方法中我们可以使用@PathVariable注解进行占位符的匹配获取工作。

http://localhost:8080/itheima_springmvc1/quick17/zhangsan

```java
@RequestMapping(value="/quick17/{name}")
public void save17(@PathVariable(value="name") String username) throws IOException {
        System.out.println(username);
 }
```

# 5. SpringMVC的响应

## 5.1 回写数据—返回字符串

通过SpringMVC框架注入的response对象，使用response.getWriter().print(“hello world”) 回写数据，此时不需要视图跳转，业务方法返回值为void

将需要回写的字符串直接返回，但此时需要通过@ResponseBody注解告知SpringMVC框架，方法返回的字符串不是跳转是直接在http响应体中返回

http://localhost/showData1

http://localhost/showData2

```java
//使用原生response对象响应数据
@RequestMapping("/showData1")
public void showData1(HttpServletResponse response) throws IOException {
    response.getWriter().write("message");
}

//使用@ResponseBody将返回的结果作为响应内容，而非响应的页面名称
@RequestMapping("/showData2")
@ResponseBody
public String showData2(){
    return "{'name':'Jock'}";
}
```

## 5.2 回写数据—返回json格式字符串

```java
public class Book {
    private String name;
    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
```

方法1：使用jackson进行json数据格式转化

手动拼接json格式字符串的方式很麻烦，开发中往往要将复杂的java对象转换成json格式的字符串，我们可以使用web阶段学习过的json转换工具jackson进行转换,通过jackson转换json格式字符串，回写字符串

http://localhost/showData3

```java
//使用jackson进行json数据格式转化
@RequestMapping("/showData3")
@ResponseBody
public String showData3() throws JsonProcessingException {
    Book book  = new Book();
    book.setName("SpringMVC入门案例");
    book.setPrice(66.66d);

    ObjectMapper om = new ObjectMapper();
    return om.writeValueAsString(book);
}
```

方法2：使用SpringMVC注解驱动

http://localhost/showData4

```java
//使用SpringMVC注解驱动，对标注@ResponseBody注解的控制器方法进行结果转换，由于返回值为引用类型，自动调用jackson提供的类型转换器进行格式转换
@RequestMapping("/showData4")
@ResponseBody
public Book showData4() {
    Book book  = new Book();
    book.setName("SpringMVC入门案例");
    book.setPrice(66.66d);
    return book;
}
```

spring-mvc.xml

```xml
<!--开启springmvc注解驱动，对@ResponseBody的注解进行格式增强，追加其类型转换的功能，具体实现由MappingJackson2HttpMessageConverter进行-->
<mvc:annotation-driven/>
```

在 SpringMVC 的各个组件中，处理器映射器、处理器适配器、视图解析器称为 SpringMVC 的三大组件。

使用<mvc:annotation-driven />自动加载 RequestMappingHandlerMapping（处理映射器）和RequestMappingHandlerAdapter（处理适配器），可用在Spring-xml.xml配置文件中使用<mvc:annotation-driven />替代注解处理器和适配器的配置。

同时使用<mvc:annotation-driven />默认底层就会集成jackson进行对象或集合的json格式字符串的转换

## 5.3 回写数据—返回对象或集合

使用SpringMVC提供的消息类型转换器将对象与集合数据自动转换为JSON数据，在spring-mvc.xml中进行如下配置：

http://localhost/showData5

```java
//转换集合类型数据
@RequestMapping("/showData5")
@ResponseBody
public List showData5() {
    Book book1  = new Book();
    book1.setName("SpringMVC入门案例");
    book1.setPrice(66.66d);

    Book book2  = new Book();
    book2.setName("SpringMVC入门案例");
    book2.setPrice(66.66d);

    ArrayList al = new ArrayList();
    al.add(book1);
    al.add(book2);
    return al;
}
```

## 5.4 页面跳转—返回字符串

http://localhost/showPage

```java
//测试服务器是否正常工作使用
@RequestMapping("/showPage")
public String showPage() {
    System.out.println("user mvc controller is running ...");
    return "page.jsp";
}
```

http://localhost/showPage1

```java
//forward:page.jsp转发访问，支持访问WEB-INF下的页面
@RequestMapping("/showPage1")
public String showPage1() {
    System.out.println("user mvc controller is running ...");
    return "forward:/WEB-INF/page/page.jsp";
}
```

http://localhost/showPage2

```java
//redirect:page.jsp重定向访问，不支持访问WEB-INF下的页面
@RequestMapping("/showPage2")
public String showPage2() {
    System.out.println("user mvc controller is running ...");
    return "redirect:/WEB-INF/page/page.jsp";
}
```

```java
@RequestMapping("/showPage2")
public String showPage2() {
    System.out.println("user mvc controller is running ...");
    return "redirect:page.jsp";
}
```

## 5.5 页面访问快捷设定—InternalResourceViewResolver

展示页面的保存位置通常固定，且结构相似，可以设定通用的访问路径，简化页面配置格式  

spring-mvc.xml

```xml
<!--设定页面加载的前缀后缀，仅适用于默认形式，不适用于手工标注转发或重定向的方式-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/page/"/>
        <property name="suffix" value=".jsp"/>
    </bean>
```

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.26/pics/6.png)

http://localhost/showPage3

```java
//页面简化配置格式，使用前缀+页面名称+后缀的形式进行，类似于字符串拼接
@RequestMapping("/showPage3")
public String showPage3() {
    System.out.println("user mvc controller is running ...");
    return "page";
}
```

http://localhost/showPage4

```java
//页面简化配置格式不支持书写forward和redirect
@RequestMapping("/showPage4")
public String showPage4() {
    System.out.println("user mvc controller is running ...");
    return "forward:page";
}
```

```java
@RequestMapping("/showPage4")
public String showPage4() {
    System.out.println("user mvc controller is running ...");
    return "redirect:page";
}
```

## 5.6 页面跳转并返回数据

http://localhost/showPageAndData1

```java
//使用原生request对象传递参数
@RequestMapping("/showPageAndData1")
public String showPageAndData1(HttpServletRequest request) {
    request.setAttribute("name","itheima");
    return "page";
}
```

http://localhost/showPageAndData2

```java
//使用Model形参传递参数
@RequestMapping("/showPageAndData2")
public String showPageAndData2(Model model) {
    //添加数据的方式，key对value
    model.addAttribute("name","Jock");
    Book book  = new Book();
    book.setName("SpringMVC入门案例");
    book.setPrice(66.66d);
    //添加数据的方式，key对value
    model.addAttribute("book",book);
    return "page";
}
```

http://localhost/showPageAndData3

```java
//使用ModelAndView形参传递参数，该对象还封装了页面信息
@RequestMapping("/showPageAndData3")
public ModelAndView showPageAndData3(ModelAndView modelAndView) {
    //ModelAndView mav = new ModelAndView();    替换形参中的参数
    Book book  = new Book();
    book.setName("SpringMVC入门案例");
    book.setPrice(66.66d);

    //添加数据的方式，key对value
    modelAndView.addObject("book",book);
    //添加数据的方式，key对value
    modelAndView.addObject("name","Jockme");
    //设置页面的方式，该方法最后一次执行的结果生效
    modelAndView.setViewName("page");
    //返回值设定成ModelAndView对象
    return modelAndView;
}
```

http://localhost/showPageAndData4

```java
//ModelAndView对象支持转发的手工设定，该设定不会启用前缀后缀的页面拼接格式
@RequestMapping("/showPageAndData4")
public ModelAndView showPageAndData4(ModelAndView modelAndView) {
    modelAndView.setViewName("forward:/WEB-INF/page/page.jsp");
    return modelAndView;
}
```

http://localhost/showPageAndData5

```java
//ModelAndView对象支持重定向的手工设定，该设定不会启用前缀后缀的页面拼接格式
@RequestMapping("/showPageAndData5")
public ModelAndView showPageAndData6(ModelAndView modelAndView) {
    modelAndView.setViewName("redirect:page.jsp");
    return modelAndView;
}
```

## 5.9 Servlet相关接口替换方案

获取HttpServletRequest，HttpServletResponse，HttpSession

SpringMVC提供访问原始Servlet接口API的功能，通过形参声明即可

```java
@RequestMapping("/servletApi")
public String servletApi(HttpServletRequest request,
                         HttpServletResponse response, HttpSession session){
    System.out.println(request);
    System.out.println(response);
    System.out.println(session);
    request.setAttribute("name","itheima");
    System.out.println(request.getAttribute("name"));
    return "page";
}
```

Head数据获取

名称： @RequestHeader
类型： 形参注解
位置：处理器类中的方法形参前方
作用：绑定请求头数据与对应处理方法形参间的关系

```java
@RequestMapping("/headApi")
public String headApi(@RequestHeader("Accept-Encoding") String headMsg){
    System.out.println(headMsg);
    return "page";
}
```

Cookie数据获取

名称： @CookieValue
类型： 形参注解
位置：处理器类中的方法形参前方
作用：绑定请求Cookie数据与对应处理方法形参间的关系

```java
@RequestMapping("/cookieApi")
public String cookieApi(@CookieValue("JSESSIONID") String jsessionid){
    System.out.println(jsessionid);
    return "page";
}
```

Session数据设置（了解）

名称： @SessionAttributes
类型： 类注解
位置：处理器类上方
作用：声明放入session范围的变量名称，适用于Model类型数据传参

```java
@Controller
@SessionAttributes(names = {"age","gender"})
public class UserController {
//测试用方法，为下面的试验服务，用于在session中放入数据
@RequestMapping("/setSessionData")
public String setSessionData(HttpSession session){
    session.setAttribute("name","itheima");
    return "page";
	}
}
```

```java
//配合@SessionAttributes(names = {"age","gender"})使用
//将数据放入session存储范围，通过Model对象实现数据set，通过@SessionAttributes注解实现范围设定
@RequestMapping("/setSessionData2")
public String setSessionDate2(Model model) {
    model.addAttribute("age",39);
    model.addAttribute("gender","男");
    return "page";
}
```

Session数据获取

名称： @SessionAttribute
类型： 形参注解
位置：处理器类中的方法形参前方
作用：绑定请求Session数据与对应处理方法形参间的关系
范例：

```java
//测试用方法，为下面的试验服务，用于在session中放入数据
@RequestMapping("/setSessionData")
public String setSessionData(HttpSession session){
    session.setAttribute("name","itheima");
    return "page";
}
```

注解式参数数据封装底层原理

数据的来源不同，对应的处理策略要进行区分
Head
Cookie
Session
SpringMVC使用策略模式进行处理分发
顶层接口： HandlerMethodArgumentResolver
实现类： ……  