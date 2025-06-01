<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/1/25
  Time: 9:05 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<head>
  <title>Create Auction</title>
</head>
<body>
<h2>Create New Auction</h2>
<form action="create-auction" method="post">
  <label>Item Name: <input type="text" name="itemName" required></label><br>
  <label>Description: <textarea name="description"></textarea></label><br>
  <label>Start Price: <input type="number" name="startPrice" step="0.01" min="1" required></label><br>
  <label>Bid Increment: <input type="number" name="bidIncrement" step="0.01" min="0.0.01" value="1.00" required></label>
  <br>
  <label>Start Time: <input type="datetime-local" name="startTime" required></label><br>
  <label>End Time: <input type="datetime-local" name="endTime" required></label><br>
  <input type="submit" value="Create Auction">
</form>
<a href="seller-profile">Back to Profile</a>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>