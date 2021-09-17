<%--
  Created by IntelliJ IDEA.
  User: zghul
  Date: 6/29/2021
  Time: 7:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create Account</title>
</head>
<body>
<h1>The Name <%=request.getParameter("username")%> is already used</h1>
<p>Please enter another name and pasword.</p>
<form action="create" method="post">
    User Name: <input type="text" name="username"/><br>
    Password: <input type="password" name="pass"/>
    <input type="submit" value="Login">
</form>
</body>
</html>
