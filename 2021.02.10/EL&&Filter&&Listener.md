# 1. EL表达式 

## 1.1 概述

EL表达式，全称是Expression Language。意为表达式语言。它是Servlet规范中的一部分，是JSP2.0规范加入的内容。

其作用是用于在JSP页面中获取数据，从而让我们的JSP脱离java代码块和JSP表达式。

EL表达式的语法格式非常简单，写为 <b><font color='red' size='5'>${表达式内容}</font></b>

例如：在浏览器中输出请求域中名称为message的内容。

假定，我们在请求域中存入了一个名称为message的数据（`request.setAttribute("message","EL");`），此时在jsp中获取的方式，如下表显示：

| Java代码块                                                   | JSP表达式                              | EL表达式                              |
| :----------------------------------------------------------- | :------------------------------------- | :------------------------------------ |
| `<%<br/> <br/> String message = (String)request.getAttribute("message");<br/> out.write(message);<br/>%>` | `<%=request.getAttribute("message")%>` | <font color='red'>`${message}`</font> |

通过上面我们可以看出，都可以从请求域中获取数据，但是EL表达式写起来是最简单的方式。这也是以后我们在实际开发中，当使用JSP作为视图时，绝大多数都会采用的方式。

## 1.2 简单案例

1. 创建一个web项目

2. 在web目录下创建el01.jsp

3. 在文件中向域对象添加数据

4. 使用三种方式获取域对象中的数据（java代码块、JSP代码块、EL表达式）

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
     <head>
       <title>EL表达式入门案例</title>
     </head>
     <body>
       <%--使用java代码在请求域中存入一个名称为message的数据--%>
       <% request.setAttribute("message","Expression Language");%>
   
       Java代码块获取：<% out.print(request.getAttribute("message"));%>
       <br/>
       JSP表达式获取：<%=request.getAttribute("message")%>
       <br/>
       EL表达式获取：${message}
     </body>
   </html>
   ```

5. 启动tomcat，测试

## 1.3 获取数据

1. 基本数据类型
2. 自定义对象类型
3. 数组类型
4. List集合
5. Map集合

示例代码：

```java
public class Student {
    private String name;
    private int age;

    public Student() {
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

```jsp
<%@ page import="cn.edu.hdu.bean.Student" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%--1.获取基本数据类型--%>
<% pageContext.setAttribute("num",10); %>
基本数据类型：${num} <br>

<%--2.获取自定义对象类型--%>
<%
    Student stu = new Student("张三",23);
	stu = null;
    pageContext.setAttribute("stu",stu);
%>
<%--EL表达式中没有空指针异常--%>
自定义对象：${stu} <br>
<%--stu.name 实现原理 getName()--%>
学生姓名：${stu.name} <br>
学生年龄：${stu.age} <br>

<%--3.获取数组类型--%>
<%
    String[] arr = {"hello","world"};
    pageContext.setAttribute("arr",arr);
%>
数组：${arr}  <br>
0索引元素：${arr[0]} <br>
1索引元素：${arr[1]} <br>
<%--EL表达式中没有索引越界异常--%>
8索引元素：${arr[8]} <br>
<%--EL表达式中没有字符串拼接--%>
0索引拼接1索引的元素：${arr[0]} + ${arr[1]} <br>

<%--4.获取List集合--%>
<%
    ArrayList<String> list = new ArrayList<>();
    list.add("aaa");
    list.add("bbb");
    pageContext.setAttribute("list",list);
%>
List集合：${list} <br>
0索引元素：${list[0]} <br>

<%--5.获取Map集合--%>
<%
    HashMap<String,Student> map = new HashMap<>();
    map.put("hm01",new Student("张三",23));
    map.put("hm02",new Student("李四",24));
    pageContext.setAttribute("map",map);
%>
Map集合：${map}  <br>
第一个学生对象：${map.hm01}  <br>
第一个学生对象的姓名：${map.hm01.name}
</body>
</html>
```

## 1.4 注意事项

1. EL表达式没有空指针异常
2. EL表达式没有索引越界异常
3. EL表达式没有字符串的拼接

## 1.5 运算符

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/1.png)

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/2.png)

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/3.png)

