package com.jamith.AuctionManagementSystem.core.user.exception;

public class UserException extends Exception {
    public UserException(String message) {
        super(message);
        System.out.println(message);
    }
    public UserException(String message, Throwable cause) {
        super(message, cause);
        System.out.println(message);
    }
}