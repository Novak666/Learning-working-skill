# 1. Ajax的概述

## 1.1 什么是AJAX

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.10/pics/1.png)

​	说白了:   AJax是可以做异步的请求,实现局部刷新一种客户端技术 

## 1.2 什么是异步

- 同步  


![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.10/pics/2.png)

- 异步

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.10/pics/3.png)

## 1.3 为什么要学习AJAX   

​	提升用户的体验。(异步)

​	实现页面局部刷新。

​	将部分的代码，写到客户端浏览器。

小结

1. 什么是Ajax: 可以用来实现异步请求, 进行局部刷新客户端技术
2. 为什么要学习Ajax?
   +  提升用户的体验。(异步)
   + 实现页面局部刷新。
   + 将部分的代码，写到客户端浏览器。

# 2. JS的Ajax入门(了解)

步骤

​	第一步：创建异步请求对象。

​	第二步：打开连接。

​	第三步：发送请求。

​	第四步：设置监听对象改变所触发的函数,处理结果

## 2.1 GET请求方式的入门

```javascript
//使用原生的js代码发送异步请求
function fn1() {
    //1. 创建XMLHTTPRequest对象
    var xhr = new XMLHttpRequest();
    //2. 建立与服务器的连接
    xhr.open("GET","demo01?username=jay")
    //3. 发送请求
    xhr.send()

    //4.自己编写代码处理响应，展示响应数据
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) {
            //获取响应数据
            var responseText = xhr.responseText;
            document.getElementById("msg").innerHTML = responseText
        }
    }
}
```

## 2.2 POST请求方式的入门

```javascript
//使用js原生的代码发送异步的post请求
function fn2() {
    //1. 创建XMLHTTPRequest对象
    var xhr = new XMLHttpRequest();
    //2. 建立与服务器的连接
    xhr.open("POST","demo01")
    //3. 发送请求
    //设置一个请求头，用来设置请求参数的类型
    xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded")
    xhr.send("username=jay")
    //4.自己编写代码处理响应，展示响应数据
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) {
            //获取响应数据
            var responseText = xhr.responseText;
            document.getElementById("msg").innerHTML = responseText
        }
    }
}
```

# 3. JQ的Ajax简介

## 3.1 为什么要使用JQuery的AJAX  

​	因为传统(js里面)的AJAX的开发中，AJAX有两个主要的问题：

​	浏览器的兼容的问题 , 编写AJAX的代码太麻烦而且很多都是雷同的。

​	在实际的开发通常使用JQuery的Ajax  ,或者Vue里面的axios

## 3.2 JQuery的Ajax的API

| 请求方式     | 语法                                          |
| ------------ | --------------------------------------------- |
| **GET请求**  | $.get(url, *[data]*, *[callback]*, *[type]*)  |
| **POST请求** | $.post(url, *[data]*, *[callback]*, *[type]*) |
| **AJAX请求** | $.ajax([settings])                            |

# 4. JQ的Ajax入门(重点)

需求：

在网页上点击按钮, 发送Ajax请求服务器,响应hello ajax...分别使用get,post,ajax三种方法

## 4.1 get()

- get方式，语法`$.get(url, [data], [callback], [type]);`

| 参数名称 | 解释                                                         |
| -------- | ------------------------------------------------------------ |
| url      | 请求的服务器端url地址                                        |
| data     | 发送给服务器端的请求参数，格式是key=value；get请求的参数可以直接写在url后面 |
| callback | 当请求成功后的回掉函数，可以在函数体中编写我们的逻辑代码     |
| type     | 预期的返回数据的类型(默认为text)，取值可以是 xml, html, script, json, text, _defaul等 |

- 实例

```js
//声明一个方法使用jQuery的ajax发送异步的get请求
function fn3() {
    $.get("/demo01?username=tom",function (result) {
        //在这个回调函数中处理响应数据result
        //将响应数据展示在id为msg的div中
        $("#msg").html(result)
    })
}
```

## 4.2 post()

- post方式，语法`$.post(url, [data], [callback], [type])`

| 参数名称 | 解释                                                         |
| -------- | ------------------------------------------------------------ |
| url      | 请求的服务器端url地址                                        |
| data     | 发送给服务器端的请求参数，格式是key=value                    |
| callback | 当请求成功后的回掉函数，可以在函数体中编写我们的逻辑代码     |
| type     | 预期的返回数据的类型(默认为text)，取值可以是 xml, html, script, json, text, _defaul等 |

- 实例

```js
//声明一个方法使用jQuery的ajax发送异步的post请求
function fn4() {
    $.post("/demo01","username=jack",function (result) {
        //result就是服务器端的响应数据
        $("#msg").html(result)
    })
}
```

## 4.3 ajax()了解

- 语法`$.ajax([settings])`

  其中，settings是一个js字面量形式的对象，格式是{name:value,name:value... ...}，常用的name属性名如下

| 属性名称 | 解释                                                         |
| -------- | ------------------------------------------------------------ |
| url      | 请求的服务器端url地址                                        |
| async    | (默认: true) 默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false |
| data     | 发送到服务器的数据，可以是键值对形式，也可以是js对象形式     |
| type     | (默认: "GET") 请求方式 ("POST" 或 "GET")， 默认为 "GET"      |
| dataType | 预期的返回数据的类型，取值可以是 xml, html, script, json, text, _defaul等 |
| success  | 请求成功后的回调函数                                         |
| error    | 请求失败时调用此函数                                         |

- 实例

```js
//声明一个方法使用jQuery的ajax方法发送异步请求
function fn5() {
    $.ajax({
        url:"/demo01", //请求路径
        data:"username=lucy", //请求参数
        type:"POST", //请求方式
        success:function (result) {
            $("#msg").html(result)
        }, //请求成功时候的回调函数
        error:function () {
            $("#msg").html("服务器异常")
        } //请求失败时候的回调函数
    })
}
```

# 5. 使用JQ的Ajax完成用户名异步校验

## 5.1 需求分析

​	我们有一个网站，网站中都有注册的页面，当我们在注册的页面中输入用户名的时候，这个时候会提示，用户名是否存在。

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.10/pics/4.png)

## 5.2 思路分析

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.10/pics/5.png)

步骤:

1. 导入jar包，拷贝配置文件、工具类、解决乱码的过滤器、jquery文件
2. 创建包结构
3. 编写前端代码发送异步请求
4. 编写Servlet代码接收请求、调用业务层的方法处理请求、响应数据给客户端
5. 编写业务层的代码处理请求
6. 编写Dao层的代码查询数据

