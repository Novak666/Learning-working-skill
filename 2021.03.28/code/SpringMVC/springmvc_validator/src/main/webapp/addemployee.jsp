<%@page pageEncoding="UTF-8" language="java" contentType="text/html;UTF-8" %>
<html>
<head>
    <title>添加员工-用于演示表单验证</title>
</head>
<body>
    <form action="/addemployee" method="post">
                                                           <%--页面使用${}获取后台传递的校验信息--%>
        员工姓名：<input type="text" name="name"><span style="color:red">${name}</span><br/>
        员工年龄：<input type="text" name="age"><span style="color:red">${age}</span><br/>
        <%--注意，引用类型的校验未通过信息不是通过对象进行封装的，直接使用对象名.属性名的格式作为整体属性字符串进行保存的，和使用者的属性传递方式有关，不具有通用性，仅适用于本案例--%>
        省：<input type="text" name="address.provinceName"><span style="color:red">${requestScope['address.provinceName']}</span><br/>
        <input type="submit" value="提交">
    </form>
</body>
</html>































<%--<form action="/addemployee" method="post">--%>
<%--    员工姓名：<input type="text" name="name"><br/>--%>
<%--    员工年龄：<input type="text" name="age"><br/>--%>
<%--    员工生日：<input type="text" name="birthday"><br/>--%>
<%--    员工性别：<input type="radio" name="gender" value="男">男--%>
<%--    <input type="radio" name="gender" value="女">女<br/>--%>
<%--    员工邮箱：<input type="text" name="email"><br/>--%>
<%--    员工电话：<input type="text" name="telephone"><br/>--%>
<%--    员工类型：<select name="type">--%>
<%--    <option value="0">---请选择---</option>--%>
<%--    <option value="1">正式工</option>--%>
<%--    <option value="2">临时工</option>--%>
<%--</select><br/>--%>
<%--    <fieldset style="width:260px">--%>
<%--        <legend>员工住址</legend>--%>
<%--        所在省份：<input type="text" name="address.provinceName"><br/>--%>
<%--        所在城市：<input type="text" name="address.cityName"><br/>--%>
<%--        详细地址：<input type="text" name="address.detail"><br/>--%>
<%--        邮政编码：<input type="text" name="address.zipCode"><br/>--%>
<%--    </fieldset>--%>
<%--    <br/>--%>
<%--    <input type="submit" value="提交">--%>
<%--</form>--%>