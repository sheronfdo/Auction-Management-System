<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 5/31/25
  Time: 7:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>
<h2>Admin Dashboard</h2>
<p>Welcome, <%= request.getAttribute("firstName") %> <%= request.getAttribute("lastName") %></p>
<a href="admin-profile">Update Profile</a>
<form action="logout" method="post">
    <input type="submit" value="Logout">
</form>
</body>
</html>