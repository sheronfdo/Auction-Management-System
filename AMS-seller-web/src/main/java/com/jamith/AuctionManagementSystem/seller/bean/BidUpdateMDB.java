package com.jamith.AuctionManagementSystem.seller.bean;

import com.jamith.AuctionManagementSystem.core.auction.dto.BidDTO;
import com.jamith.AuctionManagementSystem.seller.socket.BidUpdateWebSocket;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Topic"),
                @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/BidNotification")
        }
)
public class BidUpdateMDB implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage objMessage = (ObjectMessage) message;
                Object obj = objMessage.getObject();
                if (obj instanceof BidDTO) {
                    BidDTO bidDTO = (BidDTO) obj;
                    System.out.println("MDB received bid: " + bidDTO.toString()); // Debug
                    BidUpdateWebSocket.broadcastBidUpdate(bidDTO);
                }
            }
        } catch (JMSException e) {
            System.err.println("Failed to process JMS message: " + e.getMessage());
        }
    }
}