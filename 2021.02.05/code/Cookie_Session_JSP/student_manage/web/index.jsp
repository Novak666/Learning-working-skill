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
