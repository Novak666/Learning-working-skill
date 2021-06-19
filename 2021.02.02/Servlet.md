# 1. Servlet

## 1.1 概述

Servlet是SUN公司提供的一套规范，名称就叫Servlet规范，它也是JavaEE规范之一。我们可以像学习Java基础一样，通过API来学习Servlet。这里需要注意的是，在我们之前JDK的API中是没有Servlet规范的相关内容，需要使用JavaEE的API。

- A servlet is a small Java program that runs within a Web server. Servlets  receive and respond to requests from Web clients, usually across HTTP, the  HyperText Transfer Protocol. 

Servlet是一种运行在Web服务器端的小型Java程序。通过HTTP协议，Servlet可以接收和响应来自客户端的请求。

- To implement this interface, you can write a generic servlet that extends  `javax.servlet.GenericServlet` or an HTTP servlet that extends  `javax.servlet.http.HttpServlet`. 

想要实现Servlet的功能，可以去实现Servlet接口，也可以自定义类去继承实现类GenericServlet或者HttpServlet

- This interface defines methods to initialize a servlet, to service requests,  and to remove a servlet from the server. These are known as life-cycle methods  and are called in the following sequence:  
  1. The servlet is constructed, then initialized with the `init`  method.  
  2. Any calls from clients to the `service` method are handled.  
  3. The servlet is taken out of service, then destroyed with the  `destroy` method, then garbage collected and finalized. 

1. init方法来初始化
2. 客户端的请求都在service方法中处理
3. destroy方法用来销毁servlet

- In addition to the life-cycle methods, this interface provides the  `getServletConfig` method, which the servlet can use to get any  startup information, and the `getServletInfo` method, which allows  the servlet to return basic information about itself, such as author, version,  and copyright.

Servlet还可以提供配置

## 1.2 构建Servlet项目

1. 在IDEA中创建一个JavaWeb项目

2. 将静态资源拷贝到项目的web目录下

3. 在web.xml配置文件中，修改默认主页

4. 在项目的src路径下编写一个类，实现Servlet接口

5. 重写service方法，输出一句话即可

6. 在web.xml配置文件中，配置Servlet相关信息

   ![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.02/pics/1.png)

7. 启动tomcat，测试

## 1.3 Servlet执行过程

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.02/pics/2.png)

## 1.4 Servlet类视图

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.02/pics/3.png)

## 1.5 Servlet实现方式

我们在实现Servlet功能时，可以选择以下三种方式：

第一种：实现Servlet接口，接口中的方法必须全部实现。

使用此种方式，表示接口中的所有方法在需求方面都有重写的必要。此种方式支持最大程度的自定义。

第二种：继承GenericServlet，service方法必须重写，其他方可根据需求，选择性重写。

使用此种方式，表示只在接收和响应客户端请求这方面有重写的需求，而其他方法可根据实际需求选择性重写，使我们的开发Servlet变得简单。但是，此种方式是和HTTP协议无关的。

第三种：继承HttpServlet，它是javax.servlet.http包下的一个抽象类，是GenericServlet的子类。<b><font color='red'>如果我们选择继承HttpServlet时，只需要重写doGet和doPost方法，不要覆盖service方法。</font></b>

使用此种方式，表示我们的请求和响应需要和HTTP协议相关。也就是说，我们是通过HTTP协议来访问的。那么每次请求和响应都符合HTTP协议的规范。请求的方式就是HTTP协议所支持的方式（目前我们只知道GET和POST，而实际HTTP协议支持7种请求方式，GET POST PUT DELETE TRACE OPTIONS HEAD )。

## 1.6 Servlet生命周期

对象的生命周期，就是对象从生到死的过程，即：出生——活着——死亡。用更偏向 于开发的官方说法就是对象创建到销毁的过程。

出生：请求第一次到达Servlet时，对象就创建出来，并且初始化成功。<font color='red'>只出生一次，就放到内存中。init方法</font>

活着：服务器提供服务的整个过程中，该对象一直存在，每次只是执行service方法。

死亡：当服务停止时，或者服务器宕机时，对象消亡。<font color='red'>destroy方法</font>

