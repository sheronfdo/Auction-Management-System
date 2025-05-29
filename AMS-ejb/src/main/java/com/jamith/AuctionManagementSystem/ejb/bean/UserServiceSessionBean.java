package com.jamith.AuctionManagementSystem.ejb.bean;

import com.jamith.AuctionManagementSystem.core.model.User;
import com.jamith.AuctionManagementSystem.ejb.remote.UserService;
import jakarta.ejb.Stateless;

@Stateless
public class UserServiceSessionBean implements UserService {
    @Override
    public User getUserById(int id) {
        return new User(15, "Jamith", "Sheron", "jamithsheron5@gmail.com", "123");
    }
}
