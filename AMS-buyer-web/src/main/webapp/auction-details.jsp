<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/1/25
  Time: 9:20 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Auction Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand" href="dashboard.jsp">Auction System</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="dashboard.jsp">Dashboard</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="auctions">Browse Auctions</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="bid-history">Bid History</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="buyer-profile">Profile</a>
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
    <div class="card">
        <div class="card-body">
            <h3 class="card-title">${auction.itemName}</h3>
        </div>
        <ul class="list-group list-group-flush">
            <li class="list-group-item"><strong>Description:</strong> ${auction.description}</li>
            <li class="list-group-item"><strong>Start Price:</strong> $${auction.startPrice}</li>
            <li class="list-group-item"><strong>Bid Increment:</strong> $${auction.bidIncrement}</li>
            <li class="list-group-item"><strong>Current Bid:</strong>
                $${auction.currentBid != null ? auction.currentBid : auction.startPrice}</li>
            <li class="list-group-item"><strong>Status:</strong> ${auction.status}</li>
            <li class="list-group-item"><strong>End Time:</strong> ${auction.endTime}</li>
        </ul>
        <c:if test="${auction.status == 'ACTIVE'}">
            <div class="card-body">
                <form action="place-bid" method="post">
                    <input type="hidden" name="auctionId" value="${auction.auctionId}">
                    <div class="mb-3">
                        <label for="bidAmount" class="form-label">Your Bid (min:
                            $${auction.currentBid != null ? auction.currentBid.add(auction.bidIncrement) : auction.startPrice.add(auction.bidIncrement)})</label>
                        <input type="number" step="0.01" class="form-control" id="bidAmount" name="bidAmount" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Place Bid</button>
                </form>
            </div>
        </c:if>
    </div>
    <a href="auctions" class="btn btn-secondary mt-3">Back to Auctions</a>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO"
        crossorigin="anonymous"></script>
</body>
</html>