通过分析Servlet的生命周期我们发现，它的实例化和初始化只会在请求第一次到达Servlet时执行，而销毁只会在Tomcat服务器停止时执行，由此我们得出一个结论，Servlet对象只会创建一次，销毁一次。所以，Servlet对象只有一个实例。如果一个对象实例在应用中是唯一的存在，那么我们就说它是单实例的，即运用了<font color='red'>单例模式。</font>

## 1.7 Servlet线程安全

由于Servlet运用了单例模式，即整个应用中只有一个实例对象，所以我们需要分析这个唯一的实例中的类成员是否线程安全。接下来，我们来看下面的的示例：

```java
/*
    Servlet线程安全
 */
public class ServletDemo04 extends HttpServlet{
    //1.定义用户名成员变量
    //private String username = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = null;
        //synchronized (this) {
            //2.获取用户名
            username = req.getParameter("username");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //3.获取输出流对象
            PrintWriter pw = resp.getWriter();

            //4.响应给客户端浏览器
            pw.print("welcome:" + username);

            //5.关流
            pw.close();
        //}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

启动两个浏览器，输入不同的参数，访问之后发现输出的结果都是一样，所以出现线程安全问题

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.02/pics/4.png)

分析产生这个问题的根本原因，其实就是因为<font color='red'>Servlet是单例</font>，单例对象的类成员只会随类实例化时初始化一次，之后的操作都是改变，而不会重新初始化。

**解决方案：**

1. 就是在Servlet中定义类成员要慎重。如果类成员是共用的，并且只会在初始化时赋值，其余时间都是获取的话，那么是没问题。如果类成员并非共用，或者每次使用都有可能对其赋值，那么就要考虑线程安全问题了，把它定义到doGet或者doPost方法里面去就可以了。
2. 或者用锁。

## 1.8 Servlet映射方式

1. 和前面一样，在web.xml里面配置具体的路径名

2. /开头+/*的方式

   例如：映射为：/servlet/*

3. 通配符+结尾

   例如：映射为：*.do

3种方式有优先级，依次减弱

## 1.9 Servlet创建时机

Servlet的创建默认情况下是请求第一次到达Servlet时创建的。

- 第一种：应用加载时创建Servlet，它的优势是在服务器启动时，就把需要的对象都创建完成了，从而在使用的时候减少了创建对象的时间，提高了首次执行的效率。它的弊端也同样明显，因为在应用加载时就创建了Servlet对象，因此，导致内存中充斥着大量用不上的Servlet对象，造成了内存的浪费。
- 第二种：请求第一次访问是创建Servlet，它的优势就是减少了对服务器内存的浪费，因为那些一直没有被访问过的Servlet对象都没有创建，因此也提高了服务器的启动时间。而它的弊端就是，如果有一些要在应用加载时就做的初始化操作，它就没法完成，从而要考虑其他技术实现。

在web.xml中是支持对Servlet的创建时机进行配置的，配置的方式如下：

```xml
<!--配置ServletDemo3-->
<servlet>
    <servlet-name>servletDemo3</servlet-name>
    <servlet-class>com.itheima.web.servlet.ServletDemo3</servlet-class>
    <!--配置Servlet的创建顺序，当配置此标签时，Servlet就会改为应用加载时创建
        配置项的取值只能是正整数（包括0），数值越小，表明创建的优先级越高 负整数或不写代表第一次访问时创建
    -->
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>servletDemo3</servlet-name>
    <url-pattern>/servletDemo3</url-pattern>
