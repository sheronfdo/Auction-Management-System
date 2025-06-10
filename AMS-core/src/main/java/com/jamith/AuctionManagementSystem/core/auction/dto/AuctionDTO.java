package com.jamith.AuctionManagementSystem.core.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionDTO implements Serializable {
    private Long auctionId;
    private Long sellerId;
    private String itemName;
    private String description;
    private BigDecimal startPrice;
    private BigDecimal bidIncrement;
    private BigDecimal currentBid;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}