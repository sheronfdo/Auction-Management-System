<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/1/25
  Time: 9:06 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Update Auction</title>
</head>
<body>
<h2>Update Auction</h2>
<form action="update-auction" method="post">
  <input type="hidden" name="auctionId" value="<%= request.getAttribute("auctionId") %>">
  <label>Item Name: <input type="text" name="itemName" value="<%= request.getAttribute("itemName") %>"></label><br>
  <label>Description: <textarea name="description"><%= request.getAttribute("description") %></textarea></label><br>
  <label>Start Price: <input type="number" name="startPrice" step="0.01" value="<%= request.getAttribute("startPrice") %>"></label><br>
  <label>Bid Increment: <input type="number" name="bidIncrement" step="0.01" value="<%= request.getAttribute("bidIncrement") %>"></label><br>
  <label>Start Time: <input type="datetime-local" value="<%= request.getAttribute("startTime") %>"></label><br>
  <label>End Time: <input type="datetime-local" value="<%= request.getAttribute("endTime") %>"></label><br>
  <input type="submit" value="Update Auction">
</form>
<a href="seller-profile">Back to Profile</a>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>