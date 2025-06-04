package com.jamith.AuctionManagementSystem.buyer.socket;

import com.jamith.AuctionManagementSystem.core.auction.dto.BidDTO;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/bid-updates/{auctionId}")
public class BidUpdateWebSocket {
    private static final Map<Long, Set<Session>> sessionsByAuction = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("auctionId") Long auctionId) {
        sessionsByAuction.computeIfAbsent(auctionId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("auctionId") Long auctionId) {
        Set<Session> sessions = sessionsByAuction.get(auctionId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                sessionsByAuction.remove(auctionId);
            }
        }
    }

    @OnError
    public void onError(Session session, @PathParam("auctionId") Long auctionId, Throwable throwable) {
        System.err.println("WebSocket error for auction " + auctionId + ": " + throwable.getMessage());
        onClose(session, auctionId);
    }

    public static void broadcastBidUpdate(BidDTO bidDTO) {
        Set<Session> sessions = sessionsByAuction.get(bidDTO.getAuctionId());
        System.out.println("bid update  sessions  ===========    "+sessions);
        if (sessions != null) {
            String message = String.format(
                    "{\"bidId\":%d,\"auctionId\":%d,\"buyerId\":%d,\"itemName\":\"%s\",\"bidAmount\":%.2f,\"bidTime\":\"%s\"}",
                    bidDTO.getBidId(), bidDTO.getAuctionId(), bidDTO.getBuyerId(),
                    bidDTO.getItemName().replace("\"", "\\\""), bidDTO.getBidAmount(), bidDTO.getBidTime()
            );
            System.out.println("bid update    ===========    "+message);
            sessions.removeIf(session -> {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                        return false;
                    } catch (IOException e) {
                        System.err.println("Failed to send WebSocket message: " + e.getMessage());
                        return true;
                    }
                }
                return true;
            });
        }
    }
}