<%@ page import="store.ProductDAO" %>
<%@ page import="store.Product" %>
<%@ page import="java.sql.SQLException" %><%--
  Created by IntelliJ IDEA.
  User: zghul
  Date: 6/29/2021
  Time: 8:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%
    ProductDAO dao = (ProductDAO) application.getAttribute(ProductDAO.NAME);
    Product product = null;
    try {
        product = dao.getProduct(request.getParameter("id"));
    } catch (SQLException ignored) { }
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%=product.getName()%></title>
</head>
<body>
<h1><%=product.getName()%></h1>
<img src="<%="/store-images/" + product.getImagePath()%>" />

<form action="cart" method="post">
    <%=product.getPrice()%>$ <input name="productID" type="hidden" value="<%=product.getId()%>"/>
    <input type="submit" value="Add to Cart"/>
</form>
</body>
</html>