## 5.3 代码实现

+ 前端

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册页面</title>
    <script src="js/jquery-3.3.1.js"></script>
</head>
<body>
    <form action="#" method="post">
        用户名<input type="text" name="username" onblur="checkUsername(this.value)">
        <span id="uspan" style="color: red"></span>
    </form>

    <script>
        function checkUsername(username) {
            //校验用户名
            //发送异步请求给UserServlet，并且携带参数action和username
            $.post("user","action=checkUsername&username="+username,function (result) {
                //这个result就是服务器端的响应结果
                $("#uspan").html(result)
            })
        }
    </script>
</body>
</html>
```

* UserServlet代码

```java
package com.itheima.web.servlet;

import com.itheima.pojo.User;
import com.itheima.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Leevi
 * 日期2020-10-22  10:12
 */
@WebServlet("/user")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserService();
    /**
     * 校验用户名
     * @param request
     * @param response
     */
    public void checkUsername(HttpServletRequest request, HttpServletResponse response){
        try {
            //1. 获取请求参数
            String username = request.getParameter("username");
            //2. 调用业务层的方法，根据username查找用户
            User user = userService.findByUsername(username);
            //3. 判断user是否为null
            if (user == null) {
                //用户名可用

            }else {
                //用户名已被占用

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

* UserService的代码

```java
public class UserService {
    private UserDao userDao = new UserDao();
    public User findByUsername(String username) throws SQLException {
        return userDao.findByUsername(username);
    }
}
```

* UserDao的代码

```java
public class UserDao {
    private QueryRunner queryRunner = new QueryRunner(DruidUtil.getDataSource());
    public User findByUsername(String username) throws SQLException {
        String sql = "select * from user where username=?";
        User user = queryRunner.query(sql, new BeanHandler<>(User.class), username);
        return user;
    }
}
```

## 5.4 小结

**当前案例存在问题:**

服务器直接向客户端响应普通的字符串，客户端拿到了普通字符串之后，只能显示该字符串，没法根据服务器端的响应数据的不同而做一些自定义的处理，所以我们的想法是接下来将服务器端的响应数据封装成一个JavaBean对象

# 6. 创建ResultBean用于封装项目的响应数据

## 6.1 分析

服务器要响应哪些数据给客户端?

1. 服务器的状态: 是否出现异常
2. 这次请求的处理结果: 比如说要显示在客户端的数据、用户名是否可用、联系人列表等等
3. 如果服务器出现异常，则要将服务器想给客户端看的异常信息响应给客户端

## 6.2 实现

**封装的ResultBean的代码**

```java
package com.itheima.pojo;

import java.io.Serializable;

/**
 * 包名:com.itheima.pojo
 * @author Leevi
 * 日期2020-10-22  10:41
 * 封装服务器端给客户端的响应数据
 */
public class ResultBean implements Serializable {
    /**
     * 服务器端是否出现异常
     */
    private Boolean flag;
    /**
     * 服务器端处理请求之后要响应的数据
     */
    private Object data;
    /**
     * 服务器端的异常信息
     */
    private String errorMsg;

    @Override
    public String toString() {
        return "ResultBean{" +
                "flag=" + flag +
                ", data=" + data +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
```

## 6.3 小结

封装好ResultBean之后，服务器的响应数据就会封装在ResultBean对象中；然后再想办法将ResultBean对象的信息响应给客户端，但是客户端是无法解析ResultBean对象的，所以我们需要先将ResultBean对象转换成客户端能够认识的数据格式，然后再响应给客户端

# 7. JSON

## 7.1 json简介

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.10/pics/6.png)

- 用我们自己的话来说:  JSON就是一个容易生成和解析的数据格式; 

  ​		                             常用作客户端(前端,IOS,安卓)和服务器(JavaEE)之间的数据交换

## 7.2 语法

* 定义方式：
  * json是由键值对构成的，key和value之间使用冒号，个个键值对之间使用逗号隔开
  * json的value也可以是json类型
  * 如果有多个json对象，则可以存放在数组中
* 解析语法：
  * 获取json对象里的value值：`json对象.key`
  * 获取数组里指定索引的元素：`数组[索引]`

## 7.3 使用示例

* 对象形式

```js
var person01 = {name:"张三疯",age:189,address:"武当山"}

var person02 = { name:"张三疯",age:189,address:"武当山",wife:{
                name:"小花",
                age:18
            }
        }
```

* 数组形式

```js
//二,JSON数组 [ele,ele]
var sons = [{"name": "张三", "age": 18}, {"name": "张四", "age": 19}];
```

* 混合形式

```js
var person03 = {name:"张三疯", age:189, address:"武当山",
            wife:{
                name:"小花",
                age:18
            },
            sons:[
                {
                    nickanme:"张三",
                    age:1
                },
                {
                    nickname:"张四",
                    age:2
                }
            ]
        }
```

## 7.4 小结

1. JSON: 一种容易生成和解析的数据格式, 通常用作数据的交换

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.10/pics/7.png)

2. JSON格式
   + JSON对象 {key:value,key:value}
     + key是字符串
     + value是任意的合法数据类型
   + JSON数组  [ele,ele...]
   + 嵌套: 对象和数组的嵌套

# 8. 使用Jackson将java对象转换成json字符串

## 8.1 常见工具类

* 在Ajax使用过程中，服务端返回的数据可能比较复杂，比如`List<User>`；这些数据通常要转换成json格式，把json格式字符串返回客户端
* 常见的转换工具有：
  * Jackson：SpringMVC内置的转换工具
  * jsonlib：Java提供的转换工具(一般不用)
  * gson：google提供的转换工具(轻量级的框架)  
  * fastjson：Alibaba提供的转换工具(效率高速度快)

## 8.2 Jackson的API介绍

* Jackson提供了转换的核心类：`ObjectMapper`
* `ObjectMapper`的构造方法：无参构造
* `ObjectMapper`的常用方法：

| 方法                             | 说明                            |
| -------------------------------- | ------------------------------- |
| `writeValueAsString(Object obj)` | 把obj对象里的数据转换成json格式 |

## 8.3 java对象转成JSON

**步骤:**

1. 导入jar包

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.10/pics/8.png)

2. 创建ObjectMapper对象
3. 调用`writeValueAsString(Object obj)`

**实现**