</servlet-mapping>
```

## 1.10 默认Servlet

在Tomcat的conf目录下的web.xml中有一个默认的Servlet。

它的映射路径是<b><font color='red'>`<url-pattern>/<url-pattern>`</font></b>，我们在发送请求时，首先会在我们应用中的web.xml中查找映射配置，找到就执行，这块没有问题。但是当找不到对应的Servlet路径时，就去找默认的Servlet，由默认Servlet处理。比如找不到资源时浏览器显示404错误时，就是由这个默认的Servlet实现的。

# 2. ServletConfig

## 2.1 概述

ServletConfig是Servlet的配置参数对象，在Servlet规范中，允许为每个Servlet都提供一些初始化配置。所以，每个Servlet都一个自己的ServletConfig。它的作用是在Servlet初始化期间，把一些配置信息传递给Servlet。

## 2.2 生命周期

由于它是在初始化阶段读取了web.xml中为Servlet准备的初始化配置，并把配置信息传递给Servlet，所以生命周期与Servlet相同。这里需要注意的是，如果Servlet配置了`<load-on-startup>1</load-on-startup>`，那么ServletConfig也会在应用加载时创建。

## 2.3 配置

使用`<servlet>`标签中的`<init-param>`标签来配置。Servlet的初始化参数都是配置在Servlet的声明部分的。并且每个Servlet都支持有多个初始化参数，并且初始化参数都是以键值对的形式存在的。接下来，我们看配置示例：

```xml
<!--配置ServletDemo8-->
<servlet>
    <servlet-name>servletDemo8</servlet-name>
    <servlet-class>com.itheima.web.servlet.ServletDemo8</servlet-class>
    <!--配置初始化参数-->
    <init-param>
        <!--用于获取初始化参数的key-->
        <param-name>encoding</param-name>
        <!--初始化参数的值-->
        <param-value>UTF-8</param-value>
    </init-param>
    <!--每个初始化参数都需要用到init-param标签-->
    <init-param>
        <param-name>servletInfo</param-name>
        <param-value>This is Demo8</param-value>
    </init-param>
</servlet>
<servlet-mapping>
    <servlet-name>servletDemo8</servlet-name>
    <url-pattern>/servletDemo8</url-pattern>
</servlet-mapping>
```

## 2.4 获取

获取ServletConfig实例对象

public class ServletDemo8 extends HttpServlet {

```java
public class ServletDemo8 extends HttpServlet {

    //定义Servlet配置对象ServletConfig
    private ServletConfig servletConfig;

    /**
     * 在初始化时为ServletConfig赋值
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
    }
    ……
```
## 2.5 常用方法

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.02/pics/5.png)

```java
@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.输出ServletConfig
        System.out.println(servletConfig);
        //2.获取Servlet的名称
        String servletName= servletConfig.getServletName();
        System.out.println(servletName);
        //3.获取字符集编码
        String encoding = servletConfig.getInitParameter("encoding");
        System.out.println(encoding);
        //4.获取所有初始化参数名称的枚举
        Enumeration<String> names = servletConfig.getInitParameterNames();
        //遍历names
        while(names.hasMoreElements()){
            //取出每个name
            String name = names.nextElement();
            //根据key获取value
            String value = servletConfig.getInitParameter(name);
            System.out.println("name:"+name+",value:"+value);
        }
        //5.获取ServletContext对象
        ServletContext servletContext = servletConfig.getServletContext();
        System.out.println(servletContext);
    }
```

结果：

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.02/pics/6.png)

# 3. ServletContext

## 3.1 概述

ServletContext对象，它是应用上下文对象。每一个应用有且只有一个ServletContext对象。

**作用：**

1. 它可以配置和获得应用的全局初始化参数。
2. 实现让应用中所有Servlet间的数据共享。 

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.02/pics/7.png)

## 3.2 生命周期

出生： 应用一加载，该对象就被创建出来了。一个应用只有一个实例对象。(Servlet和ServletContext都是单例的)

活着：只要应用一直提供服务，该对象就一直存在。

死亡：应用被卸载（或者服务器挂了），该对象消亡。

## 3.3 域对象

域对象的概念，它指的是对象有作用域，即有作用范围。

域对象的作用，域对象可以实现数据共享。不同作用范围的域对象，共享数据的能力不一样。

在Servlet规范中，一共有4个域对象。今天我们讲解的ServletContext就是其中一个。它也是我们接触的第一个域对象。它是web应用中最大的作用域，叫application域。每个应用只有一个application域。它可以实现整个应用间的数据共享功能。

## 3.4 配置

ServletContext既然被称之为应用上下文对象，所以它的配置是针对<font color='red'>整个web应用项目</font>的配置，而非某个特定Servlet的配置。它的配置被称为应用的初始化参数配置。

配置的方式，需要在`<web-app>`根标签中使用`<context-param>`来配置初始化参数。具体代码如下：

```xml
<!--配置应用初始化参数-->
<context-param>
    <!--用于获取初始化参数的key-->
    <param-name>servletContextInfo</param-name>
    <!--初始化参数的值-->
    <param-value>This is application scope</param-value>
</context-param>
<!--每个应用初始化参数都需要用到context-param标签-->
<context-param>
    <param-name>globalEncoding</param-name>
    <param-value>UTF-8</param-value>
</context-param>
```

## 3.5 常用方法

```java
public class ServletContextDemo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取ServletContext对象
        ServletContext context = getServletContext();

        //获取全局配置的globalEncoding
        String value = context.getInitParameter("globalEncoding");
        System.out.println(value);

        //获取应用的访问虚拟目录
        String contextPath = context.getContextPath();
        System.out.println(contextPath);

        //根据虚拟目录获取应用部署的磁盘绝对路径
        //获取b.txt文件的绝对路径
        String b = context.getRealPath("/b.txt");
        System.out.println(b);

        //获取c.txt文件的绝对路径
        String c = context.getRealPath("/WEB-INF/c.txt");
        System.out.println(c);

        //获取a.txt文件的绝对路径
        String a = context.getRealPath("/WEB-INF/classes/a.txt");
        System.out.println(a);


        //向域对象中存储数据
        context.setAttribute("username","zhangsan");

        //移除域对象中username的数据
        //context.removeAttribute("username");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

# 4. Servlet注解开发

## 4.1 Servlet3.0

Servlet2.5最明显的特征就是Servlet的配置要求配在web.xml中，Servlet3.0开始出现注解配置的思想。

## 4.2 自动注解开发

1. 在IDEA中创建一个JavaWeb项目，此时选择JavaEE的版本为8，项目生成后不会有web.xml文件，但是要在web目录下手动添加一个WEB-INF的目录

2. 在src目录下自定义一个类继承HttpServlet，重写doGet和doPost方法

3. 添加WebServlet注解

   ![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.02/pics/8.png)

4. 启动tomcat，测试

## 4.3 注解解释

```java
/*
 * WebServlet注解
 * @since Servlet 3.0 (Section 8.1.1)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebServlet {

    /**
     * 指定Servlet的名称。
     * 相当于xml配置中<servlet>标签下的<servlet-name>
     */
    String name() default "";

