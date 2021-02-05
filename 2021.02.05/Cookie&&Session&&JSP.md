# 1. 会话

## 1.1 概述

这里的会话，指的是web开发中的一次通话过程，当打开浏览器，访问网站地址后，会话开始，当关闭浏览器（或者到了过期时间），会话结束。这期间会产生多次请求和响应。

会话管理是为我们共享数据用的，并且是在不同请求间实现数据共享。也就是说，如果我们需要在多次请求间实现数据共享，就可以考虑使用会话管理技术了。

## 1.2 分类

在JavaEE的项目中，会话管理分为两类。分别是：客户端会话管理技术和服务端会话管理技术。

**客户端会话管理技术**

它是把要共享的数据保存到了客户端（也就是浏览器端）。每次请求时，把会话信息带到服务器，从而实现多次请求的数据共享。这样可以保证每次访问时先从本地缓存中获取数据，提高效率。

**服务端会话管理技术**

它本质仍是采用客户端会话管理技术，只不过保存到客户端的是一个特殊的标识，并且把要共享的数据保存到了服务端的内存对象中。每次请求时，把这个标识带到服务器端，然后使用这个标识，找到对应的内存空间，从而实现数据共享。

# 2. Cookie

## 2.1 属性

| 属性名称 | 属性作用                 | 是否重要 |
| -------- | ------------------------ | -------- |
| name     | cookie的名称             | 必要属性 |
| value    | cookie的值（不能是中文） | 必要属性 |
| path     | cookie的路径             | 重要     |
| domain   | cookie的域名             | 重要     |
| maxAge   | cookie的生存时间。       | 重要     |
| version  | cookie的版本号。         | 不重要   |
| comment  | cookie的说明。           | 不重要   |

## 2.2 常用方法

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/1.png)

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/2.png)

## 2.3 使用案例

### 2.3.1 需求说明

通过Cookie记录最后的访问时间，并在浏览器上显示出来

### 2.3.2 案例目的

掌握Cookie的基本使用，从创建到添加客户端，再到从服务器端获取

### 2.3.3 实现步骤

1. 通过响应对象写出一个提示信息
2. 创建Cookie对象，指定name和value
3. 设置Cookie最大存活时间
4. 通过响应对象将Cookie对象添加到客户端
5. 通过请求对象获取Cookie对象
6. 将Cookie对象中的访问时间写出

### 2.3.4 示例代码

```java
@WebServlet("/servletDemo01")
public class ServletDemo01 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.通过响应对象写出提示信息
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter pw = resp.getWriter();
        pw.write("欢迎访问本网站，您的最后访问时间为：<br>");

        //2.创建Cookie对象，用于记录最后访问时间
        Cookie cookie = new Cookie("time",System.currentTimeMillis()+"");

        //3.设置最大存活时间
        //cookie.setMaxAge(3600);
        cookie.setMaxAge(0);    // 立即清除

        //4.将cookie对象添加到客户端
        resp.addCookie(cookie);

        //5.获取cookie
        Cookie[] arr = req.getCookies();
        for(Cookie c : arr) {
            if("time".equals(c.getName())) {
                //6.获取cookie对象中的value，进行写出
                String value = c.getValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                pw.write(sdf.format(new Date(Long.parseLong(value))));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

可以从浏览器的检查，Network中查看设置的Cookie

### 2.3.5 注意事项

数量限制：

Cookie有大小，个数限制。每个网站最多只能存20个cookie，且大小不能超过4kb。同时，所有网站的cookie总数不超过300个。

名称限制：

Cookie的名称只能包含ASCCI码表中的字母、数字字符。不能包含逗号、分号、空格，不能以$开头。Cookie的值不支持中文。

时间设置：

当删除Cookie时，设置maxAge值为0。当不设置maxAge时，使用的是浏览器的内存，当关闭浏览器之后，cookie将丢失。设置了此值，就会保存成缓存文件（值必须是大于0的,以秒为单位）。

路径限制：

取自第一次访问的资源路径前缀，只要以这个前缀开头（包括子级路径），能获取到。或者用setPath()方法设置指定路径。

示例代码：

```java
@WebServlet("/servlet/servletDemo02")
public class ServletDemo02 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建Cookie并添加
        Cookie cookie = new Cookie("username","zhangsan");
        cookie.setMaxAge(3600);
        resp.addCookie(cookie);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

