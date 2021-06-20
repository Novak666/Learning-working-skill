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
