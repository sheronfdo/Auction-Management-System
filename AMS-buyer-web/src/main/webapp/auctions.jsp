<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/1/25
  Time: 7:25 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Auction Listings</title>
</head>
<body>
<h2>Available Auctions</h2>
<p>Welcome, <%= request.getAttribute("firstName") %> <%= request.getAttribute("lastName") %></p>
<table border="1">
  <tr>
    <th>Auction ID</th>
    <th>Item Name</th>
    <th>Current Bid</th>
    <th>End Time</th>
    <th>Action</th>
  </tr>
  <c:forEach var="auction" items="${auctions}">
    <tr>
      <td>${auction.auctionId}</td>
      <td>${auction.itemName}</td>
      <td>${auction.currentBid != null ? auction.currentBid : auction.startPrice}</td>
      <td>${auction.endTime}</td>
      <td><a href="auction-details?auctionId=${auction.auctionId}">View Details</a></td>
    </tr>
  </c:forEach>
</table>
<a href="buyer-profile">View Profile</a>
<form action="logout" method="post">
  <input type="submit" value="Logout">
</form>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>