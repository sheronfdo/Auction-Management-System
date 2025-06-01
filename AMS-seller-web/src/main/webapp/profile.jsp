<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 5/31/25
  Time: 9:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Seller Profile</title>
</head>
<body>
<h2>Seller Profile</h2>
<form action="seller-profile" method="post">
  <label>Email: <input type="email" name="email" value="<%= request.getAttribute("email") %>"></label><br>
  <label>First Name: <input type="text" name="firstName" value="<%= request.getAttribute("firstName") %>"></label><br>
  <label>Last Name: <input type="text" name="lastName" value="<%= request.getAttribute("lastName") %>"></label><br>
  <label>New Password: <input type="password" name="password"></label><br>
  <input type="submit" value="Update Profile">
</form>
<form action="logout" method="post">
  <input type="submit" value="Logout">
</form>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>