```java
@WebServlet("/servlet/servletDemo03")
public class ServletDemo03 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取Cookie
        Cookie[] arr = req.getCookies();
        for(Cookie c : arr) {
            if("username".equals(c.getName())) {
                String value = c.getValue();
                resp.getWriter().write(value);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

```java
@WebServlet("/servlet/aaa/servletDemo04")
……
```

```java
@WebServlet("/bbb/servletDemo05")
……
```

# 3. Session

## 3.1 概述

HttpSession接口用于提供一种通过多个页面请求或访问网站来标识用户并存储有关该用户的信息的方法。简单说它就是一个服务端会话对象，用于存储用户的会话数据。

同时，它也是Servlet规范中四大域对象之一的会话域对象。并且它也是用于实现数据共享的。但它与我们之前讲解的应用域和请求域是有区别的。

| 域对象         | 作用范围     | 使用场景                                                     |
| -------------- | ------------ | ------------------------------------------------------------ |
| ServletContext | 整个应用范围 | 当前项目中需要数据共享时，可以使用此域对象。                 |
| ServletRequest | 当前请求范围 | 在请求或者当前请求转发时需要数据共享可以使用此域对象。       |
| HttpSession    | 会话返回     | 在当前会话范围中实现数据共享。它可以在多次请求中实现数据共享。 |

## 3.2 HttpSession常用方法

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/3.png)

## 3.3 HttpSession的获取

HttpSession是通过HttpServletRequest对象来获取的

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/4.png)

情况1：带JSESSIONID，且服务器中已有

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/5.png)

情况2：带JSESSIONID，但服务器中没有

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/6.png)

情况3：不带JSESSIONID

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/7.png)

getSession()默认为true，在服务器中没找到对象会自动创建

## 3.4 使用案例

### 3.4.1 需求说明

通过第一个Servlet设置共享数据用户名，并在第二个Servlet获取

### 3.4.2 案例目的

掌握HttpSession的基本使用

### 3.4.3 实现步骤

1. 在第一个Servlet中获取请求的用户名
2. 获取HttpSession对象
3. 将用户名设置到共享数据中
4. 在第二个Servlet中获取HttpSession对象
5. 获取共享数据用户名
6. 将获取到的用户名响应给客户端浏览器

### 3.4.4 示例代码

```java
@WebServlet("/servletDemo01")
public class ServletDemo01 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取请求的用户名
        String username = req.getParameter("username");

        //2.获取HttpSession的对象
        HttpSession session = req.getSession();
        System.out.println(session);
        System.out.println(session.getId());

        //3.将用户名信息添加到共享数据中
        session.setAttribute("username",username);

        //实现url重写  相当于在地址栏后面拼接了一个jsessionid
        resp.getWriter().write("<a href='"+resp.encodeURL("http://localhost:8080/session/servletDemo03")+"'>go servletDemo03</a>");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

```java
@WebServlet("/servletDemo02")
public class ServletDemo02 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取HttpSession对象
        HttpSession session = req.getSession();
        System.out.println(session);
        System.out.println(session.getId());

        //2.获取共享数据
        Object username = session.getAttribute("username");

        //3.将数据响应给浏览器
        resp.getWriter().write(username+"");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

### 3.4.5 注意事项

如果禁用了Cookie，那么Session的ID会不同

示例代码：

```java
@WebServlet("/servletDemo01")
public class ServletDemo01 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取请求的用户名
        String username = req.getParameter("username");

        //2.获取HttpSession的对象
        HttpSession session = req.getSession();
        System.out.println(session);
        System.out.println(session.getId());

        //3.将用户名信息添加到共享数据中
        session.setAttribute("username",username);

        //实现url重写  相当于在地址栏后面拼接了一个jsessionid
        resp.getWriter().write("<a href='"+resp.encodeURL("http://localhost:8080/session/servletDemo03")+"'>go servletDemo03</a>");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

