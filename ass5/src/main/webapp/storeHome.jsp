<%@ page import="store.ProductDAO" %>
<%@ page import="store.Product" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.SQLException" %><%--
  Created by IntelliJ IDEA.
  User: zghul
  Date: 6/29/2021
  Time: 8:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Student Store</title>
</head>
<body>
<h1>Student Store</h1>
<p>Item available:</p>

<ul>
    <%
        ProductDAO dao = (ProductDAO) application.getAttribute(ProductDAO.NAME);
        ArrayList<Product> list = null;
        try {
            list = dao.getAllProducts();
        } catch (SQLException ignored) { }
        assert list != null;
        for (Product p : list) {
            out.println("<li><a href=\"productPage.jsp?id=" + p.getId() + "\">" + p.getName() + "</a></li>");
        }
    %>
</ul>
</body>
</html>