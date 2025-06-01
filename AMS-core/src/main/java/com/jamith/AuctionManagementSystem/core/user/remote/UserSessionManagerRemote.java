package com.jamith.AuctionManagementSystem.core.user.remote;

import com.jamith.AuctionManagementSystem.core.user.dto.CredentialsDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.ProfileDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.SessionDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.UserDTO;
import com.jamith.AuctionManagementSystem.core.user.exception.UserException;
import jakarta.ejb.Remote;

@Remote
public interface UserSessionManagerRemote {
    void register(UserDTO user) throws UserException;
    SessionDTO login(CredentialsDTO credentials) throws UserException;
    void logout(String sessionToken) throws UserException;
    ProfileDTO getUserProfile(String sessionToken) throws UserException;
    void updateProfile(ProfileDTO profile, String sessionToken) throws UserException;
}