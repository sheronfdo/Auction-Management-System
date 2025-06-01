package com.jamith.AuctionManagementSystem.core.auction.exception;

import java.io.Serializable;

public class AuctionException extends Exception implements Serializable {
    public AuctionException(String message) {
        super(message);
    }
}