```java
package com.itheima;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.pojo.ResultBean;
import com.itheima.pojo.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名:com.itheima
 * @author Leevi
 * 日期2020-10-22  10:59
 * 测试json转换
 * 为什么要将java对象转成json:因为我们要将java对象中的数据通过http协议传给各种客户端,而json是一种所有语言都能解析的数据格式
 * 在Java代码中怎么将Java对象转成json: 使用框架
 * 1. json-lib: 很古老的很重量级的框架，一般不用
 * 2. jackson: spring默认支持的json解析框架
 * 3. fastjson: 阿里开发的速度飞快的json解析框架
 * 4. gson: 谷歌开发一款使用特别简单的json解析框架
 *
 * 一: 使用jackson进行json和java对象的转换
 *    目标: 将Java对象转成json字符串
 */
public class TestJson {
    @Test
    public void test01() throws JsonProcessingException {
        User user = new User(1, "张三丰", "123456", "18999999999@163.com", "18999999999");

        //使用jackson将user对象转换成json字符串
        String jsonStr = new ObjectMapper().writeValueAsString(user);

        System.out.println(jsonStr);
    }

    @Test
    public void test02() throws JsonProcessingException {
        User user = new User(1, "张三丰", "123456", "18999999999@163.com", "18999999999");

        ResultBean resultBean = new ResultBean(true, user);

        //将resultBean对象转成json字符串
        String jsonStr = new ObjectMapper().writeValueAsString(resultBean);
        System.out.println(jsonStr);
    }

    @Test
    public void test03() throws JsonProcessingException {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "张三丰", "123456", "18999999999@163.com", "18999999999"));
        userList.add(new User(2, "周杰棍", "654321", "19898989898@163.com", "19898989898"));
        userList.add(new User(3, "奥巴马", "666666", "666666@163.com", "19866666666"));

        String jsonStr = new ObjectMapper().writeValueAsString(userList);
        System.out.println(jsonStr);
    }

    @Test
    public void test04() throws JsonProcessingException {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "张三丰", "123456", "18999999999@163.com", "18999999999"));
        userList.add(new User(2, "周杰棍", "654321", "19898989898@163.com", "19898989898"));
        userList.add(new User(3, "奥巴马", "666666", "666666@163.com", "19866666666"));


        ResultBean resultBean = new ResultBean(true, userList);

        String jsonStr = new ObjectMapper().writeValueAsString(resultBean);
        System.out.println(jsonStr);
    }
}
```

## 8.4 小结

* Map对象或者JavaBean对象转换成json的时候会得到一个json字符串
* List<Map>或者List<JavaBean>转换成json的时候会得到一个json数组的字符串

# 9. fastjson转换工具

## 9.1 fastjson的API介绍

* fastjson提供了核心类：`JSON`
* `JSON`提供了一些常用的**静态**方法：

| 方法                       | 说明                            |
| -------------------------- | ------------------------------- |
| `toJSONString(Object obj)` | 把obj对象里的数据转换成json格式 |

## 9.2 java对象转成json

**步骤**

1. 导入jar包

   ![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.10/pics/9.png)

2. 调用JSON.toJSONString(Object obj);

**实现**

```java
@Test
public void test06(){
    //使用fastjson将user对象转换成json字符串
    User user = new User(1,"奥巴马","123456","123456@qq.com","18999999999");

    String jsonStr = JSON.toJSONString(user);

    System.out.println(jsonStr);
}

//其它的map、list转成json对象也是一样的调用JSON.toJSONString(obj)实现
```

## 9.3 小结

1. fastJSON: 阿里巴巴提供的json和java对象互转工具
2. java对象转成json
   + JSON.toJsonString(Object obj)

# 10. 能够完成自动补全的(返回JSON数据)搜索提示

## 10.1 需求

​	实现一个搜索页面，在文本框中输入一个值以后(键盘抬起的时候)，给出一些提示

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.10/pics/10.gif)

## 10.2 思路分析

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/2021.07.10/pics/11.png)

1. 创建数据库和页面, 实体类
2. 给搜索框设置键盘抬起事件

```
inputEle.keyup(function(){
	//1.获得用户输入关键词
	//2.发送Ajax请求WordServlet 携带关键词, 获得响应的数据
	//3.解析响应的数据 填充页面

});
```

3. 创建WordsServlet

```
//1.获得请求参数(关键词)
//2.调用业务 根据关键词获得List<Words> list
//3.将list存储到ResultBean中，转成json字符串输出到浏览器
```

4. 创建WordsService

```
public List<Words> findByKeyWord(String keyWord){
	//调用Dao 查询

}
```

5. 创建WordsDao

```
SELECT * FROM words WHERE word LIKE '%a%' LIMIT 0,5
SELECT * FROM words WHERE word LIKE ? LIMIT 0,5
```

## 10.3 代码实现

### 10.3.1 环境的准备

+ 创建数据库

```sql
create table words(
    id int primary key auto_increment,
    word varchar(50)
);
insert into words values (null, 'all');

insert into words values (null, 'after');

insert into words values (null, 'app');

insert into words values (null, 'apple');

insert into words values (null, 'application');

insert into words values (null, 'applet');

insert into words values (null, 'and');

insert into words values (null, 'animal');

insert into words values (null, 'back');

insert into words values (null, 'bad');

insert into words values (null, 'bag');

insert into words values (null, 'ball');

insert into words values (null, 'banana');

insert into words values (null, 'bear');

insert into words values (null, 'bike');

insert into words values (null, 'car');

insert into words values (null, 'card');

insert into words values (null, 'careful');

insert into words values (null, 'cheese');

insert into words values (null, 'come');

insert into words values (null, 'cool');

insert into words values (null, 'dance');

insert into words values (null, 'day');

insert into words values (null, 'dirty');

insert into words values (null, 'duck');

insert into words values (null, 'east');

insert into words values (null, 'egg');

insert into words values (null, 'every');

insert into words values (null, 'example');
```

+ 创建JavaBean


  ```
  package com.itheima.bean;

  import java.io.Serializable;

  public class Words implements Serializable{


      private int id;
      private String word;


      public int getId() {
          return id;
      }

      public void setId(int id) {
          this.id = id;
      }

      public String getWord() {
          return word;
      }

      public void setWord(String word) {
          this.word = word;
      }

      @Override
      public String toString() {
          return "Words{" +
                  "id=" + id +
                  ", word='" + word + '\'' +
                  '}';
      }
  }

  ```

+ 导入jar,工具类, 配置文件

