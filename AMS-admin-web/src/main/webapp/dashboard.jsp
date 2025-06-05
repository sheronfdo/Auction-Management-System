<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 5/31/25
  Time: 7:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Admin Dashboard</title>
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
                    <a class="nav-link active" href="dashboard">Dashboard</a>
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
    <h2>Admin Dashboard</h2>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <h3>Auctions</h3>
    <a href="create-auction" class="btn btn-primary mb-3">Create New Auction</a>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Item Name</th>
            <th>Status</th>
            <th>Current Bid</th>
            <th>End Time</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="auction" items="${auctions}">
            <tr>
                <td>${auction.auctionId}</td>
                <td>${auction.itemName}</td>
                <td>${auction.status}</td>
                <td>$${auction.currentBid != null ? auction.currentBid : auction.startPrice}</td>
                <td>${auction.endTime}</td>
                <td>
                    <a href="auction-details?auctionId=${auction.auctionId}" class="btn btn-sm btn-info">View</a>
                    <c:if test="${auction.status == 'ACTIVE'}">
                        <form action="close-auction" method="post" style="display:inline;">
                            <input type="hidden" name="auctionId" value="${auction.auctionId}">
                            <button type="submit" class="btn btn-sm btn-warning">Close</button>
                        </form>
                    </c:if>
                    <form action="delete-auction" method="post" style="display:inline;">
                        <input type="hidden" name="auctionId" value="${auction.auctionId}">
                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Delete this auction?')">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>