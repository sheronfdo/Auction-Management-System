<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/2/25
  Time: 5:41 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>My Auctions</title>
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
    <h2>My Auctions</h2>
    <a href="create-auction.jsp" class="btn btn-primary mb-3">Create New Auction</a>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <c:choose>
        <c:when test="${empty auctions}">
            <div class="alert alert-info">You have no auctions. Create one to get started!</div>
        </c:when>
        <c:otherwise>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Item Name</th>
                    <th>Current Bid</th>
                    <th>Status</th>
                    <th>End Time</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="auction" items="${auctions}">
                    <tr>
                        <td>${auction.auctionId}</td>
                        <td>${auction.itemName}</td>
                        <td>${auction.currentBid != null ? auction.currentBid : auction.startPrice}</td>
                        <td>${auction.status}</td>
                        <td>${auction.endTime}</td>
                        <td>
                            <a href="auction-details?auctionId=${auction.auctionId}" class="btn btn-sm btn-info">View Details</a>
                            <c:if test="${auction.status == 'PENDING'}">
                                <a href="update-auction?auctionId=${auction.auctionId}" class="btn btn-sm btn-warning">Update</a>
                                <form action="delete-auction" method="post" style="display:inline;">
                                    <input type="hidden" name="auctionId" value="${auction.auctionId}">
                                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?')">Delete</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div><script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
              integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO"
              crossorigin="anonymous"></script></body>
</html>