+ 创建页面,search.html

  ```HTML
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <title>Title</title>
  </head>
  <body>
  <center>
  
      <h1>黑马</h1>

      <input id="inputId" type="text" style="width: 500px; height: 38px;" /><input
        type="button" style="height: 38px;" value="黑马一下" />
      <div id="divId"
           style="width: 500px; border: 1px red solid; height: 300px; position: absolute; left: 394px;">
          <table id="tabId" width="100%" height="100%"  border="1px">
              
          </table>
      </div>
  
  </center>
  </body>
  </html>
  ```

### 10.3.2 实现

+ search.html

```js
<script>
    //声明一个搜索的方法
    function searchWords(keyword) {
        //判断输入框中的内容是否是空字符串，如果不是空字符串，才发请求
        if (keyword != "") {
            $("#divId").show()
            //发送异步请求给WordsServlet，携带参数action以及搜索关键字keyword
            $.post("words","action=search&keyword="+keyword,function (result) {
                //判断是否查询成功
                if (result.flag) {
                    //查询成功
                    var data = result.data;//json数组,每一个json对象就对应一个Words对象

                    //每次遍历添加之前，先要清除原来的数据
                    $("#tabId").empty()
                    //遍历出每一个json对象
                    $.each(data,function (index,words) {
                        //words就是遍历出来的每一个json
                        //获取word属性的值
                        var word = words.word;

                        //有一个word就往展示的table中添加一行
                        $("#tabId").append($("<tr><td>"+word+"</td></tr>"))
                    })
                }else {
                    //查询失败
                    $("#msg").html("网络异常")
                }
            },"json")
        }else {
            //说明:输入框的内容为空，就应该隐藏那个展示框
            $("#divId").hide()
        }
    }
</script>
```

+ WordsServlet

```java
package com.itheima.web.servlet;

import com.alibaba.fastjson.JSON;
import com.itheima.pojo.ResultBean;
import com.itheima.pojo.Words;
import com.itheima.service.WordsService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Leevi
 * 日期2020-10-22  11:58
 */
@WebServlet("/words")
public class WordsServlet extends BaseServlet {
    private WordsService wordsService = new WordsService();
    /**
     * 搜索提示
     * @param request
     * @param response
     */
    public void search(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultBean resultBean = new ResultBean(true);
        try {
            //1. 获取请求参数keyword的值
            String keyword = request.getParameter("keyword");
            //2. 调用业务层的方法，根据搜索关键字进行搜索
            List<Words> wordsList = wordsService.search(keyword);
            //3. 将搜索到的内容封装到ResultBean对象中
            resultBean.setData(wordsList);
        } catch (Exception e) {
            e.printStackTrace();
            //服务器出现异常
            resultBean.setFlag(false);
            resultBean.setErrorMsg("查询失败");
        }

        //将resultBean转换成json字符串响应给客户端
        String jsonStr = JSON.toJSONString(resultBean);

        response.getWriter().write(jsonStr);
    }
}
```

+ WordsService

```java
package com.itheima.service;

import com.itheima.dao.WordsDao;
import com.itheima.pojo.Words;

import java.util.List;

/**
 * 包名:com.itheima.service
 *
 * @author Leevi
 * 日期2020-10-22  11:58
 */
public class WordsService {
    private WordsDao wordsDao = new WordsDao();
    public List<Words> search(String keyword) throws Exception {
        return wordsDao.search(keyword);
    }
}
```

+ WordsDao

```java
package com.itheima.dao;

import com.itheima.pojo.Words;
import com.itheima.utils.DruidUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

/**
 * 包名:com.itheima.dao
 *
 * @author Leevi
 * 日期2020-10-22  11:58
 */
public class WordsDao {
    private QueryRunner queryRunner = new QueryRunner(DruidUtil.getDataSource());
    public List<Words> search(String keyword) throws Exception {
        String sql = "select * from words where word like ? limit 0,11";
        List<Words> wordsList = queryRunner.query(sql, new BeanListHandler<>(Words.class), "%" + keyword + "%");
        return wordsList;
    }
}
```

# 11. 将json字符串转换成Java对象

## 11.1 使用jackson将json字符串转换成java对象

### 11.1.1 API介绍

| 方法                                              | 功能                                               |
| ------------------------------------------------- | -------------------------------------------------- |
| `readValue(String json, Class type)`              | 把json字符串，还原成type类型的Java对象             |
| `readValue(String json, TypeReference reference)` | 把json字符串，还原成带泛型的复杂Java对象(比如List) |

* 其中`TypeReference`，`com.fasterxml.jackson.core.type.TypeReference`
  * 是一个抽象类，用于配置完整的泛型映射信息，避免泛型丢失的问题。用法示例：

``` java
// List<Integer> 类型的映射信息
TypeReference ref1 = new TypeReference<List<Integer>>() {};

// List<User> 类型的映射信息
TypeReference ref2 = new TypeReference<List<User>>() {};

// Map<String,User> 类型的映射信息
TypeReference ref3 = new TypeReference<Map<String,User>>(){};
```

### 11.1.2 使用jackson将json字符串转换JavaBean对象或者Map

```java
@Test
//把json转成JavaBean(user对象)
public void test06() throws IOException {
    String jsonStr = "{\"id\":1,\"username\":\"zs\",\"password\":\"123456\",\"email\":\"zs@163.com\",\"phone\":\"1386789898\"}";
    //1.调用JSON.parseObject(String json,Class clazz);
    //转换成user
    User user = JSON.parseObject(jsonStr, User.class);
    System.out.println(user);
    
    //转换成map
    Map map = JSON.parseObject(jsonStr, Map.class);
    System.out.println(map);
}
```

### 11.1.3 使用jackson将json数组字符串转换成List<JavaBean>

```java
@Test
//把json转成List<JavaBean>对象
public void test07() throws Exception {
    String jsonStr = "[{\"id\":1,\"username\":\"zs\",\"password\":\"123456\",\"email\":\"zs@163.com\",\"phone\":\"1386789898\"},{\"id\":2,\"username\":\"ls\",\"password\":\"123456\",\"email\":\"ls@163.com\",\"phone\":\"1386781898\"},{\"id\":3,\"username\":\"ww\",\"password\":\"123456\",\"email\":\"ww@163.com\",\"phone\":\"1386782898\"}]";
	
    //1.创建ObjectMapper对象
    ObjectMapper objectMapper = new ObjectMapper();

    //2.调用readValue()
    TypeReference<List<User>> ref = new TypeReference<List<User>>(){};
    List<User> list = objectMapper.readValue(jsonStr, ref);
    System.out.println(list);
}
```

