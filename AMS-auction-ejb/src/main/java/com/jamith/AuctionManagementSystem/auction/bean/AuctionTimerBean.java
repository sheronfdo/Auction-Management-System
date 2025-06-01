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

    @Schedule(hour = "*", minute = "*", second = "*/30", persistent = false)
    public void checkExpiredAuctions() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<AuctionEntity> query = session.createQuery(
                "FROM AuctionEntity a WHERE a.status = :status AND a.endTime <= :now",
                AuctionEntity.class
            );
            query.setParameter("status", "ACTIVE");
            query.setParameter("now", LocalDateTime.now());
            for (AuctionEntity entity : query.getResultList()) {
                entity.setStatus("CLOSED");
                entity.setUpdatedAt(LocalDateTime.now());
                session.merge(entity);
                // TODO: Publish JMS message to AuctionUpdatesTopic
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            // Log error, avoid interrupting timer
        }
    }
}