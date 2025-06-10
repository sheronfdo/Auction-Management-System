package com.jamith.AuctionManagementSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "bids")
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class BidEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private AuctionEntity auction;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private UserEntity buyer;

    @Column(nullable = false)
    private BigDecimal bidAmount;

    @Column(nullable = false)
    private LocalDateTime bidTime;
}