## 11.2 使用fastjson将json字符串转换成java对象

### 11.2.1 API 介绍

| 方法                                   | 功能                                   |
| -------------------------------------- | -------------------------------------- |
| `parseObject(String json, Class type)` | 把json字符串，还原成type类型的Java对象 |
| `parseArray(String json, Class type)`  | 把json字符串，还原成List<T>            |

### 11.2.2 使用fastjson将json字符串转换成JavaBean对象或者Map

```java
@Test
    //把json转成JavaBean(user对象)
    public void test08() throws IOException {
        String jsonStr = "{\"id\":1,\"username\":\"zs\",\"password\":\"123456\",\"email\":\"zs@163.com\",\"phone\":\"1386789898\"}";

        //1.调用JSON.parseObject(String json,Class clazz);
        User user = JSON.parseObject(jsonStr, User.class);
        System.out.println(user);

    }

    @Test
    //把json转成Map
    public void test09() throws IOException {
        String jsonStr = "{\"id\":1,\"username\":\"zs\",\"password\":\"123456\",\"email\":\"zs@163.com\",\"phone\":\"1386789898\"}";

        //1.调用JSON.parseObject(String json,Class clazz);
        Map map = JSON.parseObject(jsonStr, Map.class);

        System.out.println(map);
    }
```

### 11.2.3 使用fastjson将json字符串转换成List<JavaBean>

```java
@Test
public void test11(){
    //使用fastjson将json数组的字符串，转换成List<User>
    String jsonArr = "[{\"id\":1,\"username\":\"奥巴马\",\"password\":\"123456\",\"email\":\"123456@qq.com\",\"phone\":\"18999999999\"},{\"id\":2,\"username\":\"周杰棍\",\"password\":\"654321\",\"email\":\"654321@qq.com\",\"phone\":\"18666666666\"},{\"id\":3,\"username\":\"王丽红\",\"password\":\"777777\",\"email\":\"777777@qq.com\",\"phone\":\"18777777777\"}]";

    List<User> userList = JSON.parseArray(jsonArr, User.class);

    for (User user : userList) {
        System.out.println(user.getUsername());
    }
}
```

# 12. 将联系人增删改查改成异步

### 查询所有功能改成异步
* 创建一个list.html页面，在页面加载的时候发送异步请求获取所有联系人的信息

```html
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
    <head>
        <!-- 指定字符集 -->
        <meta charset="utf-8">
        <!-- 使用Edge最新的浏览器的渲染方式 -->
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
width: 默认宽度与设备的宽度相同
initial-scale: 初始的缩放比，为1:1 -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
        <title>联系人信息展示页面</title>

        <!-- 1. 导入CSS的全局样式 -->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
        <script src="js/jquery-2.1.0.min.js"></script>
        <!-- 3. 导入bootstrap的js文件 -->
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            td, th {
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h3 style="text-align: center">显示所有联系人</h3>
            <table border="1" class="table table-bordered table-hover">
                <tr class="success">
                    <th>编号</th>
                    <th>姓名</th>
                    <th>性别</th>
                    <th>年龄</th>
                    <th>籍贯</th>
                    <th>QQ</th>
                    <th>邮箱</th>
                    <th>操作</th>
                </tr>
                <tbody>

                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="8" align="center"><a class="btn btn-primary" href="/add.html">添加联系人</a></td>
                    </tr>
                </tfoot>
            </table>
            <script>
                //当这个页面加载的时候，就发送一个异步请求给服务器，获取所有联系人的信息
                $.get("linkman?action=findAll",function (result) {
                    if (result.flag) {
                        //服务器没有异常
                        //取出要显示在页面的结果
                        var linkmanList = result.data;
                        //遍历出每一个linkMan
                        $.each(linkmanList,function (index,linkman) {
                            //每遍历出一条数据，就往tbody中添加一行
                            $("tbody").append("<tr>\n" +
                                              "                <td>"+(index+1)+"</td>\n" +
                                              "                <td>"+linkman.name+"</td>\n" +
                                              "                <td>"+linkman.sex+"</td>\n" +
                                              "                <td>"+linkman.age+"</td>\n" +
                                              "                <td>"+linkman.address+"</td>\n" +
                                              "                <td>"+linkman.qq+"</td>\n" +
                                              "                <td>"+linkman.email+"</td>\n" +
                                              "                <td><a class=\"btn btn-default btn-sm\" href=\"/linkman?action=findOne&id=${linkman.id}\">修改</a>&nbsp;\n" +
                                              "                    <a class=\"btn btn-default btn-sm\" href=\"javascript:;\" onclick=\"deleteLinkMan('${linkman.name}','${linkman.id}')\">删除</a></td>\n" +
                                              "            </tr>")
                        })
                    }
                },"json")
            </script>
        </div>
    </body>
</html>
```

* LinkManServlet中findAll()方法的代码

```java
/**
 * 查询所有联系人
 */
private void findAll(HttpServletRequest request,HttpServletResponse response) throws IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 调用业务层的方法，查询所有联系人
        List<LinkMan> linkManList = linkManService.findAll();
        //2. 将响应数据封装到ResultBean对象中
        resultBean.setData(linkManList);
    } catch (Exception e) {
        e.printStackTrace();
        //查询失败
        resultBean.setFlag(false);
        resultBean.setErrorMsg("查询联系人失败");
    }

    //将resultBean对象转换成json字符串，响应给客户端
    String jsonStr = JSON.toJSONString(resultBean);
    response.getWriter().write(jsonStr);
}
```

### 添加联系人改成异步

* 创建一个add.html页面，屏蔽表单的同步提交，当点击提交按钮的时候绑定点击事件异步提交请求

