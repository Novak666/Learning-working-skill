# 1. 请求对象

## 1.1 请求概述

在B/S架构中，就是客户浏览器向服务器发出询问。在我们的JavaEE工程中，客户浏览器发出询问，要遵循HTTP协议所规定的。

请求对象，就是在JavaEE工程中，用于发送请求的对象。我们常用的对象就是ServletRequest和HttpServletRequest，它们的区别就是是否和HTTP协议有关。

## 1.2 获取路径的方法

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/1.png)

```java
/**
 * 请求对象的各种信息获取
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public class RequestDemo1 extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //本机地址：服务器地址
        String localAddr = request.getLocalAddr();
        //本机名称：服务器名称
        String localName = request.getLocalName();
        //本机端口：服务器端口
        int localPort = request.getLocalPort();
        //来访者ip
        String remoteAddr = request.getRemoteAddr();
        //来访者主机
        String remoteHost = request.getRemoteHost();
        //来访者端口
        int remotePort = request.getRemotePort();
        //统一资源标识符
        String URI = request.getRequestURI();
        //统一资源定位符
        String URL = request.getRequestURL().toString();
        //获取查询字符串
        String queryString = request.getQueryString();
        //获取Servlet映射路径
        String servletPath = request.getServletPath();

        //输出内容
		System.out.println("getLocalAddr() is :"+localAddr);
		System.out.println("getLocalName() is :"+localName);
		System.out.println("getLocalPort() is :"+localPort);
		System.out.println("getRemoteAddr() is :"+remoteAddr);
		System.out.println("getRemoteHost() is :"+remoteHost);
		System.out.println("getRemotePort() is :"+remotePort);
		System.out.println("getRequestURI() is :"+URI);
		System.out.println("getRequestURL() is :"+URL);
        System.out.println("getQueryString() is :"+queryString);
        System.out.println("getServletPath() is :"+servletPath);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
```

## 1.3 获取请求头信息的方法

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/2.png)

请求头可以在浏览器的检查中Network下查看

示例代码：

```java
/**
 * 获取请求消息头
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public class RequestDemo2 extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //1.根据名称获取头的值	一个消息头一个值
        String value = request.getHeader("Accept-Encoding");
        System.out.println("getHeader():"+value);

        //2.根据名称获取头的值	一个头多个值
        Enumeration<String> values = request.getHeaders("Accept");
        while(values.hasMoreElements()){
            System.out.println("getHeaders():"+values.nextElement());
        }

        //3.获取请求消息头的名称的枚举
        Enumeration<String> names = request.getHeaderNames();
        while(names.hasMoreElements()){
            String name = names.nextElement();
            String value1 = request.getHeader(name);
            System.out.println(name+":"+value1);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
```

## 1.4 获取请求参数

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/3.png)

首先在web页面下准备一个html页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册页面</title>
</head>
<body>
    <form action="/request/servletDemo08" method="post" autocomplete="off">
        姓名：<input type="text" name="username"> <br>
        密码：<input type="password" name="password"> <br>
        爱好：<input type="checkbox" name="hobby" value="study">学习
              <input type="checkbox" name="hobby" value="game">游戏 <br>
        <button type="submit">注册</button>
    </form>
