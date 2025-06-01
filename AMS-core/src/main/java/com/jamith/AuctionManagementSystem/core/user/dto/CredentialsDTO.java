package com.jamith.AuctionManagementSystem.core.user.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CredentialsDTO implements Serializable {
    private String email;
    private String password;
}