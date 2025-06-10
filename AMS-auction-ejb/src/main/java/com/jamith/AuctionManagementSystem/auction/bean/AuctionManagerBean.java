package com.jamith.AuctionManagementSystem.auction.bean;


import com.jamith.AuctionManagementSystem.auction.util.HibernateUtil;
import com.jamith.AuctionManagementSystem.core.auction.dto.AuctionDTO;
import com.jamith.AuctionManagementSystem.core.auction.dto.AuctionSummaryDTO;
import com.jamith.AuctionManagementSystem.core.auction.dto.BidDTO;
import com.jamith.AuctionManagementSystem.core.auction.exception.AuctionException;
import com.jamith.AuctionManagementSystem.core.auction.remote.AuctionManagerRemote;
import com.jamith.AuctionManagementSystem.core.user.dto.ProfileDTO;
import com.jamith.AuctionManagementSystem.core.user.exception.UserException;
import com.jamith.AuctionManagementSystem.core.user.remote.UserSessionManagerRemote;
import com.jamith.AuctionManagementSystem.entity.AuctionEntity;
import com.jamith.AuctionManagementSystem.entity.BidEntity;
import com.jamith.AuctionManagementSystem.entity.UserEntity;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Topic;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class AuctionManagerBean implements AuctionManagerRemote {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @EJB
    private UserSessionManagerRemote userSessionManager;

    @Resource(lookup = "jms/BidNotificationConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/BidNotification")
    private Topic auctionUpdatesTopic;

    @Override
    public void createAuction(AuctionDTO auction, String sessionToken) throws AuctionException {
        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if ("BUYER".equals(profile.getRole())) {
                throw new AuctionException("Only admin or sellers can create auctions");
            }
            if (auction.getItemName() == null || auction.getStartPrice() == null ||
                    auction.getBidIncrement() == null || auction.getStartTime() == null ||
                    auction.getEndTime() == null) {
                throw new AuctionException("Required auction fields are missing");
            }
            if (auction.getStartTime().isBefore(LocalDateTime.now()) ||
                    auction.getEndTime().isBefore(auction.getStartTime())) {
                throw new AuctionException("Invalid start or end time");
            }
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                UserEntity seller = session.get(UserEntity.class, profile.getUserId());
                AuctionEntity entity = new AuctionEntity();
                entity.setSeller(seller);
                entity.setItemName(auction.getItemName());
                entity.setDescription(auction.getDescription());
                entity.setStartPrice(auction.getStartPrice());
                entity.setBidIncrement(auction.getBidIncrement());
                entity.setStatus("PENDING");
                entity.setStartTime(auction.getStartTime());
                entity.setEndTime(auction.getEndTime());
                entity.setCreatedAt(LocalDateTime.now());
                session.persist(entity);
                session.getTransaction().commit();
            }
        } catch (UserException e) {
            throw new AuctionException("Invalid session: " + e.getMessage());
        }
    }

    @Override
    public void updateAuction(AuctionDTO auction, String sessionToken) throws AuctionException {
        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"SELLER".equals(profile.getRole())) {
                throw new AuctionException("Only sellers can update auctions");
            }
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                AuctionEntity entity = session.get(AuctionEntity.class, auction.getAuctionId(), org.hibernate.LockMode.OPTIMISTIC);
                if (entity == null) {
                    throw new AuctionException("Auction not found");
                }
                if (!entity.getSeller().getUserId().equals(profile.getUserId())) {
                    throw new AuctionException("Not authorized to update this auction");
                }
                if (!"PENDING".equals(entity.getStatus())) {
                    throw new AuctionException("Only pending auctions can be updated");
                }
                if (auction.getItemName() != null) entity.setItemName(auction.getItemName());
                if (auction.getDescription() != null) entity.setDescription(auction.getDescription());
                if (auction.getStartPrice() != null) entity.setStartPrice(auction.getStartPrice());
                if (auction.getBidIncrement() != null) entity.setBidIncrement(auction.getBidIncrement());
                if (auction.getStartTime() != null && auction.getStartTime().isAfter(LocalDateTime.now())) {
                    entity.setStartTime(auction.getStartTime());
                }
                if (auction.getEndTime() != null && auction.getEndTime().isAfter(entity.getStartTime())) {
                    entity.setEndTime(auction.getEndTime());
                }
                entity.setUpdatedAt(LocalDateTime.now());
                session.merge(entity);
                session.getTransaction().commit();
            } catch (org.hibernate.StaleObjectStateException e) {
                throw new AuctionException("Auction update failed due to concurrent modification");
            }
        } catch (UserException e) {
            throw new AuctionException("Invalid session: " + e.getMessage());
        }
    }

    @Override
    public void deleteAuction(Long auctionId, String sessionToken) throws AuctionException {
        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"SELLER".equals(profile.getRole())) {
                throw new AuctionException("Only sellers can delete auctions");
            }
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                AuctionEntity entity = session.get(AuctionEntity.class, auctionId, org.hibernate.LockMode.OPTIMISTIC);
                if (entity == null) {
                    throw new AuctionException("Auction not found");
                }
                if (!entity.getSeller().getUserId().equals(profile.getUserId())) {
                    throw new AuctionException("Not authorized to delete this auction");
                }
                if (!"PENDING".equals(entity.getStatus())) {
                    throw new AuctionException("Only pending auctions can be deleted");
                }
                session.remove(entity);
                session.getTransaction().commit();
            } catch (org.hibernate.StaleObjectStateException e) {
                throw new AuctionException("Auction deletion failed due to concurrent modification");
            }
        } catch (UserException e) {
            throw new AuctionException("Invalid session: " + e.getMessage());
        }
    }

    @Override
    public List<AuctionSummaryDTO> listSellerAuctions(String sessionToken) throws AuctionException {
        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"SELLER".equals(profile.getRole())) {
                throw new AuctionException("Only sellers can list their auctions");
            }
            try (Session session = sessionFactory.openSession()) {
                Query<AuctionEntity> query = session.createQuery(
                        "FROM AuctionEntity a WHERE a.seller.userId = :sellerId",
                        AuctionEntity.class
                );
                query.setParameter("sellerId", profile.getUserId());
                List<AuctionEntity> entities = query.getResultList();
                return entities.stream().map(e -> {
                    AuctionSummaryDTO dto = new AuctionSummaryDTO();
                    dto.setAuctionId(e.getAuctionId());
                    dto.setItemName(e.getItemName());
                    dto.setCurrentBid(e.getCurrentBid() != null ? e.getCurrentBid() : e.getStartPrice());
                    dto.setStatus(e.getStatus());
                    dto.setEndTime(e.getEndTime());
                    return dto;
                }).collect(Collectors.toList());
            }
        } catch (UserException e) {
            throw new AuctionException("Invalid session: " + e.getMessage());
        }
    }

    @Override
    public void closeAuction(Long auctionId, String sessionToken) throws AuctionException {
        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"ADMIN".equals(profile.getRole())) {
                throw new AuctionException("Only admins can close auctions manually");
            }
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                AuctionEntity entity = session.get(AuctionEntity.class, auctionId, org.hibernate.LockMode.OPTIMISTIC);
                if (entity == null) {
                    throw new AuctionException("Auction not found");
                }
                if ("CLOSED".equals(entity.getStatus())) {
                    throw new AuctionException("Auction already closed");
                }
                entity.setStatus("CLOSED");
                entity.setUpdatedAt(LocalDateTime.now());
                session.merge(entity);
                session.getTransaction().commit();
            } catch (org.hibernate.StaleObjectStateException e) {
                throw new AuctionException("Auction closure failed due to concurrent modification");
            }
        } catch (UserException e) {
            throw new AuctionException("Invalid session: " + e.getMessage());
        }
    }

    @Override
    public List<AuctionSummaryDTO> listActiveAuctions() throws AuctionException {
        try (Session session = sessionFactory.openSession()) {
            Query<AuctionEntity> query = session.createQuery(
                    "FROM AuctionEntity a WHERE a.status = :status AND a.startTime <= :now AND a.endTime > :now",
                    AuctionEntity.class
            );
            query.setParameter("status", "ACTIVE");
            query.setParameter("now", LocalDateTime.now());
            List<AuctionEntity> entities = query.getResultList();
            return entities.stream().map(e -> {
                AuctionSummaryDTO dto = new AuctionSummaryDTO();
                dto.setAuctionId(e.getAuctionId());
                dto.setItemName(e.getItemName());
                dto.setCurrentBid(e.getCurrentBid() != null ? e.getCurrentBid() : e.getStartPrice());
                dto.setEndTime(e.getEndTime());
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new AuctionException("Failed to list auctions: " + e.getMessage());
        }
    }

    @Override
    public AuctionDTO getAuctionDetails(Long auctionId) throws AuctionException {
        try (Session session = sessionFactory.openSession()) {
            AuctionEntity entity = session.get(AuctionEntity.class, auctionId);
            if (entity == null) {
                throw new AuctionException("Auction not found");
            }
            AuctionDTO dto = new AuctionDTO();
            dto.setAuctionId(entity.getAuctionId());
            dto.setSellerId(entity.getSeller().getUserId());
            dto.setItemName(entity.getItemName());
            dto.setDescription(entity.getDescription());
            dto.setStartPrice(entity.getStartPrice());
            dto.setBidIncrement(entity.getBidIncrement());
            dto.setCurrentBid(entity.getCurrentBid());
            dto.setStatus(entity.getStatus());
            dto.setStartTime(entity.getStartTime());
            dto.setEndTime(entity.getEndTime());
            return dto;
        } catch (Exception e) {
            throw new AuctionException("Failed to retrieve auction details: " + e.getMessage());
        }
    }

    @Override
    public void placeBid(Long auctionId, BigDecimal bidAmount, String sessionToken) throws AuctionException {
        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"BUYER".equals(profile.getRole())) {
                throw new AuctionException("Only buyers can place bids");
            }
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                AuctionEntity auction = session.find(AuctionEntity.class, auctionId);
                if (auction == null) {
                    throw new AuctionException("Auction not found");
                }
                if (!"ACTIVE".equals(auction.getStatus())) {
                    throw new AuctionException("Auction is not active");
                }
                if (LocalDateTime.now().isAfter(auction.getEndTime())) {
                    throw new AuctionException("Auction has ended");
                }
                BigDecimal minimumBid = auction.getCurrentBid() != null
                        ? auction.getCurrentBid().add(auction.getBidIncrement())
                        : auction.getStartPrice().add(auction.getBidIncrement());
                if (bidAmount.compareTo(minimumBid) < 0) {
                    throw new AuctionException("Bid must be at least " + minimumBid);
                }
                UserEntity buyer = session.find(UserEntity.class, profile.getUserId());
                if (buyer == null) {
                    throw new AuctionException("Buyer not found");
                }
                BidEntity bid = new BidEntity();
                bid.setAuction(auction);
                bid.setBuyer(buyer);
                bid.setBidAmount(bidAmount);
                bid.setBidTime(LocalDateTime.now());
                auction.setCurrentBid(bidAmount);
                session.persist(bid);
                session.merge(auction);
                session.getTransaction().commit();

                try (JMSContext context = connectionFactory.createContext()) {
                    BidDTO bidDTO = new BidDTO();
                    bidDTO.setBidId(bid.getBidId());
                    bidDTO.setAuctionId(auctionId);
                    bidDTO.setBuyerId(buyer.getUserId());
                    bidDTO.setItemName(auction.getItemName());
                    bidDTO.setBidAmount(bidAmount);
                    bidDTO.setBidTime(bid.getBidTime());
                    ObjectMessage message = context.createObjectMessage(bidDTO);
                    message.setObject(bidDTO);
                    context.createProducer().send(auctionUpdatesTopic, message);
                    System.out.println("Bid notification send " + message.toString());
                } catch (Exception e) {
                    System.err.println("Failed to publish JMS message: " + e.getMessage());
                }
            }
        } catch (UserException e) {
            throw new AuctionException("Invalid session: " + e.getMessage());
        } catch (Exception e) {
            throw new AuctionException("Failed to place bid: " + e.getMessage());
        }
    }

    @Override
    public List<BidDTO> getBidHistory(String sessionToken) throws AuctionException {
        try (Session session = sessionFactory.openSession()) {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"BUYER".equals(profile.getRole())) {
                throw new AuctionException("Only buyers can view bid history");
            }
            TypedQuery<BidEntity> query = session.createQuery(
                    "SELECT b FROM BidEntity b WHERE b.buyer.userId = :buyerId ORDER BY b.bidTime DESC",
                    BidEntity.class
            );
            query.setParameter("buyerId", profile.getUserId());
            List<BidEntity> bids = query.getResultList();
            return bids.stream().map(b -> {
                BidDTO dto = new BidDTO();
                dto.setBidId(b.getBidId());
                dto.setAuctionId(b.getAuction().getAuctionId());
                dto.setBuyerId(b.getBuyer().getUserId());
                dto.setItemName(b.getAuction().getItemName());
                dto.setBidAmount(b.getBidAmount());
                dto.setBidTime(b.getBidTime());
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new AuctionException("Failed to list auctions: " + e.getMessage());
        }
    }

    @Override
    public List<AuctionDTO> getAllAuctions() throws AuctionException {
        try (Session session = sessionFactory.openSession()) {
            Query<AuctionEntity> query = session.createQuery("FROM AuctionEntity a ORDER BY a.createdAt DESC", AuctionEntity.class
            );
            List<AuctionEntity> auctionEntities = query.getResultList();
            return auctionEntities.stream().map(this::mapToAuctionDTO).map(e -> {
                AuctionDTO dto = new AuctionDTO();
                dto.setAuctionId(e.getAuctionId());
                dto.setSellerId(e.getSellerId());
                dto.setItemName(e.getItemName());
                dto.setDescription(dto.getDescription());
                dto.setStartPrice(e.getStartPrice());
                dto.setBidIncrement(e.getBidIncrement());
                dto.setCurrentBid(e.getCurrentBid());
                dto.setStatus(e.getStatus());
                dto.setStartTime(e.getStartTime());
                dto.setEndTime(e.getEndTime());
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new AuctionException("Failed to retrieve auctions: " + e.getMessage());
        }
    }

    @Override
    public List<BidDTO> getBidsForAuction(Long auctionId) throws AuctionException {
        try (Session session = sessionFactory.openSession()) {
            AuctionEntity auction = session.get(AuctionEntity.class, auctionId);
            if (auction == null) {
                throw new AuctionException("Auction not found: " + auctionId);
            }
            Query<BidEntity> query = session.createQuery("SELECT b FROM BidEntity b WHERE b.auction.auctionId = :auctionId ORDER BY b.bidTime DESC", BidEntity.class);
            query.setParameter("auctionId", auctionId);
            List<BidEntity> bids = query.getResultList();
            return bids.stream().map(b -> {
                BidDTO dto = new BidDTO();
                dto.setBidId(b.getBidId());
                dto.setAuctionId(b.getAuction().getAuctionId());
                dto.setBuyerId(b.getBuyer().getUserId());
                dto.setItemName(b.getAuction().getItemName());
                dto.setBidAmount(b.getBidAmount());
                dto.setBidTime(b.getBidTime());
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new AuctionException("Failed to retrieve bids for auction: " + auctionId + ": " + e.getMessage());
        }
    }

    private AuctionDTO mapToAuctionDTO(AuctionEntity entity) {
        AuctionDTO dto = new AuctionDTO();
        dto.setAuctionId(entity.getAuctionId());
        dto.setSellerId(entity.getSeller().getUserId());
        dto.setItemName(entity.getItemName());
        dto.setDescription(entity.getDescription());
        dto.setStartPrice(entity.getStartPrice());
        dto.setBidIncrement(entity.getBidIncrement());
        dto.setCurrentBid(entity.getCurrentBid());
        dto.setStatus(entity.getStatus());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        return dto;
    }
}