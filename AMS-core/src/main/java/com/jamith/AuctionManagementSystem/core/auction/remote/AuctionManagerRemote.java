package com.jamith.AuctionManagementSystem.core.auction.remote;

import com.jamith.AuctionManagementSystem.core.auction.dto.AuctionDTO;
import com.jamith.AuctionManagementSystem.core.auction.dto.AuctionSummaryDTO;
import com.jamith.AuctionManagementSystem.core.auction.exception.AuctionException;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface AuctionManagerRemote {
    void createAuction(AuctionDTO auction, String sessionToken) throws AuctionException;
    void updateAuction(AuctionDTO auction, String sessionToken) throws AuctionException;
    void deleteAuction(Long auctionId, String sessionToken) throws AuctionException;
    List<AuctionSummaryDTO> listSellerAuctions(String sessionToken) throws AuctionException;
    void closeAuction(Long auctionId, String sessionToken) throws AuctionException;
    List<AuctionSummaryDTO> listActiveAuctions() throws AuctionException;
    AuctionDTO getAuctionDetails(Long auctionId) throws AuctionException;
}