示例代码：

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EL表达式运算符</title>
</head>
<body>
    <%--empty--%>
    <%
        String str1 = null;
        String str2 = "";
        int[] arr = {};
    %>
    ${empty str1} <br>
    ${empty str2} <br>
    ${empty arr} <br>

    <%--三元运算符。获取性别的数据，在对应的按钮上进行勾选--%>
    <% pageContext.setAttribute("gender","women"); %>
    <input type="radio" name="gender" value="men" ${gender == "men" ? "checked" : ""}>男
    <input type="radio" name="gender" value="women" ${gender == "women" ? "checked" : ""}>女
</body>
</html>
```

## 1.6 使用细节

1. EL表达式能够获取四大域对象的数据，根据名称从小到大在域对象中查找
2. 还可以获取JSP其他8个隐式对象，并调用对象中的方法

示例代码：

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EL表达式使用细节</title>
</head>
<body>
    <%--获取四大域对象中的数据--%>
    <%
        //pageContext.setAttribute("username","zhangsan");
        request.setAttribute("username","zhangsan");
        //session.setAttribute("username","zhangsan");
        //application.setAttribute("username","zhangsan");
    %>
    ${username} <br>

    <%--获取JSP中其他八个隐式对象  获取虚拟目录名称--%>
    <%= request.getContextPath()%>
    ${pageContext.request.contextPath}
</body>
</html>
```

## 1.7 隐式对象

EL表达式也为我们提供隐式对象，可以让我们不声明直接来使用，十一个对象见下表，需要注意的是，它和JSP的隐式对象不是一回事：

| EL中的隐式对象   | 类型                          | 对应JSP隐式对象 | 备注                                    |
| ---------------- | ----------------------------- | --------------- | --------------------------------------- |
| PageContext      | Javax.serlvet.jsp.PageContext | PageContext     | 完全一样                                |
| ApplicationScope | Java.util.Map                 | 没有            | 应用层范围                              |
| SessionScope     | Java.util.Map                 | 没有            | 会话范围                                |
| RequestScope     | Java.util.Map                 | 没有            | 请求范围                                |
| PageScope        | Java.util.Map                 | 没有            | 页面层范围                              |
| Header           | Java.util.Map                 | 没有            | 请求消息头key，值是value（一个）        |
| HeaderValues     | Java.util.Map                 | 没有            | 请求消息头key，值是数组（一个头多个值） |
| Param            | Java.util.Map                 | 没有            | 请求参数key，值是value（一个）          |
| ParamValues      | Java.util.Map                 | 没有            | 请求参数key，值是数组（一个名称多个值） |
| InitParam        | Java.util.Map                 | 没有            | 全局参数，key是参数名称，value是参数值  |
| Cookie           | Java.util.Map                 | 没有            | Key是cookie的名称，value是cookie对象    |

示例代码：

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EL表达式11个隐式对象</title>
</head>
<body>
    <%--pageContext对象 可以获取其他三个域对象和JSP中八个隐式对象--%>
    ${pageContext.request.contextPath} <br>

    <%--applicationScope sessionScope requestScope pageScope 操作四大域对象中的数据--%>
    <% request.setAttribute("username","zhangsan"); %>
    ${username} <br>
    ${requestScope.username} <br>

    <%--header headerValues  获取请求头数据--%>
    ${header["connection"]} <br>
    ${headerValues["connection"][0]} <br>

    <%--param paramValues 获取请求参数数据--%>
    ${param.username} <br>
    ${paramValues.hobby[0]} <br>
    ${paramValues.hobby[1]} <br>

    <%--initParam 获取全局配置参数--%>
    ${initParam["pname"]}  <br>

    <%--cookie 获取cookie信息--%>
    ${cookie}  <br> <%--获取Map集合--%>
    ${cookie.JSESSIONID}  <br> <%--获取map集合中第二个元素--%>
    ${cookie.JSESSIONID.name}  <br> <%--获取cookie对象的名称--%>
    ${cookie.JSESSIONID.value} <%--获取cookie对象的值--%>


