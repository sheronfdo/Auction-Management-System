package com.jamith.AuctionManagementSystem.user.bean;

import com.jamith.AuctionManagementSystem.core.user.dto.CredentialsDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.ProfileDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.SessionDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.UserDTO;
import com.jamith.AuctionManagementSystem.core.user.exception.UserException;
import com.jamith.AuctionManagementSystem.core.user.remote.UserSessionManagerRemote;
import com.jamith.AuctionManagementSystem.user.entity.SessionEntity;
import com.jamith.AuctionManagementSystem.user.entity.UserEntity;
import com.jamith.AuctionManagementSystem.user.util.HibernateUtil;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
@Stateless
public class UserSessionManagerBean implements UserSessionManagerRemote {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @PostConstruct
    public void init() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM UserEntity u WHERE u.role = :role", Long.class);
            query.setParameter("role", "ADMIN");
            if (query.getSingleResult() == 0) {
                UserEntity admin = new UserEntity();
                admin.setEmail("admin@auction.com");
                admin.setPasswordHash(BCrypt.hashpw("admin123", BCrypt.gensalt()));
                admin.setRole("ADMIN");
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setCreatedAt(LocalDateTime.now());
                session.persist(admin);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public void register(UserDTO user) throws UserException {
        if (!user.getRole().equals("BUYER") && !user.getRole().equals("SELLER")) {
            throw new UserException("Invalid role");
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<UserEntity> query = session.createQuery("FROM UserEntity u WHERE u.email = :email", UserEntity.class);
            query.setParameter("email", user.getEmail());
            if (!query.getResultList().isEmpty()) {
                throw new UserException("Email already registered");
            }
            UserEntity entity = new UserEntity();
            entity.setEmail(user.getEmail());
            entity.setPasswordHash(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            entity.setRole(user.getRole());
            entity.setFirstName(user.getFirstName());
            entity.setLastName(user.getLastName());
            entity.setCreatedAt(LocalDateTime.now());
            session.persist(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public SessionDTO login(CredentialsDTO credentials) throws UserException {
        try (Session session = sessionFactory.openSession()) {
            Query<UserEntity> query = session.createQuery("FROM UserEntity u WHERE u.email = :email", UserEntity.class);
            query.setParameter("email", credentials.getEmail());
            UserEntity user;
            try {
                user = query.getSingleResult();
            } catch (jakarta.persistence.NoResultException e) {
                throw new UserException("User not found");
            }
            if (!BCrypt.checkpw(credentials.getPassword(), user.getPasswordHash())) {
                throw new UserException("Invalid credentials");
            }
            SessionEntity sessionEntity = new SessionEntity();
            sessionEntity.setUser(user);
            sessionEntity.setSessionToken(java.util.UUID.randomUUID().toString());
            sessionEntity.setCreatedAt(LocalDateTime.now());
            sessionEntity.setExpiresAt(LocalDateTime.now().plusMinutes(30));
            session.beginTransaction();
            session.persist(sessionEntity);
            session.getTransaction().commit();
            SessionDTO dto = new SessionDTO();
            dto.setSessionToken(sessionEntity.getSessionToken());
            dto.setUserId(user.getUserId());
            dto.setExpiry(sessionEntity.getExpiresAt());
            return dto;
        }
    }

    @Override
    public void logout(String sessionToken) throws UserException {
        try (Session session = sessionFactory.openSession()) {
            Query<SessionEntity> query = session.createQuery("FROM SessionEntity s WHERE s.sessionToken = :token AND s.expiresAt > :now", SessionEntity.class);
            query.setParameter("token", sessionToken);
            query.setParameter("now", LocalDateTime.now());
            SessionEntity sessionEntity = query.uniqueResult();
            if (sessionEntity == null) {
                throw new UserException("Invalid or expired session");
            }
            session.beginTransaction();
            session.remove(sessionEntity);
            session.getTransaction().commit();
        }
    }

    @Override
    public ProfileDTO getUserProfile(String sessionToken) throws UserException {
        try (Session session = sessionFactory.openSession()) {
            Query<SessionEntity> query = session.createQuery("FROM SessionEntity s WHERE s.sessionToken = :token AND s.expiresAt > :now", SessionEntity.class);
            query.setParameter("token", sessionToken);
            query.setParameter("now", LocalDateTime.now());
            SessionEntity sessionEntity = query.uniqueResult();
            if (sessionEntity == null) {
                throw new UserException("Invalid or expired session");
            }
            UserEntity user = sessionEntity.getUser();
            ProfileDTO profile = new ProfileDTO();
            profile.setUserId(user.getUserId());
            profile.setEmail(user.getEmail());
            profile.setFirstName(user.getFirstName());
            profile.setLastName(user.getLastName());
            profile.setRole(user.getRole());
            return profile;
        }
    }

    @Override
    public void updateProfile(ProfileDTO profile, String sessionToken) throws UserException {
        try (Session session = sessionFactory.openSession()) {
            Query<SessionEntity> query = session.createQuery("FROM SessionEntity s WHERE s.sessionToken = :token AND s.expiresAt > :now", SessionEntity.class);
            query.setParameter("token", sessionToken);
            query.setParameter("now", LocalDateTime.now());
            SessionEntity sessionEntity = query.uniqueResult();
            if (sessionEntity == null) {
                throw new UserException("Invalid or expired session");
            }
            UserEntity user = session.get(UserEntity.class, sessionEntity.getUser().getUserId(), org.hibernate.LockMode.OPTIMISTIC);
            session.beginTransaction();
            if (profile.getEmail() != null && !profile.getEmail().equals(user.getEmail())) {
                Query<UserEntity> emailQuery = session.createQuery("FROM UserEntity u WHERE u.email = :email", UserEntity.class);
                emailQuery.setParameter("email", profile.getEmail());
                if (!emailQuery.getResultList().isEmpty()) {
                    throw new UserException("Email already in use");
                }
                user.setEmail(profile.getEmail());
            }
            if (profile.getFirstName() != null) user.setFirstName(profile.getFirstName());
            if (profile.getLastName() != null) user.setLastName(profile.getLastName());
            if (profile.getPassword() != null) user.setPasswordHash(BCrypt.hashpw(profile.getPassword(), BCrypt.gensalt()));
            user.setUpdatedAt(LocalDateTime.now());
            session.merge(user);
            session.getTransaction().commit();
        } catch (org.hibernate.StaleObjectStateException e) {
            throw new UserException("Profile update failed due to concurrent modification");
        }
    }
}