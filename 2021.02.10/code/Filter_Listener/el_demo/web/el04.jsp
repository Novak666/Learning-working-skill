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
