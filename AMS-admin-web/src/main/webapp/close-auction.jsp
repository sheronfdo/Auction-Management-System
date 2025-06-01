<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/1/25
  Time: 9:24 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Close Auction</title>
</head>
<body>
<h2>Close Auction</h2>
<form action="close-auction" method="post">
  <label>Auction ID: <input type="number" name="auctionId" required></label><br>
  <input type="submit" value="Close Auction">
</form>
<a href="dashboard">Back to Dashboard</a>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>
