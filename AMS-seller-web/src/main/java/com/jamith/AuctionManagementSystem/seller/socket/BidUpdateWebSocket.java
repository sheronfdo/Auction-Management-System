package com.jamith.AuctionManagementSystem.seller.socket;

import com.jamith.AuctionManagementSystem.core.auction.dto.BidDTO;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/bid-updates/{auctionId}")
public class BidUpdateWebSocket {
    private static final Map<Long, Set<Session>> sessionsByAuction = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("auctionId") String auctionIdStr) {
        try {
            Long auctionId = parseAuctionId(auctionIdStr);
            sessionsByAuction.computeIfAbsent(auctionId, k -> ConcurrentHashMap.newKeySet()).add(session);
            System.out.println("WebSocket session opened for auction: " + auctionId + ", session: " + session.getId());
        } catch (NumberFormatException e) {
            System.err.println("Invalid auctionId: " + auctionIdStr + ". Closing session: " + session.getId());
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Invalid auctionId"));
            } catch (IOException ex) {
                System.err.println("Failed to close session: " + ex.getMessage());
            }
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("auctionId") String auctionIdStr) {
        try {
            Long auctionId = parseAuctionId(auctionIdStr);
            Set<Session> sessions = sessionsByAuction.get(auctionId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    sessionsByAuction.remove(auctionId);
                }
                System.out.println("WebSocket session closed for auction: " + auctionId + ", session: " + session.getId());
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid auctionId on close: " + auctionIdStr + ", session: " + session.getId());
        }
    }

    @OnError
    public void onError(Session session, @PathParam("auctionId") String auctionIdStr, Throwable throwable) {
        System.err.println("WebSocket error for auctionId: " + auctionIdStr + ", session: " + session.getId() + ": " + throwable.getMessage());
        try {
            Long auctionId = parseAuctionId(auctionIdStr);
            onClose(session, auctionIdStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid auctionId on error: " + auctionIdStr + ", session: " + session.getId());
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Invalid auctionId"));
            } catch (IOException ex) {
                System.err.println("Failed to close session: " + ex.getMessage());
            }
        }
    }

    public static void broadcastBidUpdate(BidDTO bidDTO) {
        if (bidDTO == null || bidDTO.getAuctionId() == null) {
            System.err.println("Invalid BidDTO: " + (bidDTO == null ? "null" : "auctionId is null"));
            return;
        }
        Set<Session> sessions = sessionsByAuction.get(bidDTO.getAuctionId());
        System.out.println("Broadcasting bid update for auction: " + bidDTO.getAuctionId() + ", sessions: " + (sessions != null ? sessions.size() : "null"));
        if (sessions != null) {
            String message = String.format(
                    "{\"bidId\":%d,\"auctionId\":%d,\"buyerId\":%s,\"itemName\":\"%s\",\"bidAmount\":%.2f,\"bidTime\":\"%s\"}",
                    bidDTO.getBidId() != null ? bidDTO.getBidId() : 0,
                    bidDTO.getAuctionId(),
                    bidDTO.getBuyerId() != null ? bidDTO.getBuyerId().toString() : "0",
                    bidDTO.getItemName() != null ? bidDTO.getItemName().replace("\"", "\\\"") : "",
                    bidDTO.getBidAmount() != null ? bidDTO.getBidAmount().doubleValue() : 0.0,
                    bidDTO.getBidTime() != null ? bidDTO.getBidTime().toString() : ""
            );
            System.out.println("Bid update message: " + message);
            sessions.removeIf(session -> {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                        return false;
                    } catch (IOException e) {
                        System.err.println("Failed to send WebSocket message to session " + session.getId() + ": " + e.getMessage());
                        return true;
                    }
                }
                return true;
            });
        }
    }

    private Long parseAuctionId(String auctionIdStr) throws NumberFormatException {
        if (auctionIdStr == null || auctionIdStr.trim().isEmpty()) {
            throw new NumberFormatException("Empty or null auctionId");
        }
        return Long.parseLong(auctionIdStr);
    }
}