```java
@WebServlet("/servletDemo03")
public class ServletDemo03 extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取HttpSession对象
        HttpSession session = req.getSession(false);
        System.out.println(session);
        if(session == null) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("为了不影响正常的使用，请不要禁用浏览器的Cookie~");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

**HttpSession的钝化和活化：**

**什么是持久态**

把长时间不用，但还不到过期时间的HttpSession进行序列化，写到磁盘上。我们把HttpSession持久态也叫做钝化。（与钝化相反的，我们叫活化。）

**什么时候使用持久化**

第一种情况：当访问量很大时，服务器会根据getLastAccessTime来进行排序，对长时间不用，但是还没到过期时间的HttpSession进行持久化。

第二种情况：当服务器进行重启的时候，为了保持客户HttpSession中的数据，也要对HttpSession进行持久化

**注意**

HttpSession的持久化由服务器来负责管理，我们不用关心。只有实现了序列化接口的类才能被序列化，否则不行。

# 4. JSP

## 4.1 简介

JSP全称是Java Server Page，它和Servlet一样，也是sun公司推出的一套开发动态web资源的技术，称为JSP/Servlet规范。JSP是基于Java语言的，<font color='red'>JSP的本质其实就是一个Servlet</font>。

JSP部署在服务器上，可以处理客户端发送的请求，<font color='red'>并根据请求内容动态生成HTML、XML或者其他格式文档的Web网页</font>，然后再响应给客户端。

| 类别       | 适用场景                                                     |
| ---------- | :----------------------------------------------------------- |
| HTML       | 只能开发静态资源，不能包含java代码，无法添加动态数据。       |
| CSS        | 美化页面                                                     |
| JavaScript | 给网页添加一些动态效果                                       |
| Servlet    | 写java代码，可以输出页面内容，但是很不方便，开发效率极低。   |
| JSP        | 它包括了HTML的展示技术，同时具备Servlet输出动态资源的能力。但是不适合作为控制器来用。包含了显示页面的技术，也有Java代码功能 |

## 4.2 简单案例

1. 创建一个JavaWeb工程

2. 在web目录下创建一个index.jsp文件

3. 填写jsp文件内容

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   
     <head>
       <title>JSP的入门</title>
     </head>
   
     <body>
         这是第一个JSP页面
     </body>
   </html>
   ```

4. 启动tomcat，测试

## 4.3 JSP执行过程

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/8.png)

JSP生成Java文件：

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/9.png)

Java文件的代码说明JSP就是一个HttpServlet。同时，我们在index_jsp.java文件中找到了输出页面的代码，并且在浏览器端查看源文件，看到的内容是一样的。这也就是说明，我们的浏览器上的内容，在通过jsp展示时，本质都是用out.write()输出出来的。

讲到这里，我们应该清楚的认识到，JSP它是一个特殊的Servlet，主要是用于展示动态数据。它展示的方式是用流把数据输出出来，而我们在使用JSP时，涉及HTML的部分，都与HTML的用法一致，这部分称为jsp中的模板元素，在开发过程中，先写好这些模板元素，因为它们决定了页面的外观。

## 4.4 JSP语法

### 4.4.1 注释

形式为：<font color='red'><b><%--注释--%></b></font>

### 4.4.2 Java代码

形式为：<font color='red'><b><% 此处写java代码 %></b></font>

**示例：**

```jsp
<!--Java代码块-->
<% out.println("这是Java代码块");%>
<hr/>
```

### 4.4.3 表达式

形式为：<font color='red'><b><%=表达式%></b></font>

**示例：**

```jsp
<!--JSP表达式-->
<%="这是JSP表达式"%><br/>
就相当于<br/>
<%out.println("这是没有JSP表达式输出的");%>
```

### 4.4.4 申明

在JSP中可以声明一些变量，方法，静态方法，形式为：<font color='red'><b><%! 声明的内容 %></b></font>

**示例：**

```jsp
<!--JSP声明-->
<%! String str = "声明语法格式";%>
<%=str%>
```

## 4.5 JSP指令

### 4.5.1 page指令

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/10.png)

### 4.5.2 include指令

语法格式：<%@include file="" %>该指令是包含外部页面。 

属性：file，以/开头，就代表当前应用。

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/11.png)

### 4.5.3 taglib指令

语法格式：<%@taglib uri="" prefix=""%>

作用：该指令用于引入外部标签库。html标签和jsp标签不用引入。

属性：                                                                                   

​       uri：外部标签的URI地址。

​       prefix：使用标签时的前缀。

## 4.6 JSP使用细节

### 4.6.1 九大隐式对象

隐式对象指的是在jsp中，可以不声明就直接使用的对象。它只存在于jsp中，因为java类中的变量必须要先声明再使用。其实jsp中的隐式对象也并非是未声明，只是它是在翻译成.java文件时声明的。所以我们在jsp中可以直接使用。