</body>
</html>
```

# 2. JSTL

## 2.1 概述

JSTL的全称是：JSP Standard Tag Libary。它是JSP中标准的标签库。它是由Apache实现的。

开发人员可以利用这些标签取代JSP页面上的Java代码，从而提高程序的可读性，降低程序的维护难度。

它由以下5个部分组成：

| 组成      | 作用         | 说明                           |
| --------- | ------------ | ------------------------------ |
| Core      | 核心标签库。 | 通用逻辑处理                   |
| Fmt       | 国际化有关。 | 需要不同地域显示不同语言时使用 |
| Functions | EL函数       | EL表达式可以使用的方法         |
| SQL       | 操作数据库。 | 不用                           |
| XML       | 操作XML。    | 不用                           |

## 2.2 核心标签库

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/4.png)

## 2.3 使用案例

1. 创建一个web项目
2. 在web目录下创建一个WEB-INF目录
3. 在WEB-INF目录下创建一个libs目录，将JSTL的jar包导入
4. 创建JSP文件，通过taglib导入JSTL标签库
5. 对流程控制和迭代遍历的标签进行使用
6. 启动tomcat，测试

示例代码：

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>流程控制</title>
</head>
<body>
    <%--向域对象中添加成绩数据--%>
    ${pageContext.setAttribute("score","T")}

    <%--对成绩进行判断--%>
    <c:if test="${score eq 'A'}">
        优秀
    </c:if>

    <%--对成绩进行多条件判断--%>
    <c:choose>
        <c:when test="${score eq 'A'}">优秀</c:when>
        <c:when test="${score eq 'B'}">良好</c:when>
        <c:when test="${score eq 'C'}">及格</c:when>
        <c:when test="${score eq 'D'}">较差</c:when>
        <c:otherwise>成绩非法</c:otherwise>
    </c:choose>
</body>
</html>
```

```jsp
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>循环</title>
</head>
<body>
    <%--向域对象中添加集合--%>
    <%
        ArrayList<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        list.add("dd");
        pageContext.setAttribute("list",list);
    %>

    <%--遍历集合--%>
    <c:forEach items="${list}" var="str">
        ${str} <br>
    </c:forEach>
</body>
</html>
```

# 3. Filter

## 3.1 简介

过滤器——Filter，它是JavaWeb三大组件之一。另外两个是Servlet和Listener。

A filter is an object that performs filtering tasks on either the request to a  resource (a servlet or static content), or on the response from a resource, or  both. 

Filter是一个接口，使用的前提是要实现接口

方法：

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/5.png)

配置方式：

注解方式或者配置文件方式

## 3.2 FilterChain

FilterChain是一个接口，代表过滤器链对象。由Servlet容器提供实现类对象，可直接使用

多个过滤器就可以组成过滤器链

方法：

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/6.png)

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/7.png)

## 3.3 使用案例

### 3.3.1 需求说明

通过filter过滤器解决多个资源的中文乱码问题

### 3.3.2 案例目的

掌握filter过滤器的使用

### 3.3.3 实现步骤

1. 创建一个web项目
2. 创建两个Servlet功能类，都向客户端写出中文数据
3. 创建一个filter过滤器实现类，重写doFilter核心方法
4. 在方法内解决中文乱码，并放行
5. 启动tomcat，测试

### 3.3.4 示例代码

Servlet：

```java
@WebServlet("/servletDemo01")
public class ServletDemo01 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servletDemo01执行了...");
        //resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("servletDemo01执行了...");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

```java
@WebServlet("/servletDemo02")
public class ServletDemo02 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servletDemo02执行了...");
        //resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("servletDemo02执行了...");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

Filter：（注解配置）

```java
//@WebFilter("/*")
public class FilterDemo01 implements Filter{

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filterDemo01执行了...");

        //处理乱码
        servletResponse.setContentType("text/html;charset=UTF-8");

        //放行
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
```

## 3.4 使用细节

配置文件方式：

```xml
<filter>
    <filter-name>filterDemo01</filter-name>
    <filter-class>com.itheima.filter.FilterDemo02</filter-class>
</filter>
<filter-mapping>
    <filter-name>filterDemo01</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>-->
```

如果有多个过滤器，则取决于过滤器映射的顺序（标签<filter-mapping>）

## 3.5 生命周期

+ 创建

  当应用加载时实例化对象并执行init初始化方法

+ 服务

  对象提供服务的过程，执行doFilter方法

+ 销毁

  当应用卸载时或服务器停止时对象销毁，执行destory方法

