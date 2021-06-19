<%@ page import="com.itheima.bean.Student" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>查看学生</title>
</head>
<body>
    <table width="600px" border="1px">
        <tr>
            <th>学生姓名</th>
            <th>学生年龄</th>
            <th>学生成绩</th>
        </tr>
        <% ArrayList<Student> students = (ArrayList<Student>) session.getAttribute("students");
            for(Student stu : students) {
        %>
            <tr align="center">
                <td><%=stu.getUsername()%></td>
                <td><%=stu.getAge()%></td>
                <td><%=stu.getScore()%></td>
            </tr>
        <%}%>
    </table>
</body>
</html>
