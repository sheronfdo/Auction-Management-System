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
public class AuctionSummaryDTO implements Serializable {
    private Long auctionId;
    private String itemName;
    private BigDecimal currentBid;
    private LocalDateTime endTime;
    private String status;
}