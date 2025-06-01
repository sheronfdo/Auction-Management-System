package com.jamith.AuctionManagementSystem.core.user.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProfileDTO implements Serializable {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
}