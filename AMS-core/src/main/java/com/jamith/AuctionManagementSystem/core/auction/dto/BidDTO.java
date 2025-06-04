package com.jamith.AuctionManagementSystem.core.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BidDTO implements Serializable {
    private Long bidId;
    private Long auctionId;
    private Long buyerId;
    private String itemName;
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
}