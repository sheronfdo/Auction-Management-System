<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 5/31/25
  Time: 8:09 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Register</title>
</head>
<body>
<h2>Register</h2>
<form action="register" method="post">
  <label>Email: <input type="email" name="email" required></label><br>
  <label>Password: <input type="password" name="password" required></label><br>
  <label>First Name: <input type="text" name="firstName"></label><br>
  <label>Last Name: <input type="text" name="lastName"></label><br>
  <label hidden="hidden">Role:
    <select name="role" required hidden="hidden">
      <option value="BUYER" selected>Buyer</option>
      <option value="SELLER">Seller</option>
    </select>
  </label><br>
  <input type="submit" value="Register">
</form>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>