    /**
     * 用于映射Servlet访问的url映射
     * 相当于xml配置时的<url-pattern>
     */
    String[] value() default {};

    /**
     * 相当于xml配置时的<url-pattern>
     */
    String[] urlPatterns() default {};

    /**
     * 用于配置Servlet的启动时机
     * 相当于xml配置的<load-on-startup>
     */
    int loadOnStartup() default -1;

    /**
     * 用于配置Servlet的初始化参数
     * 相当于xml配置的<init-param>
     */
    WebInitParam[] initParams() default {};

    /**
     * 用于配置Servlet是否支持异步
     * 相当于xml配置的<async-supported>
     */
    boolean asyncSupported() default false;

    /**
     * 用于指定Servlet的小图标
     */
    String smallIcon() default "";

    /**
     * 用于指定Servlet的大图标
     */
    String largeIcon() default "";

    /**
     * 用于指定Servlet的描述信息
     */
    String description() default "";

    /**
     * 用于指定Servlet的显示名称
     */
    String displayName() default "";
}
```

## 4.4 手动创建容器（了解）

1. 在IDEA中创建一个JavaWeb项目，此时选择JavaEE的版本为8，项目生成后不会有web.xml文件，但是要在web目录下手动添加一个WEB-INF的目录

2. 在src目录下自定义一个类继承HttpServlet，重写doGet和doPost方法

3. 自定义一个类，实现ServletContainerInitializer接口

4. 在src目录下创建一个META-INF目录，META-INF下创建一个services包，services包下创建一个javax.servlet.ServletContainerInitializer的文件，里面填写步骤3的全限定类名

5. 编写代码

   ![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.02/pics/9.png)

6. 启动tomcat，测试

# 5. IDEA导入Servlet项目

1. 生成Empty Project，命名为Servlet

2. 复制Modules到Servlet文件夹内

3. 在Project Structure中引入Module，选择Create module from existing sources，reuse，Dependencies中选择Module SDK，+上Tomcat依赖Libraries

   ![a1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.02/pics/a1.png)

4. 在Project Structure的Artifacts，生成war包
5. 配置Tomcat启动参数

6. 测试