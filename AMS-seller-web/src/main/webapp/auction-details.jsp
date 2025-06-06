<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 6/6/25
  Time: 5:00 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Auction Details</title>
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
                    <a class="nav-link" href="dashboard.jsp">Dashboard</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="list-auctions">My Auctions</a>
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
<div class="container mt-5">
    <h2>Auction Details</h2>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <div class="card mb-4">
        <div class="card-header">
            <h5>Auction #${auction.auctionId}: ${auction.itemName}</h5>
        </div>
        <div class="card-body">
            <p><strong>Description:</strong> ${auction.description}</p>
            <p><strong>Start Price:</strong> ${auction.startPrice}</p>
            <p><strong>Bid Increment:</strong> ${auction.bidIncrement}</p>
            <p><strong>Current Bid:</strong> $<span id="currentBid"> ${auction.currentBid != null ? auction.currentBid : 'No bids yet'}</span></p>
            <p><strong>Status:</strong> ${auction.status}</p>
            <p><strong>Start Time:</strong> ${auction.startTime}</p>
            <p><strong>End Time:</strong> ${auction.endTime}</p>
            <c:if test="${auction.status == 'CLOSED' && not empty winner}">
                <div class="alert alert-success">
                    <strong>Winner:</strong> Buyer ID: ${winner.buyerId}, Bid: ${winner.bidAmount}, Bid Time: ${winner.bidTime}
                </div>
            </c:if>
        </div>
    </div>
    <h4>Bid History</h4>
    <c:choose>
        <c:when test="${empty bids}">
            <div class="alert alert-info">No bids have been placed on this auction.</div>
        </c:when>
        <c:otherwise>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Bid ID</th>
                    <th>Buyer ID</th>
                    <th>Bid Amount</th>
                    <th>Bid Time</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="bid" items="${bids}">
                    <tr>
                        <td>${bid.bidId}</td>
                        <td>${bid.buyerId}</td>
                        <td>${bid.bidAmount}</td>
                        <td>${bid.bidTime}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
    <a href="list-auctions" class="btn btn-primary mt-3">Back to My Auctions</a>
</div>
<c:if test="${not empty auction}">
    <script>
        const auctionId = "${auction.auctionId}";
        if (!auctionId) {
            console.error('Auction ID is undefined');
            throw new Error('Failed to initialize WebSocket: Invalid auctionId');
        }
        console.log('Connecting WebSocket for auctionId: ' + auctionId);
        const wsUrl = `ws://localhost:8080/seller/ws/bid-updates/`+auctionId;
        console.log('WebSocket URL: ' + wsUrl);
        const ws = new WebSocket(wsUrl);

        ws.onopen = function() {
            console.log('WebSocket connected for auction ' + auctionId);
        };

        ws.onmessage = function(event) {
            console.log('WebSocket message received: ' + event.data);
            try {
                const bid = JSON.parse(event.data);
                document.getElementById('currentBid').textContent = bid.bidAmount.toFixed(2);
                <%--const minBidSpan = document.getElementById('minBid');--%>
                <%--const bidInput = document.getElementById('bidAmount');--%>
                <%--if (minBidSpan && bidInput) {--%>
                <%--    const newMin = (parseFloat(bid.bidAmount) + ${auction.bidIncrement}).toFixed(2);--%>
                <%--    minBidSpan.textContent = newMin;--%>
                <%--    bidInput.min = newMin;--%>
                <%--}--%>
                <%--const tbody = document.getElementById('bidTableBody');--%>
                <%--const row = document.createElement('tr');--%>
                <%--row.innerHTML = `--%>
                <%--    <td>${bid.bidId}</td>--%>
                <%--    <td>${bid.buyerId}</td>--%>
                <%--    <td>$${bid.bidAmount.toFixed(2)}</td>--%>
                <%--    <td>${bid.bidTime}</td>--%>
                <%--`;--%>
                <%--tbody.prepend(row);--%>
            } catch (e) {
                console.error('Error parsing WebSocket message: ', e);
            }
        };

        ws.onclose = function(event) {
            console.log('WebSocket closed: ', event);
        };

        ws.onerror = function(error) {
            console.error('WebSocket error: ', error);
        };
    </script>
</c:if>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO"
        crossorigin="anonymous"></script>
</body>
</html>