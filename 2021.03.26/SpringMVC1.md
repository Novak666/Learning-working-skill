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

+ 业务层与数据层的bean加载由Spring控制，参照spring的加载方式
+ 表现层的bean加载由SpringMVC单独控制
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

# 4. SpringMVC的响应

1. 页面跳转

   直接返回字符串

   通过ModelAndView对象返回

2. 回写数据

   直接返回字符串

   返回对象或集合

## 4.1 页面跳转—返回字符串

- 转发（默认）

```java
@RequestMapping("/showPage1")
public String showPage1() {
    System.out.println("user mvc controller is running ...");
    return "forward:page.jsp";
}
```

- 重定向

```java
@RequestMapping("/showPage2")
public String showPage2() {
System.out.println("user mvc controller is running ...");
return "redirect:page.jsp";
}
```

## 4.2 页面访问快捷设定—InternalResourceViewResolver

展示页面的保存位置通常固定，且结构相似，可以设定通用的访问路径，简化页面配置格式  

```xml
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/pages/"/>
    <property name="suffix" value=".jsp"/>
/bean>
```

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.03.26/pics/6.png)

## 4.3 页面跳转—返回ModelAndView

方式1：在Controller中方法返回ModelAndView对象，并且设置视图名称

```java
@RequestMapping(value="/quick2")
    public ModelAndView save2(){
        /*
            Model:模型 作用封装数据
            View：视图 作用展示数据
         */
        ModelAndView modelAndView = new ModelAndView();
        //设置模型数据
        modelAndView.addObject("username","itcast");
        //设置视图名称
        modelAndView.setViewName("success");

        return modelAndView;
    }
```

方式2：在Controller中方法形参上直接声明ModelAndView，无需在方法中自己创建（框架自动注入的），在方法中直接使用该对象设置视图，同样可以跳转页面

```java
 @RequestMapping(value="/quick3")
    public ModelAndView save3(ModelAndView modelAndView){
        modelAndView.addObject("username","itheima");
        modelAndView.setViewName("success");
        return modelAndView;
    }
@RequestMapping(value="/quick4")
    public String save4(Model model){
        model.addAttribute("username","博学谷");
        return "success";
    }
```

方式3：在Controller方法的形参上可以直接使用原生的HttpServeltRequest对象，只需声明即可（不常用）

```java
@RequestMapping(value="/quick5")
    public String save5(HttpServletRequest request){
        request.setAttribute("username","酷丁鱼");
        return "success";
    }
```

## 4.4 回写数据—返回字符串

通过SpringMVC框架注入的response对象，使用response.getWriter().print(“hello world”) 回写数据，此时不需要视图跳转，业务方法返回值为void

将需要回写的字符串直接返回，但此时需要通过@ResponseBody注解告知SpringMVC框架，方法返回的字符串不是跳转是直接在http响应体中返回

```java
@RequestMapping(value="/quick7")
    @ResponseBody  //告知SpringMVC框架 不进行视图跳转 直接进行数据响应
    public String save7() throws IOException {
        return "hello itheima";
    }

    @RequestMapping(value="/quick6")
    public void save6(HttpServletResponse response) throws IOException {
        response.getWriter().print("hello itcast");
    }
```

## 4.5 回写数据—返回json格式字符串

```java
@RequestMapping(value="/quick8")
    @ResponseBody
    public String save8() throws IOException {
        return "{\"username\":\"zhangsan\",\"age\":18}";
    }
```

手动拼接json格式字符串的方式很麻烦，开发中往往要将复杂的java对象转换成json格式的字符串，我们可以使用web阶段学习过的json转换工具jackson进行转换,通过jackson转换json格式字符串，回写字符串

```java
@RequestMapping(value="/quick9")
    @ResponseBody
    public String save9() throws IOException {
        User user = new User();
        user.setUsername("lisi");
        user.setAge(30);
        //使用json的转换工具将对象转换成json格式字符串在返回
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        return json;
    }
```

## 4.6 回写数据—返回对象或集合

使用SpringMVC提供的消息类型转换器将对象与集合数据自动转换为JSON数据，在spring-mvc.xml中进行如下配置：

```xml
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
    <property name="messageConverters">
        <list>
            <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"/>
        </list>
    </property>
</bean
```

```java
@RequestMapping(value="/quick10")
    @ResponseBody
    //期望SpringMVC自动将User转换成json格式的字符串
    public User save10() throws IOException {
        User user = new User();
        user.setUsername("lisi2");
        user.setAge(32);
        return user;
}
```

使用SpringMVC注解驱动简化配置

```xml
<!--开启springmvc注解驱动，对@ResponseBody的注解进行格式增强，追加其类型转换的功能，具体实现由MappingJackson2HttpMessageConverter进行-->
<mvc:annotation-driven/>
```

在 SpringMVC 的各个组件中，处理器映射器、处理器适配器、视图解析器称为 SpringMVC 的三大组件。

使用`<mvc:annotation-driven />`自动加载 RequestMappingHandlerMapping（处理映射器）和

RequestMappingHandlerAdapter（处理适配器），可用在Spring-xml.xml配置文件中使用

`<mvc:annotation-driven />`替代注解处理器和适配器的配置。

同时使用`<mvc:annotation-driven />`

默认底层就会集成jackson进行对象或集合的json格式字符串的转换

