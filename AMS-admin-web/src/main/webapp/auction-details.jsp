<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/5/25
  Time: 6:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Auction Details</title>
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
    <h2>Auction Details</h2>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <c:if test="${not empty auction}">
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">${auction.itemName}</h3>
            </div>
            <ul class="list-group list-group-flush">
                <li class="list-group-item"><strong>Description:</strong> ${auction.description}</li>
                <li class="list-group-item"><strong>Start Price:</strong> $${auction.startPrice}</li>
                <li class="list-group-item"><strong>Bid Increment:</strong> $${auction.bidIncrement}</li>
                <li class="list-group-item"><strong>Current Bid:</strong> $${auction.currentBid != null ? auction.currentBid : auction.startPrice}</li>
                <li class="list-group-item"><strong>Status:</strong> ${auction.status}</li>
                <li class="list-group-item"><strong>End Time:</strong> ${auction.endTime}</li>
            </ul>
        </div>
        <h4 class="mt-4">Bid History</h4>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Bid ID</th>
                <th>Buyer ID</th>
                <th>Amount</th>
                <th>Time</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="bid" items="${bids}">
                <tr>
                    <td>${bid.bidId}</td>
                    <td>${bid.buyerId}</td>
                    <td>$${bid.bidAmount}</td>
                    <td>${bid.bidTime}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <a href="dashboard" class="btn btn-secondary mt-3">Back to Dashboard</a>
    </c:if>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>