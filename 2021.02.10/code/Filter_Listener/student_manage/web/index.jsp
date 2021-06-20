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