| 隐式对象名称 | 类型                                   | 备注                          |
| ------------ | -------------------------------------- | ----------------------------- |
| request      | javax.servlet.http.HttpServletRequest  |                               |
| response     | javax.servlet.http.HttpServletResponse |                               |
| session      | javax.servlet.http.HttpSession         | Page指令可以控制开关          |
| application  | javax.servlet.ServletContext           |                               |
| page         | Java.lang.Object                       | 当前jsp对应的servlet引用实例  |
| config       | javax.servlet.ServletConfig            |                               |
| exception    | java.lang.Throwable                    | page指令有开关                |
| out          | javax.servlet.jsp.JspWriter            | 字符输出流，相当于printwriter |
| pageContext  | javax.servlet.jsp.PageContext          | 很重要                        |

### 4.6.2 pageContext对象

pageContext是JSP独有的对象，Servlet中没有这个对象。本身也是一个域（作用范围）对象（页面域），但是它可以操作其他3个域对象中的属性。而且还可以获取其他8个隐式对象。

PageContext是一个局部变量，所以它的生命周期随着JSP的创建而诞生，随着JSP的结束而消失。每个JSP页面都有一个独立的PageContext。

### 4.6.3 四大域对象（重要）

| 域对象名称     | 范围     | 级别                     | 备注                                                         |
| -------------- | -------- | ------------------------ | ------------------------------------------------------------ |
| PageContext    | 页面范围 | 最小，只能在当前页面用   | 因范围太小，开发中用的很少                                   |
| ServletRequest | 请求范围 | 一次请求或当期请求转发用 | 当请求转发之后，<font color='red'>再次转发时请求域丢失</font> |
| HttpSession    | 会话范围 | 多次请求数据共享时使用   | 多次请求共享数据，<font color='red'>但不同的客户端不能共享</font> |
| ServletContext | 应用范围 | 最大，整个应用都可以使用 | 尽量少用，<font color='red'>如果对数据有修改需要做同步处理</font> |

## 4.7 MVC模型简介

M：model，通常用于封装数据，封装的是数据模型。

V：view，通常用于展示数据。动态展示用jsp页面，静态数据展示用html。

C：controller，通常用于处理请求和响应。目前指的是Servlet，以后还会有Spring，SpringMVC。

![12](C:\Users\HASEE\Desktop\pics\12.png)**Servlet：**擅长处理业务逻辑，不擅长输出显示界面。在web开发中多用于控制程序逻辑（流程）。所以我们称之为：控制器。

**JSP：**擅长显示界面，不擅长处理程序逻辑。在web开发中多用于展示动态界面。所以我们称之为：视图。

分层的模型可以降低耦合性。

# 5. 综合案例-学生管理系统

## 5.1 案例效果

![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/13.png)

![14](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.05/pics/14.png)

## 5.2 实现步骤

```java
public class Student {
    private String username;
    private int age;
    private int score;

    public Student() {
    }

    public Student(String username, int age, int score) {
        this.username = username;
        this.age = age;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
```

### 5.2.1 登录

1. 创建一个web项目

2. 在web目录下创建一个index.jsp

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
     <% Object username = session.getAttribute("username");
       if(username == null) {
     %>
     <a href="/stu/login.jsp">请登录</a>
     <%} else {%>
     <a href="/stu/addStudent.jsp">添加学生</a>
     <a href="/stu/listStudentServlet">查看学生</a>
     <%}%>
     </body>
   </html>
   ```

3. 在页面中获取会话域中的用户名，获取到了就显示添加和查看功能的超链接，没获取到就显示登陆功能的超链接

4. 在web目录下创建一个login.jsp，实现登陆页面

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>学生登录</title>
   </head>
   <body>
       <form action="/stu/loginStudentServlet" method="get" autocomplete="off">
       姓名：<input type="text" name="username"> <br>
       密码：<input type="password" name="password"> <br>
       <button type="submit">登录</button>
   </form>
   </body>
   </html>
   ```

5. 创建LoginServlet，获取用户名和密码

   ```java
   /*
       学生登录
    */
   @WebServlet("/loginStudentServlet")
   public class loginStudentServlet extends HttpServlet {
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           //1.获取用户名和密码
           String username = req.getParameter("username");
           String password = req.getParameter("password");
   
           //2.判断用户名
           if(username == null || "".equals(username)) {
               //2.1用户名为空 重定向到登录页面
               resp.sendRedirect("/stu/login.jsp");
               return;
           }
   
           //2.2用户名不为空 将用户名存入会话域中
           req.getSession().setAttribute("username",username);
   
           //3.重定向到首页index.jsp
           resp.sendRedirect("/stu/index.jsp");
       }
   
       @Override
       protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           doGet(req,resp);
       }
   }
   ```

