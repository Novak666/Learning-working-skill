<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EL表达式快速入门</title>
</head>
<body>
    <%--1.向域对象中添加数据--%>
    <% request.setAttribute("username","zhangsan"); %>

    <%--2.获取数据--%>
    Java代码块：<% out.println(request.getAttribute("username")); %> <br>

    JSP表达式：<%= request.getAttribute("username")%> <br>

    EL表达式：${username}
</body>
</html>
