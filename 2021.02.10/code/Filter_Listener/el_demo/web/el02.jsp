<%@ page import="com.itheima.bean.Student" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EL表达式获取不同类型数据</title>
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
