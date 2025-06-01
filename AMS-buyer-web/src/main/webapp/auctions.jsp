<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/1/25
  Time: 7:25 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
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
  <!-- Mock auction data -->
  <tr>
    <td>1</td>
    <td>Vintage Watch</td>
    <td>$100</td>
    <td>2025-06-02 12:00</td>
    <td><a href="#">Place Bid</a></td>
  </tr>
  <tr>
    <td>2</td>
    <td>Antique Vase</td>
    <td>$250</td>
    <td>2025-06-03 15:00</td>
    <td><a href="#">Place Bid</a></td>
  </tr>
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