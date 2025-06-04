package com.jamith.AuctionManagementSystem.auction.bean;


import com.jamith.AuctionManagementSystem.auction.util.HibernateUtil;
import com.jamith.AuctionManagementSystem.entity.AuctionEntity;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDateTime;

@Singleton
public class AuctionTimerBean {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Schedule(hour = "*", minute = "*/1", persistent = false) // Run every 1 minute
    public void updateAuctionStatuses() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            System.out.println("Updating auction statuses..."+LocalDateTime.now());
            // PENDING -> ACTIVE
            Query<AuctionEntity> activateQuery = session.createQuery(
                    "FROM AuctionEntity a WHERE a.status = :status AND a.startTime <= :now AND a.endTime > :now",
                    AuctionEntity.class
            );
            activateQuery.setParameter("status", "PENDING");
            activateQuery.setParameter("now", LocalDateTime.now());
            for (AuctionEntity entity : activateQuery.getResultList()) {
                entity.setStatus("ACTIVE");
                entity.setUpdatedAt(LocalDateTime.now());
                session.merge(entity);
            }

            // ACTIVE -> CLOSED
            Query<AuctionEntity> closeQuery = session.createQuery(
                    "FROM AuctionEntity a WHERE a.status = :status AND a.endTime <= :now",
                    AuctionEntity.class
            );
            closeQuery.setParameter("status", "ACTIVE");
            closeQuery.setParameter("now", LocalDateTime.now());
            for (AuctionEntity entity : closeQuery.getResultList()) {
                entity.setStatus("CLOSED");
                entity.setUpdatedAt(LocalDateTime.now());
                session.merge(entity);
                // TODO: Publish JMS message to AuctionUpdatesTopic
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Failed to update auction statuses: " + e.getMessage());
        }
    }
}