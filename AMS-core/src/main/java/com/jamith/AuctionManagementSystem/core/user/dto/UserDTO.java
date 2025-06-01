package com.jamith.AuctionManagementSystem.core.user.dto;

import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO implements Serializable {
    private Long userId;
    private String email;
    private String role; // BUYER, SELLER
    private String firstName;
    private String lastName;
    private String password;
    }