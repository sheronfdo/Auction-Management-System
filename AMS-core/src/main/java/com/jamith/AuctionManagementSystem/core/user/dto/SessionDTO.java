package com.jamith.AuctionManagementSystem.core.user.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SessionDTO implements Serializable {
    private String sessionToken;
    private Long userId;
    private LocalDateTime expiry;
}