<%@ page import="store.Cart" %>
<%@ page import="store.Product" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="store.ProductDAO" %><%--
  Created by IntelliJ IDEA.
  User: zghul
  Date: 6/29/2021
  Time: 9:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Shopping Cart</title>
</head>
<body>
<h1>Shopping Cart</h1>
<form action="cart" method="post">
    <ul>
        <%
            Cart cart = (Cart)session.getAttribute(Cart.NAME);
            Iterator<Product> it = cart.getProducts();
            while(it.hasNext()) {
                Product p = it.next();
                out.println("<li> <input type ='number' value='" + cart.getQuantity(p) + "' name='" + p.getId() + "'/>"
                        + p.getName() + ", " + p.getPrice() + "</li>");
            }
        %>
    </ul>
    Total: $<%=Math.round(cart.getTotal() * 100.0) / 100.0%> <input type="submit" value="Update Cart"/>
</form>
<a href="storeHome.jsp">Continue Shopping</a>
</body>
</html>
