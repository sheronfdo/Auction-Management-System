<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 5/31/25
  Time: 10:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Admin Login</title>
</head>
<body>
<h2>Admin Login</h2>
<form action="login" method="post">
    <label>Email: <input type="email" name="email" required></label><br>
    <label>Password: <input type="password" name="password" required></label><br>
    <input type="submit" value="Login">
</form>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>