**Filter的实例对象在内存中也只有一份。所以也是单例的。**

示例代码：

```java
//@WebFilter("/*")
public class FilterDemo03 implements Filter{

    /*
        初始化方法
     */
    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("对象初始化成功了...");
    }

    /*
        提供服务方法
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filterDemo03执行了...");

        //处理乱码
        servletResponse.setContentType("text/html;charset=UTF-8");

        //放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

    /*
        对象销毁
     */
    @Override
    public void destroy() {
        System.out.println("对象销毁了...");
    }
}
```

## 3.6 FilterConfig

FilterConfig是一个接口，代表过滤器的配置对象，可以加载一些初始化参数

方法：

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/8.png)

示例代码：

```java
//@WebFilter("/*")
public class FilterDemo04 implements Filter{

    /*
        初始化方法
     */
    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("对象初始化成功了...");

        //获取过滤器名称
        String filterName = filterConfig.getFilterName();
        System.out.println(filterName);

        //根据name获取value
        String username = filterConfig.getInitParameter("username");
        System.out.println(username);
    }
……
}
```

```xml
<filter>
    <filter-name>filterDemo04</filter-name>
    <filter-class>com.itheima.filter.FilterDemo04</filter-class>
    <init-param>
        <param-name>username</param-name>
        <param-value>zhangsan</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>filterDemo04</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

## 3.7 过滤器的五种拦截行为

Filter过滤器默认拦截的是请求，但是在实际开发中，我们还有请求转发和请求包含，以及由服务器触发调用的全局错误页面。默认情况下过滤器是不参与过滤的，要想使用，需要我们配置。

示例代码：

Servlet：

```java
@WebServlet("/servletDemo03")
public class ServletDemo03 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servletDemo03执行了...");

        //int i = 1/ 0;

        //请求转发
        //req.getRequestDispatcher("/index.jsp").forward(req,resp);

        //请求包含
        req.getRequestDispatcher("/index.jsp").include(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

Filter：

```java
//@WebFilter("/*")
public class FilterDemo05 implements Filter{
    /*
        提供服务方法
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filterDemo05执行了...");

        //处理乱码
        servletResponse.setContentType("text/html;charset=UTF-8");

        //放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

}
```

web.xml：

```xml
<filter>
    <filter-name>filterDemo05</filter-name>
    <filter-class>com.itheima.filter.FilterDemo05</filter-class>
    <!--配置开启异步支持，当dispatcher配置ASYNC时，需要配置此行-->
    <async-supported>true</async-supported>
</filter>
<filter-mapping>
    <filter-name>filterDemo05</filter-name>
    <!--<url-pattern>/error.jsp</url-pattern>-->
    <url-pattern>/index.jsp</url-pattern>
    <!--过滤请求：默认值。-->
    <dispatcher>REQUEST</dispatcher>
    <!--过滤全局错误页面：当由服务器调用全局错误页面时，过滤器工作-->
    <dispatcher>ERROR</dispatcher>
    <!--过滤请求转发：当请求转发时，过滤器工作。-->
    <dispatcher>FORWARD</dispatcher>
    <!--过滤请求包含：当请求包含时，过滤器工作。它只能过滤动态包含，jsp的include指令是静态包含，过滤器不会起作用-->
    <dispatcher>INCLUDE</dispatcher>
    <!--过滤异步类型，它要求我们在filter标签中配置开启异步支持-->
    <dispatcher>ASYNC</dispatcher>
</filter-mapping>

<!--配置全局错误页面-->
<error-page>
    <exception-type>java.lang.Exception</exception-type>
    <location>/error.jsp</location>
</error-page>
<error-page>
    <error-code>404</error-code>
    <location>/error.jsp</location>
</error-page>
```

## 3.8 过滤器与Servlet的区别

| 方法/类型                                          | Servlet                                                      | Filter                                                       | 备注                                                         |
| -------------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 初始化                                        方法 | `void   init(ServletConfig);   `                             | `void init(FilterConfig);   `                                | 几乎一样，都是在web.xml中配置参数，用该对象的方法可以获取到。 |
| 提供服务方法                                       | `void   service(request,response);                                               ` | `void   dofilter(request,response,FilterChain);                                   ` | Filter比Servlet多了一个FilterChain，它不仅能完成Servlet的功能，而且还可以决定程序是否能继续执行。所以过滤器比Servlet更为强大。   在Struts2中，核心控制器就是一个过滤器。 |
| 销毁方法                                           | `void destroy();`                                            | `void destroy();`                                            |                                                              |

# 4. Listener

## 4.1 简介

所有的监听器都是基于<font color='red'>观察者模式</font>的

三个组成部分：

1. 事件源：触发事件的对象
2. 事件：触发的动作，封装了事件源
3. 监听器：当事件源触发了事件后，可以完成功能

监听器可以监听：对象的创建和销毁、域对象中属性的变化、会话的相关内容

一共有8种监听器

## 4.2 监听对象的监听器

### 4.2.1 ServletContextListener

用于监听ServletContext对象的创建和销毁

方法：

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/9.png)

