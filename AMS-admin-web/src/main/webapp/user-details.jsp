<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/5/25
  Time: 6:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>User Details</title>
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
  <h2>User Details</h2>
  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>
  <c:if test="${not empty userProfile}">
    <div class="card">
      <div class="card-body">
        <h3>${userProfile.firstName} ${userProfile.lastName}</h3>
      </div>
      <ul class="list-group list-group-flush">
        <li class="list-group-item"><strong>User ID:</strong> ${userProfile.userId}</li>
        <li class="list-group-item"><strong>Email:</strong> ${userProfile.email}</li>
        <li class="list-group-item"><strong>Role:</strong> ${userProfile.role}</li>
        <li class="list-group-item"><strong>Status:</strong> ${userProfile.active ? 'Active' : 'Suspended'}</li>
      </ul>
      <div class="card-body">
        <form action="user-details" method="post" style="display:inline;">
          <input type="hidden" name="userId" value="${userProfile.userId}">
          <input type="hidden" name="action" value="${userProfile.active ? 'suspend' : 'activate'}">
          <button type="submit" class="btn btn-sm ${userProfile.active ? 'btn-warning' : 'btn-success'}">
              ${userProfile.active ? 'Suspend' : 'Activate'}
          </button>
        </form>
        <a href="users" class="btn btn-sm btn-secondary">Back to Users</a>
      </div>
    </div>
  </c:if>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>