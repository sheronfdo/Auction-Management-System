package com.jamith.AuctionManagementSystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "auctions")
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class AuctionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Long auctionId;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity seller;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column
    private String description;

    @Column(name = "start_price", nullable = false)
    private BigDecimal startPrice;

    @Column(name = "bid_increment", nullable = false)
    private BigDecimal bidIncrement;

    @Column(name = "current_bid")
    private BigDecimal currentBid;

    @Column(nullable = false)
    private String status;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @OneToMany(mappedBy = "auction")
    private List<BidEntity> bids;
}