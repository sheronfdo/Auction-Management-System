package com.jamith.AuctionManagementSystem.ejb.remote;

import com.jamith.AuctionManagementSystem.core.model.User;
import jakarta.ejb.Remote;

@Remote
public interface UserService {
    User getUserById(int id);
}