6. 如果用户名为空，则重新定向到登陆页面

7. 如果不为空，将用户名添加到会话域中，再重定向到首页

### 5.2.2 添加

1. 在web目录下创建一个addStudent.jsp，实现添加学生的表单项

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>添加学生</title>
   </head>
   <body>
       <form action="/stu/addStudentServlet" method="get" autocomplete="off">
       学生姓名：<input type="text" name="username"> <br>
       学生年龄：<input type="number" name="age"> <br>
       学生成绩：<input type="number" name="score"> <br>
       <button type="submit">保存</button>
   </form>
   </body>
   </html>
   ```

2. 创建AddStudentServlet，获取学生信息并保存到文件中

   ```java
   /*
       实现添加功能
    */
   @WebServlet("/addStudentServlet")
   public class AddStudentServlet extends HttpServlet{
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           //1.获取表单中的数据
           String username = req.getParameter("username");
           String age = req.getParameter("age");
           String score = req.getParameter("score");
   
           //2.创建学生对象并赋值
           Student stu = new Student();
           stu.setUsername(username);
           stu.setAge(Integer.parseInt(age));
           stu.setScore(Integer.parseInt(score));
   
           //3.将学生对象的数据保存到d:\\stu.txt文件中
           BufferedWriter bw = new BufferedWriter(new FileWriter("f:\\stu.txt",true));
           bw.write(stu.getUsername() + "," + stu.getAge() + "," + stu.getScore());
           bw.newLine();
           bw.close();
   
           //4.通过定时刷新功能响应给浏览器
           resp.setContentType("text/html;charset=UTF-8");
           resp.getWriter().write("添加成功。2秒后自动跳转到首页...");
           resp.setHeader("Refresh","2;URL=/stu/index.jsp");
       }
   
       @Override
       protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           doGet(req,resp);
       }
   }
   ```

3. 通过定时刷新功能2秒后跳转到首页

### 5.2.3 查看

1. 创建ListStudentServlet，读取文件中的学生信息到集合中。

   ```java
   /*
       实现查看功能
    */
   @WebServlet("/listStudentServlet")
   public class ListStudentServlet extends HttpServlet {
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           //1.创建字符输入流对象，关联读取的文件
           BufferedReader br = new BufferedReader(new FileReader("f:\\stu.txt"));
   
           //2.创建集合对象，用于保存Student对象
           ArrayList<Student> list = new ArrayList<>();
   
           //3.循环读取文件中的数据，将数据封装到Student对象中。再把多个学生对象添加到集合中
           String line;
           while((line = br.readLine()) != null) {
               //张三,23,95
               Student stu = new Student();
               String[] arr = line.split(",");
               stu.setUsername(arr[0]);
               stu.setAge(Integer.parseInt(arr[1]));
               stu.setScore(Integer.parseInt(arr[2]));
               list.add(stu);
           }
   
           //4.将集合对象存入会话域中
           req.getSession().setAttribute("students",list);
   
           //5.重定向到学生列表页面
           resp.sendRedirect("/stu/listStudent.jsp");
   
       }
   
       @Override
       protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           doGet(req,resp);
       }
   }
   ```

2. 将集合添加到会话域中。

3. 重定向到listStudent.jsp页面上

4. 在web目录下创建一个listStudent.jsp

   ```jsp
   <%@ page import="cn.edu.hdu.bean.Student" %>
   <%@ page import="java.util.ArrayList" %>
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
       <title>Title</title>
   </head>
   <body>
       <table width="600px" border="1px">
       <tr>
           <th>学生姓名</th>
           <th>学生年龄</th>
           <th>学生成绩</th>
       </tr>
       <% ArrayList<Student> students = (ArrayList<Student>) session.getAttribute("students");
           for(Student stu : students) {
       %>
       <tr align="center">
           <td><%=stu.getUsername()%></td>
           <td><%=stu.getAge()%></td>
           <td><%=stu.getScore()%></td>
       </tr>
       <%}%>
   </table>
   </body>
   </html>
   ```

5. 定义表格标签，在表格中获取会话域的集合数据，将数据显示在页面上