ServletContextEvent代表事件对象

事件对象中封装了事件源，也就是ServletContext

真正的事件指的是创建或销毁ServletContext对象的操作

### 4.2.2 HttpSessionListener

用于监听HttpSession对象的创建和销毁

方法：

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/10.png)

HttpSessionEvent代表事件对象

事件对象中封装了事件源，也就是HttpSession

真正的事件指的是创建或销毁HttpSession对象的操作

### 4.2.3 ServletRequestListener

用于监听ServletRequest对象的创建和销毁

方法：

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/11.png)

ServletRequestEvent代表事件对象

事件对象中封装了事件源，也就是ServletRequest

真正的事件指的是创建或销毁ServletRequest对象的操作

## 4.3 监听域对象属性变化的监听器

### 4.3.1 ServletContextAttributeListener

用于监听ServletContext域（应用域）中属性的变化

方法：

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/12.png)

ServletContextAttributeEvent代表事件对象

事件对象中封装了事件源，也就是ServletContext

真正的事件指的是添加、移除、替换应用域中属性的操作

### 4.3.2 HttpSessionAttributeListener

用于监听HttpSession域（会话域）中属性的变化

方法：

![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/13.png)

HttpSessionBindingEvent代表事件对象

事件对象中封装了事件源，也就是HttpSession

真正的事件指的是添加、移除、替换会话域中属性的操作

### 4.3.3 ServletRequestAttributeListener

用于监听ServletRequest域（请求域）中属性的变化

方法：

![14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/14.png)

ServletRequestAttributeEvent代表事件对象

事件对象中封装了事件源，也就是ServletRequest

真正的事件指的是添加、移除、替换请求域中属性的操作

## 4.4 会话相关的感知型监听器

### 4.4.1 HttpSessionBindingListener

用于感知对象和和会话域绑定的监听器

方法：

![15](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/15.png)

HttpSessionBindingEvent代表事件对象

事件对象中封装了事件源，也就是HttpSession

真正的事件指的是添加、移除会话域中数据的操作

### 4.4.2 HttpSessionActivationListener

用于感知会话域中对象钝化和活化的监听器

方法：

![16](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.10/pics/16.png)

HttpSessionEvent代表事件对象

事件对象中封装了事件源，也就是HttpSession

真正的事件指的是会话域中数据钝化、活化的操作

## 4.5 ServletContextListener的使用

示例代码：

```java
/*
    ServletContext对象的创建和销毁的监听器
 */
//@WebListener
public class ServletContextListenerDemo implements ServletContextListener{
    /*
        ServletContext对象创建的时候执行此方法
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("监听到了对象的创建...");

        //获取对象
        ServletContext servletContext = sce.getServletContext();
        //System.out.println(servletContext);

        //添加属性
        servletContext.setAttribute("username","zhangsan");

        //替换属性
        servletContext.setAttribute("username","lisi");

        //移除属性
        servletContext.removeAttribute("username");
    }

    /*
        ServletContext对象销毁的时候执行此方法
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("监听到了对象的销毁...");
    }
}
```

## 4.6 ServletContextAttributeListener的使用

结合上一节，示例代码：

