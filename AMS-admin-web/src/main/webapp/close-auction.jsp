<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/1/25
  Time: 9:24 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Close Auction</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
  <div class="container-fluid">
    <a class="navbar-brand" href="dashboard">Auction System</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav me-auto">
        <li class="nav-item">
          <a class="nav-link" href="dashboard">Dashboard</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="create-auction">Create Auction</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="users">Manage Users</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="admin-profile">Profile</a>
        </li>
      </ul>
      <form class="d-flex" action="logout" method="post">
        <button class="btn btn-outline-light" type="submit">Logout</button>
      </form>
    </div>
  </div>
</nav>
<div class="container mt-4">
  <h2>Close Auction</h2>
  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>
  <form action="close-auction" method="post">
    <div class="mb-3">
      <label for="auctionId" class="form-label">Auction ID</label>
      <input type="number" class="form-control" id="auctionId" name="auctionId" required>
    </div>
    <button type="submit" class="btn btn-primary">Close Auction</button>
    <a href="dashboard" class="btn btn-secondary">Cancel</a>
  </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>