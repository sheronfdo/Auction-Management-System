<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/3/25
  Time: 8:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Buyer Dashboard</title>
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
                    <a class="nav-link active" href="dashboard.jsp">Dashboard</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="auctions">Browse Auctions</a>
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
    <h2>
        Welcome, <%= request.getSession(false) != null ? request.getSession(false).getAttribute("firstName") : "Buyer" %>
        !</h2>
    <div class="row mt-4">
        <div class="col-md-6 mb-3">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Browse Auctions</h5>
                    <p class="card-text">Explore active auctions and place bids.</p>
                    <a href="auctions" class="btn btn-primary">Browse Auctions</a>
                </div>
            </div>
        </div>
        <div class="col-md-6 mb-3">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Bid History</h5>
                    <p class="card-text">View your bidding activity.</p>
                    <a href="bid-history" class="btn btn-primary">View Bid History</a>
                </div>
            </div>
        </div>
        <div class="col-md-6 mb-3">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Profile Settings</h5>
                    <p class="card-text">Update your personal information.</p>
                    <a href="buyer-profile" class="btn btn-primary">Edit Profile</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO"
        crossorigin="anonymous"></script>
</body>
</html>