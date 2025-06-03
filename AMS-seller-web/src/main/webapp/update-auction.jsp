<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/1/25
  Time: 9:06 AM
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" %>--%>
<%--<html>--%>
<%--<head>--%>
<%--  <title>Update Auction</title>--%>
<%--</head>--%>
<%--<body>--%>
<%--<h2>Update Auction</h2>--%>
<%--<form action="update-auction" method="post">--%>
<%--  <input type="hidden" name="auctionId" value="<%= request.getAttribute("auctionId") %>">--%>
<%--  <label>Item Name: <input type="text" name="itemName" value="<%= request.getAttribute("itemName") %>"></label><br>--%>
<%--  <label>Description: <textarea name="description"><%= request.getAttribute("description") %></textarea></label><br>--%>
<%--  <label>Start Price: <input type="number" name="startPrice" step="0.01" value="<%= request.getAttribute("startPrice") %>"></label><br>--%>
<%--  <label>Bid Increment: <input type="number" name="bidIncrement" step="0.01" value="<%= request.getAttribute("bidIncrement") %>"></label><br>--%>
<%--  <label>Start Time: <input type="datetime-local" value="<%= request.getAttribute("startTime") %>"></label><br>--%>
<%--  <label>End Time: <input type="datetime-local" value="<%= request.getAttribute("endTime") %>"></label><br>--%>
<%--  <input type="submit" value="Update Auction">--%>
<%--</form>--%>
<%--<a href="seller-profile">Back to Profile</a>--%>
<%--<% if (request.getAttribute("error") != null) { %>--%>
<%--<p style="color:red"><%= request.getAttribute("error") %></p>--%>
<%--<% } %>--%>
<%--</body>--%>
<%--</html>--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Update Auction</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
  <div class="container-fluid">
    <a class="navbar-brand" href="dashboard.jsp">Auction System</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav me-auto">
        <li class="nav-item">
          <a class="nav-link active" href="dashboard.jsp">Dashboard</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="list-auctions">My Auctions</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="create-auction.jsp">Create Auction</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="seller-profile">Profile</a>
        </li>
      </ul>
      <form class="d-flex" action="logout" method="post">
        <button class="btn btn-outline-light" type="submit">Logout</button>
      </form>
    </div>
  </div>
</nav>
<div class="container mt-4">
  <h2>Update Auction</h2>
  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>
  <form action="update-auction" method="post">
    <input type="hidden" name="auctionId" value="${auctionId}">
    <div class="mb-3">
      <label for="itemName" class="form-label">Item Name</label>
      <input type="text" class="form-control" id="itemName" name="itemName" value="${itemName}" required>
    </div>
    <div class="mb-3">
      <label for="description" class="form-label">Description</label>
      <textarea class="form-control" id="description" name="description">${description}</textarea>
    </div>
    <div class="mb-3">
      <label for="startPrice" class="form-label">Start Price</label>
      <input type="number" step="0.01" class="form-control" id="startPrice" name="startPrice" value="${startPrice}" required>
    </div>
    <div class="mb-3">
      <label for="bidIncrement" class="form-label">Bid Increment</label>
      <input type="number" step="0.01" class="form-control" id="bidIncrement" name="bidIncrement" value="${bidIncrement}" required>
    </div>
    <div class="mb-3">
      <label for="startTime" class="form-label">Start Time</label>
      <input type="datetime-local" class="form-control" id="startTime" name="startTime" value="${startTime}" required>
    </div>
    <div class="mb-3">
      <label for="endTime" class="form-label">End Time</label>
      <input type="datetime-local" class="form-control" id="endTime" name="endTime" value="${endTime}" required>
    </div>
    <button type="submit" class="btn btn-primary">Update Auction</button>
    <a href="list-auctions" class="btn btn-secondary">Cancel</a>
  </form>
</div><script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
              integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO"
              crossorigin="anonymous"></script></body>
</html>