```java
/*
    应用域对象中的属性变化的监听器
 */
//@WebListener
public class ServletContextAttributeListenerDemo implements ServletContextAttributeListener{
    /*
        向应用域对象中添加属性时执行此方法
     */
    @Override
    public void attributeAdded(ServletContextAttributeEvent scae) {
        System.out.println("监听到了属性的添加...");

        //获取应用域对象
        ServletContext servletContext = scae.getServletContext();
        //获取属性
        Object value = servletContext.getAttribute("username");
        System.out.println(value);
    }

    /*
        向应用域对象中替换属性时执行此方法
     */
    @Override
    public void attributeReplaced(ServletContextAttributeEvent scae) {
        System.out.println("监听到了属性的替换...");

        //获取应用域对象
        ServletContext servletContext = scae.getServletContext();
        //获取属性
        Object value = servletContext.getAttribute("username");
        System.out.println(value);
    }

    /*
        向应用域对象中移除属性时执行此方法
     */
    @Override
    public void attributeRemoved(ServletContextAttributeEvent scae) {
        System.out.println("监听到了属性的移除...");

        //获取应用域对象
        ServletContext servletContext = scae.getServletContext();
        //获取属性
        Object value = servletContext.getAttribute("username");
        System.out.println(value);
    }
}
```

如果采用web.xml配置方式

```xml
!--配置监听器-->
<listener>
    <listener-class>com.itheima.listener.ServletContextListenerDemo</listener-class>
</listener>

<listener>
    <listener-class>com.itheima.listener.ServletContextAttributeListenerDemo</listener-class>
</listener>
```

# 5. 综合管理案例-学生管理系统优化

## 5.1 案例需求

在前一个系统上进行优化：

1. 解决乱码
2. 检查登录
3. 优化JSP页面

## 5.2 解决乱码

使用过滤器实现所有资源的编码统一（不用在每一个Servlet中再写一遍处理编码的逻辑）

示例代码：

```java
/*
    解决全局乱码问题
 */
@WebFilter("/*")
public class EncodingFilter implements Filter{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try{
            //1.将请求和响应对象转换为和HTTP协议相关
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            //2.设置编码格式
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");

            //3.放行
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
```

## 5.3 检查登录

使用过滤器，示例代码：

```java
/*
    检查登录
 */
@WebFilter(value = {"/addStudent.jsp","/listStudentServlet"})
public class LoginFilter implements Filter{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try{
            //1.将请求和响应对象转换为和HTTP协议相关
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            //2.获取会话域对象中数据
            Object username = request.getSession().getAttribute("username");

            //3.判断用户名
            if(username == null || "".equals(username)) {
                //重定向到登录页面
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            //4.放行
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## 5.4 优化JSP页面

通过EL表达式和JSTL替换之前的Java代码块和JSP表达式

1. addStudent.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加学生</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/addStudentServlet" method="get" autocomplete="off">
    学生姓名：<input type="text" name="username"> <br>
    学生年龄：<input type="number" name="age"> <br>
    学生成绩：<input type="number" name="score"> <br>
    <button type="submit">保存</button>
</form>
</body>
</html>
```

2. index.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>学生管理系统首页</title>
</head>
<body>
    <%--
        获取会话域中的数据
        如果获取到了则显示添加和查看功能的超链接
        如果没获取到则显示登录功能的超链接
    --%>
    <c:if test="${sessionScope.username eq null}">
        <a href="${pageContext.request.contextPath}/login.jsp">请登录</a>
    </c:if>

    <c:if test="${sessionScope.username ne null}">
        <a href="${pageContext.request.contextPath}/addStudent.jsp">添加学生</a>
        <a href="${pageContext.request.contextPath}/listStudentServlet">查看学生</a>
    </c:if>

</body>
</html>
```

3. listStudent.jsp

```jsp
<%@ page import="com.itheima.bean.Student" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>查看学生</title>
</head>
<body>
    <table width="600px" border="1px">
        <tr>
            <th>学生姓名</th>
            <th>学生年龄</th>
            <th>学生成绩</th>
        </tr>
        <c:forEach items="${students}" var="s">
            <tr align="center">
                <td>${s.username}</td>
                <td>${s.age}</td>
                <td>${s.score}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
```

4. login.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>学生登录</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/loginStudentServlet" method="get" autocomplete="off">
        姓名：<input type="text" name="username"> <br>
        密码：<input type="password" name="password"> <br>
        <button type="submit">登录</button>
    </form>
</body>
</html>
```