```html
<!-- HTML5文档-->
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
    <head>
        <!-- 指定字符集 -->
        <meta charset="utf-8">
        <!-- 使用Edge最新的浏览器的渲染方式 -->
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
width: 默认宽度与设备的宽度相同
initial-scale: 初始的缩放比，为1:1 -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
        <title>添加用户</title>

        <!-- 1. 导入CSS的全局样式 -->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
        <script src="js/jquery-2.1.0.min.js"></script>
        <!-- 3. 导入bootstrap的js文件 -->
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">
            <center><h3>添加联系人页面</h3></center>
            <div id="msg" style="color: red"></div>
            <form action="/linkman" id="myForm" method="post" onsubmit="return false">
                <input type="hidden" name="action" value="add">
                <div class="form-group">
                    <label for="name">姓名:</label>
                    <input type="text" class="form-control" id="name" name="name" placeholder="请输入姓名">
                </div>

                <div class="form-group">
                    <label>性别：</label>
                    <input type="radio" name="sex" value="男" checked="checked"/>男
                    <input type="radio" name="sex" value="女"/>女
                </div>

                <div class="form-group">
                    <label for="age">年龄：</label>
                    <input type="text" class="form-control" id="age" name="age" placeholder="请输入年龄">
                </div>

                <div class="form-group">
                    <label for="address">籍贯：</label>
                    <select name="address" class="form-control" id="address">
                        <option value="广东">广东</option>
                        <option value="广西">广西</option>
                        <option value="湖南">湖南</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="qq">QQ：</label>
                    <input type="text" class="form-control" name="qq" id="qq" placeholder="请输入QQ号码"/>
                </div>

                <div class="form-group">
                    <label for="email">Email：</label>
                    <input type="text" class="form-control" name="email" id="email" placeholder="请输入邮箱地址"/>
                </div>

                <div class="form-group" style="text-align: center">
                    <input class="btn btn-primary" type="submit" value="提交" onclick="addLinkman()"/>
                    <input class="btn btn-default" type="reset" value="重置" />
                    <input class="btn btn-default" type="button" value="返回" />
                </div>
            </form>

            <script>
                //声明一个添加联系人的方法
                function addLinkman() {
                    //获取表单中的所有请求参数
                    var parameter = $("#myForm").serialize();
                    //发送异步请求给"/linkman",请求方式post，提交表单中的所有数据
                    $.post("linkman",parameter,function (result) {
                        if (result.flag) {
                            //添加成功
                            //跳转到list.html
                            location.href = "list.html"
                        }else {
                            //添加失败
                            $("#msg").html(result.errorMsg)
                        }
                    },"json")
                }
            </script>
        </div>
    </body>
</html>
```

* LinkManServlet的add()方法的代码

```java
/**
 * 添加联系人
 * @param request
 * @param response
 * @throws IOException
 */
private void add(HttpServletRequest request,HttpServletResponse response) throws IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 获取所有请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        //2. 将请求参数封装到LinkMan对象中
        LinkMan linkMan = new LinkMan();
        BeanUtils.populate(linkMan,parameterMap);
        //3. 调用业务层的方法添加联系人
        linkManService.add(linkMan);
        //4. 添加成功
        // 告诉客户端添加成功
    } catch (Exception e) {
        e.printStackTrace();
        //告诉客户端添加失败
        resultBean.setFlag(false);
        resultBean.setErrorMsg("添加失败");
    }

    String jsonStr = JSON.toJSONString(resultBean);
    response.getWriter().write(jsonStr);
}
```

### 删除功能改成异步

* 前端list.html代码

```html
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
    <head>
        <!-- 指定字符集 -->
        <meta charset="utf-8">
        <!-- 使用Edge最新的浏览器的渲染方式 -->
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
width: 默认宽度与设备的宽度相同
initial-scale: 初始的缩放比，为1:1 -->
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
        <title>联系人信息展示页面</title>

        <!-- 1. 导入CSS的全局样式 -->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
        <script src="js/jquery-2.1.0.min.js"></script>
        <!-- 3. 导入bootstrap的js文件 -->
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            td, th {
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h3 style="text-align: center">显示所有联系人</h3>
            <table border="1" class="table table-bordered table-hover">
                <tr class="success">
                    <th>编号</th>
                    <th>姓名</th>
                    <th>性别</th>
                    <th>年龄</th>
                    <th>籍贯</th>
                    <th>QQ</th>
                    <th>邮箱</th>
                    <th>操作</th>
                </tr>
                <tbody>

                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="8" align="center"><a class="btn btn-primary" href="/add.html">添加联系人</a></td>
                    </tr>
                </tfoot>
            </table>
            <script>
                //当这个页面加载的时候，就发送一个异步请求给服务器，获取所有联系人的信息
                $.get("/linkman?action=findAll",function (result) {
                    if (result.flag) {
                        //服务器没有异常
                        //取出要显示在页面的结果
                        var linkmanList = result.data;
                        //遍历出每一个linkMan
                        $.each(linkmanList,function (index,linkman) {
                            //每遍历出一条数据，就往tbody中添加一行
                            $("tbody").append("<tr>\n" +
                                              "                <td>"+(index+1)+"</td>\n" +
                                              "                <td>"+linkman.name+"</td>\n" +
                                              "                <td>"+linkman.sex+"</td>\n" +
                                              "                <td>"+linkman.age+"</td>\n" +
                                              "                <td>"+linkman.address+"</td>\n" +
                                              "                <td>"+linkman.qq+"</td>\n" +
                                              "                <td>"+linkman.email+"</td>\n" +
                                              "                <td><a class=\"btn btn-default btn-sm\" href=\"/update.html?id="+linkman.id+"\">修改</a>&nbsp;\n" +
                                              "                    <a class=\"btn btn-default btn-sm\" href=\"javascript:;\" onclick=\"deleteLinkMan('"+linkman.name+"','"+linkman.id+"')\">删除</a></td>\n" +
                                              "            </tr>")
                        })
                    }
                },"json")

                //声明一个删除联系人的方法
                function deleteLinkMan(name,id) {
                    //1. 弹出一个确认框
                    var flag = confirm("你确定要删除"+name+"吗?");
                    if (flag) {
                        //确定要删除
                        //向LinkManServlet发送异步请求，并且携带要删除的联系人的id
                        $.post("linkman","action=delete&id="+id,function (result) {
                            if (result.flag) {
                                //删除成功，跳转到list.html页面
                                location.href = "list.html"
                            }else {
                                //删除失败
                                alert(result.errorMsg)
                            }
                        },"json")
                    }
                }
            </script>
        </div>
    </body>
</html>
```

* LinkManServlet的delete()方法的代码

```java
/**
 * 删除联系人
 * @param request
 * @param response
 * @throws IOException
 */
private void delete(HttpServletRequest request,HttpServletResponse response) throws IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 获取要删除的联系人的id
        Integer id = Integer.valueOf(request.getParameter("id"));
        //2. 调用业务层的方法，根据id删除联系人
        linkManService.deleteById(id);
    } catch (Exception e) {
        e.printStackTrace();
        resultBean.setFlag(false);
        resultBean.setErrorMsg("删除失败");
    }

    //将resultBean转换成json字符串，响应给客户端
    response.getWriter().write(JSON.toJSONString(resultBean));
}
```

