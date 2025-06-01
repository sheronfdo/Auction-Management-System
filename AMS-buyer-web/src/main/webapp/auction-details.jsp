<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/1/25
  Time: 9:20 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Auction Details</title>
</head>
<body>
<h2>Auction Details</h2>
<p>Item Name: <%= request.getAttribute("itemName") %></p>
<p>Description: <%= request.getAttribute("description") %></p>
<p>Current Bid: <%= request.getAttribute("currentBid") != null ? request.getAttribute("currentBid") : request.getAttribute("startPrice") %></p>
<p>Bid Increment: <%= request.getAttribute("bidIncrement") %></p>
<p>End Time: <%= request.getAttribute("endTime") %></p>
<p>Status: <%= request.getAttribute("status") %></p>
<a href="auctions">Back to Auctions</a>
<form action="logout" method="post">
  <input type="submit" value="Logout">
</form>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>