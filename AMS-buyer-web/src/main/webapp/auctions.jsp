<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/1/25
  Time: 7:25 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Browse Auctions</title>
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
    <h2>Browse Active Auctions</h2>
    <p>Welcome, <%= request.getAttribute("firstName") %>
    </p>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <c:choose>
        <c:when test="${empty auctions}">
            <div class="alert alert-info">No active auctions found.</div>
        </c:when>
        <c:otherwise>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Auction ID</th>
                    <th>Item Name</th>
                    <th>Current Bid</th>
                    <th>End Time</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="auction" items="${auctions}">
                    <tr>
                        <td>${auction.auctionId}</td>
                        <td>${auction.itemName}</td>
                        <td>${auction.currentBid != null ? auction.currentBid : auction.startPrice}</td>
                        <td>${auction.endTime}</td>
                        <td><a href="auction-details?auctionId=${auction.auctionId}" class="btn btn-sm btn-primary">View
                            Details</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO"
        crossorigin="anonymous"></script>
</body>
</html>