</body>
</html>
```

示例代码：

```java
@WebServlet("/servletDemo03")
public class ServletDemo03 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.根据名称获取数据   getParameter()
        String username = req.getParameter("username");
        System.out.println(username);
        String password = req.getParameter("password");
        System.out.println(password);
        System.out.println("--------------------");

        //2.根据名称获取所有数据 getParameterValues()
        String[] hobbies = req.getParameterValues("hobby");
        for(String hobby : hobbies) {
            System.out.println(hobby);
        }
        System.out.println("--------------------");

        //3.获取所有名称  getParameterNames()
        Enumeration<String> names = req.getParameterNames();
        while(names.hasMoreElements()) {
            String name = names.nextElement();
            System.out.println(name);
        }
        System.out.println("--------------------");

        //4.获取所有参数的键值对 getParameterMap()
        Map<String, String[]> map = req.getParameterMap();
        for(String key : map.keySet()) {
            String[] values = map.get(key);
            System.out.print(key + ":");
            for(String value : values) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

## 1.5 封装请求参数

### 1.5.1 手动封装

自定义一个对象，成员变量包括username，password和hobby

示例代码：

```java
@WebServlet("/servletDemo04")
public class ServletDemo04 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取所有的数据
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String[] hobbies = req.getParameterValues("hobby");

        //2.封装学生对象
        Student stu = new Student(username,password,hobbies);

        //3.输出对象
        System.out.println(stu);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

### 1.5.2 反射封装<font color='red'>（有点难）</font>

示例代码：

```java
@WebServlet("/servletDemo05")
public class ServletDemo05 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取所有的数据
        Map<String, String[]> map = req.getParameterMap();

        //2.封装学生对象
        Student stu = new Student();
        //2.1遍历集合
        for(String name : map.keySet()) {
            String[] value = map.get(name);
            try {
                //2.2获取Student对象的属性描述器
                PropertyDescriptor pd = new PropertyDescriptor(name,stu.getClass());
                //2.3获取对应的setXxx方法
                Method writeMethod = pd.getWriteMethod();
                //2.4执行方法
                if(value.length > 1) {
                    writeMethod.invoke(stu,(Object)value);
                }else {
                    writeMethod.invoke(stu,value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //3.输出对象
        System.out.println(stu);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

### 1.5.3 工具类封装

在WEB-INF下新建一个libs目录，放入工具类Jar包commons-beanutils-1.9.4.jar和commons-logging-1.2.jar

示例代码：

```java
@WebServlet("/servletDemo06")
public class ServletDemo06 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取所有的数据
        Map<String, String[]> map = req.getParameterMap();

        //2.封装学生对象
        Student stu = new Student();
        try {
            BeanUtils.populate(stu,map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //3.输出对象
        System.out.println(stu);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

## 1.6 流对象获取请求参数

示例代码：

```java
@WebServlet("/servletDemo07")
public class ServletDemo07 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //字符流(必须是post方式)
        /*BufferedReader br = req.getReader();
        String line;
        while((line = br.readLine()) != null) {
            System.out.println(line);
        }*/
        //br.close();

        //字节流
        ServletInputStream is = req.getInputStream();
        byte[] arr = new byte[1024];
        int len;
        while((len = is.read(arr)) != -1) {
            System.out.println(new String(arr,0,len));
        }
        //is.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

## 1.7 请求出现乱码

**1. GET方式请求**

tomcat8.5以后已经解决乱码问题

**2. POST方式请求**

示例代码：

```java
@WebServlet("/servletDemo08")
public class ServletDemo08 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码格式
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        System.out.println(username);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

## 1.8 请求域

可以在一次请求范围内进行数据共享，一般用于请求转发的多个资源间的数据共享

如下图所示，假设ServletA处理不了来自客户端的请求，那么就可以将请求转发到ServletB。相应的设置数据共享的方法也在图中。

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/4.png)

## 1.9 请求转发

特点：

1. 浏览器地址栏不变
2. 域对象中的数据不丢失
3. 负责转发的Servlet转发前后的响应正文会丢失
4. 由转发的目的地来响应客户端

方法：

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/5.png)

示例代码：

```java
@WebServlet("/servletDemo09")
public class ServletDemo09 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置共享数据
        req.setAttribute("encoding","gbk");

        //获取请求调度对象
        RequestDispatcher rd = req.getRequestDispatcher("/servletDemo10");
        //实现转发功能
        rd.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

```java
@WebServlet("/servletDemo10")
public class ServletDemo10 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取共享数据
        Object encoding = req.getAttribute("encoding");
        System.out.println(encoding);

        System.out.println("servletDemo10执行了...");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

## 1.10 请求包含

可以合并其他Servlet的功能，一起响应给客户端

特点：

1. 浏览器地址栏不变
2. 域对象中的数据不丢失
3. 被包含的Servlet响应头会丢失

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/6.png)

方法：

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/7.png)

示例代码：

```java
@WebServlet("/servletDemo11")
public class ServletDemo11 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servletDemo11执行了...");

        //获取请求调度对象
        RequestDispatcher rd = req.getRequestDispatcher("/servletDemo12");
        //实现包含功能
        rd.include(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

```java
@WebServlet("/servletDemo12")
public class ServletDemo12 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servletDemo12执行了...");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

# 2. 响应对象

## 2.1 响应概述

响应，它表示了服务器端收到请求，同时也已经处理完成，把处理的结果告知用户。简单来说，指的就是服务器把请求的处理结果告知客户端。在B/S架构中，响应就是把结果带回浏览器。

响应对象，顾名思义就是用于在JavaWeb工程中实现上述功能的对象。响应对象也是是Servlet规范中定义的，它包括了协议无关的和协议相关的。

协议无关的对象标准是：ServletResponse接口

协议相关的对象标准是：HttpServletResponse接口

## 2.2 常见响应状态码

常用状态码：

| 状态码 |                            说明                            |
| :----: | :--------------------------------------------------------: |
|  200   |                          执行成功                          |
|  302   | 它和307一样，都是用于重定向的状态码。只是307目前已不再使用 |
|  304   |                 请求资源未改变，使用缓存。                 |
|  400   |            请求错误。最常见的就是请求参数有问题            |
|  404   |                       请求资源未找到                       |
|  405   |                      请求方式不被支持                      |
|  500   |                     服务器运行内部错误                     |

状态码首位含义：

| 状态码 |    说明    |
| :----: | :--------: |
|  1xx   |    消息    |
|  2xx   |    成功    |
|  3xx   |   重定向   |
|  4xx   | 客户端错误 |
|  5xx   | 服务器错误 |

## 2.3 字节流响应消息和解决乱码

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/8.png)

示例代码：

```java
public class ResponseDemo1 extends HttpServlet {

    /**
     * 演示字节流输出的乱码问题
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String str = "字节流输出中文的乱码问题";//UTF-8的字符集，此时浏览器显示也需要使用UTF-8的字符集。
        //1.拿到字节流输出对象
        ServletOutputStream sos = response.getOutputStream();
        
        response.setContentType("text/html;charset=UTF-8");
        //2.把str转换成字节数组之后输出到浏览器
        sos.write(str.getBytes("UTF-8")); 
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
```

## 2.4 字符流响应消息和解决乱码

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/9.png)

示例代码：

```java
public class ResponseDemo2 extends HttpServlet {

    /**
     * 字符流输出中文乱码
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String str = "字符流输出中文乱码";
        //response.setCharacterEncoding("UTF-8");

        //设置响应正文的MIME类型和字符集
        response.setContentType("text/html;charset=UTF-8");
        //1.获取字符输出流
        PrintWriter out = response.getWriter();

        out.write(str);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
```

## 2.5 响应图片

本节讨论如何向浏览器响应图片数据

示例代码：

```java
@WebServlet("/servletDemo03")
public class ServletDemo03 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //通过文件的相对路径来获取文件的绝对路径，因为tomcat发布后是war包形式，所以路径会变
        String realPath = getServletContext().getRealPath("/img/hm.png");
        System.out.println(realPath);
        //1.创建字节输入流对象，关联图片路径
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(realPath));

        //2.通过响应对象获取字节输出流对象
        ServletOutputStream sos = resp.getOutputStream();

        //3.循环读写
        byte[] arr = new byte[1024];
        int len;
        while((len = bis.read(arr)) != -1) {
            sos.write(arr,0,len);
        }

        bis.close();
        sos.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

## 2.6 设置缓存时间

缓存：对于不经常变化的数据，我们可以设置合理的缓存时间，避免浏览器频繁请求服务器，提高效率

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/10.png)

示例代码：

```java
@WebServlet("/servletDemo04")
public class ServletDemo04 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String news = "这是一条很火爆的新闻~~";

        //设置缓存时间
        resp.setDateHeader("Expires",(System.currentTimeMillis()+1*60*60*1000L));

        //设置编码格式
        resp.setContentType("text/html;charset=UTF-8");
        //写出数据
        resp.getWriter().write(news);
        System.out.println("aaa");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

## 2.7 设置定时刷新

定时刷新：过了指定时间后，页面自动进行跳转

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/11.png)

示例代码：

```java
@WebServlet("/servletDemo05")
public class ServletDemo05 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String news = "您的用户名或密码错误，3秒后自动跳转到登录页面...";

        //设置编码格式
        resp.setContentType("text/html;charset=UTF-8");
        //写出数据
        resp.getWriter().write(news);

        //设置响应消息头定时刷新
        resp.setHeader("Refresh","3;URL=/response/login.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

## 2.8 请求重定向

请求重定向：客户端的一次请求到达后，发现需要借助其他Servlet来实现功能

特点：浏览器的地址栏会发生改变，两次请求的请求域对象中不能共享数据，可以重定向到其他服务器<font color='red'>（区别于请求转发）</font>

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/12.png)

示例代码：

```java
@WebServlet("/servletDemo06")
public class ServletDemo06 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置请求域数据
        req.setAttribute("username","zhangsan");

        //设置重定向
        resp.sendRedirect(req.getContextPath() + "/servletDemo07");
      
      // resp.sendRedirect("https://www.baidu.com");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

```java
@WebServlet("/servletDemo07")
public class ServletDemo07 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servletDemo07执行了...");
        Object username = req.getAttribute("username");
        System.out.println(username);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.02.03/pics/13.png)

## 2.9 文件下载

步骤：

1. 创建字节输入流，关联读取的文件
2. <font color='red'>设置响应消息头支持的类型</font>
3. <font color='red'>设置响应消息头以下载的方式打开资源</font>
4. 通过响应对象获取字节输出流对象
5. 循环读写
6. 释放资源

```java
@WebServlet("/servletDemo08")
public class ServletDemo08 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.创建字节输入流，关联读取的文件
        //获取文件的绝对路径
        String realPath = getServletContext().getRealPath("/img/hm.png");
        //创建字节输出流对象
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(realPath));

        //2.设置响应头支持的类型  应用支持的类型为字节流
        /*
            Content-Type 消息头名称   支持的类型
            application/octet-stream   消息头参数  应用类型为字节流
         */
        resp.setHeader("Content-Type","application/octet-stream");

        //3.设置响应头以下载方式打开  以附件形式处理内容
        /*
            Content-Disposition  消息头名称  处理的形式
            attachment;filename=  消息头参数  附件形式进行处理
         */
        resp.setHeader("Content-Disposition","attachment;filename=" + System.currentTimeMillis() + ".png");

        //4.获取字节输出流对象
        ServletOutputStream sos = resp.getOutputStream();

        //5.循环读写文件
        byte[] arr = new byte[1024];
        int len;
        while((len = bis.read(arr)) != -1) {
            sos.write(arr,0,len);
        }

        //6.释放资源
        bis.close();
        sos.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
```

## 2.10 注意事项

**第一： response得到的字符流和字节流互斥，只能选其一**

**第二：response获取的流不用关闭，由服务器关闭即可**

# 3. 案例中的使用

## 3.1 案例需求介绍

在昨天的课程中，我们实现了浏览器发送请求，由Servlet来接收。今天，我们继续对学生管理系统进行升级，通过Servlet来实现学生的新增，删除，修改，查询操作。

新增：Create

查询：Retrieve

修改：Update

删除：Delete

每个单词取第一个字母，组成了CRUD。所以，同学们今后看到CRUD操作，指的就是增删改查。

今天案例的CRUD，我们只关注Servlet接收请求和处理响应，不用过多的去关注真正增删改查操作（因为，我们目前还是把学生信息写到文件中，等web5天课程结束，我们会讲解数据库，它是我们保存数据这类问题的终极解决方案，而保存文件只是个替代品，我们没必要在替代品处消耗太多精力）。