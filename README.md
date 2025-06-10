# Auction Management System

This is the **Auction Management System**, a distributed online auction platform developed for the BCD I Assignment. The system enables real-time bidding and auction management, supporting high concurrency with a modular Java EE architecture. The project is hosted on [GitHub](https://github.com/sheronfdo/Auction-Management-System).

## Project Overview
The Auction Management System allows buyers to bid on auctions, sellers to manage auctions, and admins to oversee operations. It leverages **Enterprise JavaBeans (EJB)**, **Java Message Service (JMS)**, **Contexts and Dependency Injection (CDI)**, **JPA**, and **WebSocket** for scalability, real-time updates, and reliability.

## Features
- **User Authentication**: Secure login/logout for buyers, sellers, and admins.
- **Buyer Features**:
  - View active auctions.
  - Place bids with real-time updates via WebSocket.
  - View auction details and bid history.
- **Seller Features**:
  - Create, update, and delete auctions.
  - View and manage auction listings.
  - Monitor bids in real-time via WebSocket.
  - Update profile details.
- **Admin Features**:
  - Manage users (buyers/sellers).
  - Oversee auctions and resolve disputes.
- **Real-Time Updates**: Bid notifications broadcast via JMS and WebSocket.
- **Bid Validation**: Ensures bids exceed current highest bid and auction status.
- **Automatic Bid Increments**: Configurable increments for auctions.
- **Scalability**: Supports 2000 concurrent users with <1% error rate.
- **Data Persistence**: Stores users, auctions, and bids in MySQL.

## Modular Architecture
The project follows a multi-module Maven structure, ensuring separation of concerns and reusability. Modules are:

- **AMS-core**: Shared utilities and common code for cross-module use.
- **AMS-entity**: JPA entities (`User`, `Auction`, `Bid`) for data modeling.
- **AMS-user-ejb**: EJB module for user management (`UserService`) handling authentication and profiles.
- **AMS-auction-ejb**: EJB module for auction and bid logic (`AuctionService`, `BidService`), including JMS integration.
- **AMS-buyer-web**: Web module for buyer UI (JSPs, servlets) and WebSocket for bid updates.
- **AMS-seller-web**: Web module for seller UI (JSPs, servlets) and auction management.
- **AMS-admin-web**: Web module for admin UI (JSPs, servlets) for user and auction oversight.
- **AMS-ear**: Enterprise archive packaging all modules for deployment on Payara.

## Technologies
- **Backend**: Java EE (Java 11), EJB, JMS, CDI, JPA
- **Frontend**: JSP, Servlets, WebSocket
- **Server**: Payara 6.2025.5
- **Database**: MySQL
- **Build**: Maven
- **Testing**: JUnit 5.9.2

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/sheronfdo/Auction-Management-System.git
   cd Auction-Management-System
   ```
2. Configure MySQL:
   - Create database: `CREATE DATABASE auction_db;`
   - Update `persistence.xml` with credentials.
3. Configure Payara:
   - Start Payara: `./payara6/bin/asadmin start-domain`
   - Create JMS Queue: `./payara6/bin/asadmin create-jms-resource --restype jakarta.jms.Queue jms/BidNotification`
4. Build and deploy:
   ```bash
   mvn clean install
   ./payara6/bin/asadmin deploy AMS-ear/target/AuctionManagementSystem-1.0.ear
   ```
5. Access:
   - Buyer: `http://localhost:8080/AMS-buyer-web`
   - Seller: `http://localhost:8080/AMS-seller-web`
   - Admin: `http://localhost:8080/AMS-admin-web`

## Usage
- **Buyers**: Login, browse auctions, bid on items.
- **Sellers**: Login, create auctions, manage profiles.
- **Admins**: Login, manage users and auctions.

## License
MIT License
