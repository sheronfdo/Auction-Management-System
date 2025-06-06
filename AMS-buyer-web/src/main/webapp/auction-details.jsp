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
    <c:if test="${not empty auction}">
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">${auction.itemName}</h3>
            </div>
            <ul class="list-group list-group-flush">
                <li class="list-group-item"><strong>Description:</strong> ${auction.description}</li>
                <li class="list-group-item"><strong>Start Price:</strong> $${auction.startPrice}</li>
                <li class="list-group-item"><strong>Bid Increment:</strong> $${auction.bidIncrement}</li>
                <li class="list-group-item"><strong>Current Bid:</strong> $<span id="currentBid">${auction.currentBid != null ? auction.currentBid : auction.startPrice}</span></li>
                <li class="list-group-item"><strong>Status:</strong> ${auction.status}</li>
                <li class="list-group-item"><strong>End Time:</strong> ${auction.endTime}</li>
            </ul>
            <c:if test="${auction.status == 'ACTIVE'}">
                <div class="card-body">
                    <form action="place-bid" method="post">
                        <input type="hidden" name="auctionId" value="${auction.auctionId}">
                        <div class="mb-3">
                            <label for="bidAmount" class="form-label">Your Bid (min: $<span id="minBid">${auction.currentBid != null ? auction.currentBid.add(auction.bidIncrement) : auction.startPrice.add(auction.bidIncrement)}</span>)</label>
                            <input type="number" step="0.01" class="form-control" id="bidAmount" name="bidAmount"
                                   min="${auction.currentBid != null ? auction.currentBid.add(auction.bidIncrement) : auction.startPrice.add(auction.bidIncrement)}" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Place Bid</button>
                    </form>
                </div>
            </c:if>
        </div>
        <h4 class="mt-4">Bid History</h4>
        <table class="table table-striped" id="bidTable">
            <thead>
            <tr>
                <th>Bid ID</th>
                <th>Buyer ID</th>
                <th>Amount</th>
                <th>Time</th>
            </tr>
            </thead>
            <tbody id="bidTableBody">
            <c:choose>
                <c:when test="${empty bids}">
                    <tr id="noBidsRow">
                        <td colspan="4" class="text-center">No bids have been placed on this auction.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="bid" items="${bids}">
                        <tr>
                            <td>${bid.bidId}</td>
                            <td>${bid.buyerId}</td>
                            <td>$${bid.bidAmount}</td>
                            <td>${bid.bidTime}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
        <a href="auctions" class="btn btn-secondary mt-3">Back to Auctions</a>
    </c:if>
</div>
<c:if test="${not empty auction}">
    <script>
        const auctionId = "${auction.auctionId}";
        if (!auctionId) {
            console.error('Auction ID is undefined');
            throw new Error('Failed to initialize WebSocket: Invalid auctionId');
        }
        console.log('Connecting WebSocket for auctionId: ' + auctionId);
        const wsUrl = `ws://localhost:8080/buyer/ws/bid-updates/`+auctionId;
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
                const minBidSpan = document.getElementById('minBid');
                const bidInput = document.getElementById('bidAmount');
                if (minBidSpan && bidInput) {
                    const newMin = (parseFloat(bid.bidAmount) + ${auction.bidIncrement}).toFixed(2);
                    minBidSpan.textContent = newMin;
                    bidInput.min = newMin;
                }
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