# 5. SpringMVC的请求

获得请求参数的类型：

1. 基本类型参数

2. POJO类型参数

3. 数组类型参数

4. 集合类型参数

## 5.1 获得基本类型参数

Controller中的业务方法的参数名称要与请求参数的name一致，参数值会自动映射匹配。并且能自动做类型转换（自动的类型转换是指从String向其他类型的转换）

http://localhost:8080/itheima_springmvc1/quick11?username=zhangsan&age=12

```java
@RequestMapping(value="/quick11")
public void save11(String username,int age) throws IOException {
     System.out.println(username);
     System.out.println(age);
}
```

## 5.2 获得POJO类型参数

Controller中的业务方法的POJO参数的属性名与请求参数的名称一致，参数值会自动映射匹配

http://localhost/user/requestParam3?name=itheima&age=14

Controller

```java
@RequestMapping("/requestParam3")
public String requestParam3(User user){
    System.out.println("name="+user.getName());
    return "page.jsp";
}
```

POJO类

```java
public class User {
    private String name;
    private Integer age;
    
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

## 5.3 获得数组类型参数

Controller中的业务方法数组名称与请求参数的名称一致，且请求参数数量＞ 1个，参数值会自动映射匹配

http://localhost/user/requestParam3?strs=itheima&strs=14

```java
@RequestMapping("/requestParam9")
public String requestParam9(String[] strs){
    System.out.println(strs[0]+","+strs[1]);
    return "page.jsp";
}
```

## 5.4 @RequestParam注解

当请求的参数名称与Controller的业务方法参数名称不一致时，就需要通过@RequestParam注解进行显示的绑定

类型： 形参注解

位置：处理器类中的方法形参前方

作用：绑定请求参数与对应处理方法形参间的关系

```java
@RequestMapping("/requestParam2")
public String requestParam2(@RequestParam(
                            name = "userName",
                            required = true,
                            defaultValue = "itheima") String name){
    
    System.out.println("name="+name);
    return "page.jsp";
}
```

## 5.5 获得集合类型参数

保存简单类型数据，请求参数名与处理器方法形参名保持一致，且请求参数数量＞ 1个
访问URL： http://localhost/requestParam10?nick=Jockme&nick=zahc

```java
@RequestMapping("/requestParam10")
public String requestParam10(@RequestParam("nick") List<String> nick){
    System.out.println(nick);
    return "page.jsp";
}
```

## 5.6 获取Restful风格的参数

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

## 5.7 日期类型转换

1. 在配置文件中声明转换器

```xml
<!--5.启用自定义Converter-->
<mvc:annotation-driven conversion-service="conversionService"/>
<!--1.设定格式类型Converter，注册为Bean，受SpringMVC管理-->
<bean id="conversionService"
      class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
    <!--2.自定义Converter格式类型设定，该设定使用的是同类型覆盖的思想-->
    <property name="formatters">
        <!--3.使用set保障相同类型的转换器仅保留一个，避免冲突-->
        <set>
            <!--4.设置具体的格式类型-->
            <bean class="org.springframework.format.datetime.DateFormatter">
                <!--5.类型规则-->
                <property name="pattern" value="yyyy-MM-dd"/>
            </bean>
        </set>
    </property>
</bean>
```

日期类型格式转换（简化版）
名称： @DateTimeFormat
类型： 形参注解、成员变量注解
位置：形参前面 或 成员变量上方
作用：为当前参数或变量指定类型转换规则
范例：  

```java
public String requestParam12(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
    System.out.println("date="+date);
    return "page.jsp";
}
```

```java
@DateTimeFormat(pattern = "yyyy-MM-dd")
private Date birthday;
```

- 注意：依赖注解驱动支持  

  <mvc:annotation-driven />  

## 5.8 自定义类型转换器

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
    return "page.jsp";
}
```

Head数据获取

名称： @RequestHeader
类型： 形参注解
位置：处理器类中的方法形参前方
作用：绑定请求头数据与对应处理方法形参间的关系
范例：

```java
@RequestMapping("/headApi")
public String headApi(@RequestHeader("Accept-Language") String head){
    System.out.println(head);
    return "page.jsp";
}  
```

Cookie数据获取

名称： @CookieValue
类型： 形参注解
位置：处理器类中的方法形参前方
作用：绑定请求Cookie数据与对应处理方法形参间的关系
范例：

```java
@RequestMapping("/cookieApi")
public String cookieApi(@CookieValue("JSESSIONID") String jsessionid){
    System.out.println(jsessionid);
    return "page.jsp";
}  
```

Session数据获取

名称： @SessionAttribute
类型： 形参注解
位置：处理器类中的方法形参前方
作用：绑定请求Session数据与对应处理方法形参间的关系
范例：

```java
@RequestMapping("/sessionApi")
public String sessionApi(@SessionAttribute("name") String name){
    System.out.println(name);
    return "page.jsp";
}  
```

Session数据设置（了解）

名称： @SessionAttributes
类型： 类注解
位置：处理器类上方
作用：声明放入session范围的变量名称，适用于Model类型数据传参
范例：

```java
@Controller
@SessionAttributes(names={"name"})
public class ServletController {
    @RequestMapping("/setSessionData2")
    public String setSessionDate2(Model model) {
        model.addAttribute("name", "Jock2");
        return "page.jsp";
    }
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