### 数据回显功能改成异步

* update.html页面的代码（注意:需要引入getParameter.js脚本）

```js
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>
    <!-- 指定字符集 -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>修改联系人</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/jquery-2.1.0.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/getParameter.js"></script>

</head>
<body>
<div class="container" style="width: 400px;">
    <h3 style="text-align: center;">修改联系人</h3>
    <form id="updateForm" action="linkman" method="post" onsubmit="return false">
        <input type="hidden" name="id" id="id">
        <input type="hidden" name="action" value="update">
        <div class="form-group">
            <label for="name">姓名：</label>
            <input type="text" class="form-control" id="name" name="name"
                   placeholder="请输入姓名" />
        </div>

        <div class="form-group">
            <label>性别：</label>
            <input type="radio" name="sex" value="男" />男
            <input type="radio" name="sex" value="女" />女
        </div>

        <div class="form-group">
            <label for="age">年龄：</label>
            <input type="text" class="form-control" id="age"  name="age" placeholder="请输入年龄" />
        </div>

        <div class="form-group">
            <label for="address">籍贯：</label>
            <select name="address" id="address" class="form-control" >
                <option value="广东">广东</option>
                <option value="广西">广西</option>
                <option value="湖南">湖南</option>
            </select>
        </div>

        <div class="form-group">
            <label for="qq">QQ：</label>
            <input type="text" id="qq" value="${linkman.qq}" class="form-control" name="qq" placeholder="请输入QQ号码"/>
        </div>

        <div class="form-group">
            <label for="email">Email：</label>
            <input type="text" id="email" value="${linkman.email}" class="form-control" name="email" placeholder="请输入邮箱地址"/>
        </div>

        <div class="form-group" style="text-align: center">
            <input class="btn btn-primary" type="submit" value="提交"/>
            <input class="btn btn-default" type="reset" value="重置" />
            <input class="btn btn-default" type="button" value="返回"/>
        </div>
    </form>
    <script>
        //获取要修改的联系人的id
        var id = getParameter("id");
        //页面加载的时候，就应该向服务器发送异步请求，进行数据回显
        $.post("linkman","action=findOne&id="+id,function (result) {
            if (result.flag) {
                //数据回显成功
                //回显姓名
                $("#name").val(result.data.name)
                //回显性别
                if (result.data.sex == "男") {
                    $(":radio[value='男']").prop("checked",true)
                }else {
                    $(":radio[value='女']").prop("checked",true)
                }
                //回显年龄
                $("#age").val(result.data.age)

                //回显address
                $("#address").val(result.data.address)

                //回显qq和email
                $("#qq").val(result.data.qq)
                $("#email").val(result.data.email)

                //回显id
                $("#id").val(result.data.id)
            }else {
                //数据回显失败
                alert(result.errorMsg)
            }
        },"json")
    </script>
</div>
</body>
</html>
```

* LinkManServlet的findOne()方法

```java
/**
 * 根据id查询联系人
 * @param request
 * @param response
 */
private void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 获取要查询的联系人的id
        Integer id = Integer.valueOf(request.getParameter("id"));
        //2. 调用业务层的方法，根据id查询联系人
        LinkMan linkMan = linkManService.findById(id);
        //3. 将响应结果封装到ResultBean对象
        resultBean.setData(linkMan);
    } catch (Exception e) {
        e.printStackTrace();
        resultBean.setFlag(false);
        resultBean.setErrorMsg("数据回显失败");
    }

    //将resultBean转换成json字符串，响应给客户端
    response.getWriter().write(JSON.toJSONString(resultBean));
}
```

### 修改功能改成异步

* update.html代码

```html
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>
    <base href="<%=basePath%>"/>
    <!-- 指定字符集 -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>修改联系人</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/jquery-2.1.0.min.js"></script>
    <script src="js/bootstrap.min.js"></script>

    <!--引入getParameter.js-->
    <script src="js/getParameter.js"></script>
</head>
<body>
<div class="container" style="width: 400px;">
    <h3 style="text-align: center;">修改联系人</h3>
    <form action="linkman" id="updateForm" method="post" onsubmit="return false">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" id="id">
        <div class="form-group">
            <label for="name">姓名：</label>
            <input type="text" class="form-control" id="name"
                   name="name" placeholder="请输入姓名"/>
        </div>

        <div class="form-group">
            <label>性别:</label>
            <input id="male" type="radio" name="sex" value="男"  checked/>男
            <input id="female" type="radio" name="sex" value="女"  />女
        </div>

        <div class="form-group">
            <label for="age">年龄：</label>
            <input type="text" class="form-control" id="age" name="age" placeholder="请输入年龄" />
        </div>

        <div class="form-group">
            <label for="address">籍贯：</label>
            <select name="address" id="address" class="form-control" >
                    <option value="广东" selected>广东</option>
                    <option value="广西">广西</option>
                    <option value="湖南">湖南</option>
            </select>
        </div>

        <div class="form-group">
            <label for="qq">QQ：</label>
            <input type="text" id="qq" class="form-control" name="qq" placeholder="请输入QQ号码"/>
        </div>

        <div class="form-group">
            <label for="email">Email：</label>
            <input type="text" id="email" class="form-control" name="email" placeholder="请输入邮箱地址"/>
        </div>

        <div class="form-group" style="text-align: center">
            <input class="btn btn-primary" type="submit" value="提交" onclick="updateLinkMan()"/>
            <input class="btn btn-default" type="reset" value="重置" />
            <input class="btn btn-default" type="button" value="返回"/>
        </div>
    </form>
</div>
<script>
    //发送异步请求给LinkManServlet，根据id查询联系人信息
    var id = getParameter("id");
    $.post("linkman","action=findOne&id="+id,function (result) {
        if (result.flag) {
            //查询成功
            var linkman = result.data;
            $("#name").val(linkman.name)

            if (linkman.sex == "男") {
                $("#male").prop("checked",true)
            }else {
                $("#female").prop("checked",true)
            }

            $("#age").val(linkman.age)

            $("#address").val(linkman.address)

            $("#qq").val(linkman.qq)

            $("#email").val(linkman.email)

            $("#id").val(linkman.id)
        }
    },"json")

    function updateLinkMan() {
        //发送异步请求提交修改数据
        $.post("linkman",$("#updateForm").serialize(),function (result) {
            if (result.flag) {
                //修改成功
                location.href = "list.html"
            }else {
                alert("修改失败")
            }
        },"json")
    }
</script>
</body>
</html>
```

