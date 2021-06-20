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
