<%--
  Created by IntelliJ IDEA.
  User: zghul
  Date: 6/29/2021
  Time: 6:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Welcome <%= request.getParameter("username")%></title>
</head>
<body>
<h1>Welcome <%= request.getParameter("username")%></h1>
</body>
</html>