* LinkManServlet的update()方法的代码

```java
/**
 * 修改联系人的方法
 * @param request
 * @param response
 */
private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 获取所有请求参数封装到LinkMan对象中
        Map<String, String[]> parameterMap = request.getParameterMap();
        LinkMan linkMan = new LinkMan();
        BeanUtils.populate(linkMan,parameterMap);

        //2. 调用业务层的方法，修改联系人信息
        linkManService.update(linkMan);
    } catch (Exception e) {
        e.printStackTrace();
        resultBean.setFlag(false);
        resultBean.setErrorMsg("修改失败");
    }

    //将resultBean转换成json字符串，响应给客户端
    response.getWriter().write(JSON.toJSONString(resultBean));
}
```

### 分页功能改成异步

* list_page.html代码（注意: 需要引入getParameter.js脚本）

```html
<!DOCTYPE html>
<!-- 网页使用的语言 -->
<html lang="zh-CN">
<head>
    <!-- 指定字符集 -->
    <meta charset="utf-8">
    <!-- 使用Edge最新的浏览器的渲染方式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- viewport视口：网页可以根据设置的宽度自动进行适配，在浏览器的内部虚拟一个容器，容器的宽度与设备的宽度相同。
    width: 默认宽度与设备的宽度相同
    initial-scale: 初始的缩放比，为1:1 -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>Bootstrap模板</title>

    <!-- 1. 导入CSS的全局样式 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
    <script src="js/jquery-2.1.0.min.js"></script>
    <!-- 3. 导入bootstrap的js文件 -->
    <script src="js/bootstrap.min.js"></script>
    <style type="text/css">
        td, th {
            text-align: center;
        }
    </style>
    <script src="js/getParameter.js"></script>
</head>
<body>
<div class="container">
    <h3 style="text-align: center">显示所有联系人</h3>
    <table border="1" class="table table-bordered table-hover">
        <thead>
            <tr class="success">
                <th>编号</th>
                <th>姓名</th>
                <th>性别</th>
                <th>年龄</th>
                <th>籍贯</th>
                <th>QQ</th>
                <th>邮箱</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>

        </tbody>
        <tfoot>
            <tr>
                <td id="total" colspan="8" align="center">

                </td>
            </tr>
            <tr>
                <td colspan="8" align="center">
                    <ul class="pagination success">

                    </ul>
                </td>
            </tr>
        </tfoot>
    </table>
</div>
<script>
    //获取currentPage和pageSize的值
    var currentPage = parseInt(getParameter("currentPage"));
    var pageSize = parseInt(getParameter("pageSize"));

    //发送异步请求给LinkManServlet进行分页查询
    $.post("linkman","action=findByPage&currentPage="+currentPage+"&pageSize="+pageSize,function (result) {
        if (result.flag) {
            //分页查询成功
            //获取并展示当前页的联系人集合
            var linkmanList = result.data.list;
            $.each(linkmanList,function (index,linkman) {
                $("tbody").append($("<tr>\n" +
                    "                <td>"+(index+1)+"</td>\n" +
                    "                <td>"+linkman.name+"</td>\n" +
                    "                <td>"+linkman.sex+"</td>\n" +
                    "                <td>"+linkman.age+"</td>\n" +
                    "                <td>"+linkman.address+"</td>\n" +
                    "                <td>"+linkman.qq+"</td>\n" +
                    "                <td>"+linkman.email+"</td>\n" +
                    "                <td><a class=\"btn btn-default btn-sm\" href=\"修改联系人.html\">修改</a>&nbsp;<a class=\"btn btn-default btn-sm\" href=\"修改联系人.html\">删除</a></td>\n" +
                    "            </tr>"))
            })

            var totalSize = result.data.totalSize;
            var totalPage = result.data.totalPage;
            //展示总条数和每页条数
            $("#total").html("总数据条数为"+totalSize+"条<br>\n" +
                "                每页显示"+pageSize+"条数据")

            //展示页码
            //先展示上一页
            if(currentPage > 1){
                $(".pagination").append($("<li>\n" +
                    "                <a href=\"list_page.html?currentPage="+(currentPage-1)+"&pageSize="+pageSize+"\" aria-label=\"Previous\">\n" +
                    "                <span aria-hidden=\"true\">&laquo;</span>\n" +
                    "            </a>\n" +
                    "            </li>"))
            }

            //遍历展示页码
            for(var i=1;i<=totalPage;i++){
                //判断是否是当前页
                if (currentPage == i) {
                    //高亮
                    $(".pagination").append($("<li class=\"active\"><a href=\"#\">"+i+"</a></li>"))
                }else {
                    $(".pagination").append($(" <li><a href=\"list_page.html?currentPage="+i+"&pageSize="+pageSize+"\">"+i+"</a></li>"))
                }
            }

            //展示下一页
            if(currentPage < totalPage){
                $(".pagination").append($("<li>\n" +
                    "                            <a href=\"list_page.html?currentPage="+(currentPage+1)+"&pageSize="+pageSize+"\" aria-label=\"Next\">\n" +
                    "                                <span aria-hidden=\"true\">&raquo;</span>\n" +
                    "                            </a>\n" +
                    "                        </li>"))
            }
        }
    },"json")
</script>
</body>
</html>
```

* LinkManServlet的findByPage()方法的代码

```java
private void findByPage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
    ResultBean resultBean = new ResultBean(true);
    try {
        //1. 获取请求参数currentPage和pageSize的值
        Long currentPage = Long.valueOf(request.getParameter("currentPage"));
        Integer pageSize = Integer.valueOf(request.getParameter("pageSize"));

        //2. 调用业务层的方法，查询当前页的pageBean
        PageBean<LinkMan> pageBean = linkManService.findByPage(currentPage,pageSize);

        resultBean.setData(pageBean);
    } catch (Exception e) {
        e.printStackTrace();
        //分页查询失败
        resultBean.setFlag(false);
        resultBean.setErrorMsg("分页查询失败");
    }

    //将resultBean转换成json字符串，响应给客户端
    response.getWriter().write(JSON.toJSONString(resultBean));
}
```

