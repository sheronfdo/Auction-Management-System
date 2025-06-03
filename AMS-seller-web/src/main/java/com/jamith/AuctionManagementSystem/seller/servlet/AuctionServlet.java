package com.jamith.AuctionManagementSystem.seller.servlet;

import com.jamith.AuctionManagementSystem.core.auction.dto.AuctionDTO;
import com.jamith.AuctionManagementSystem.core.auction.exception.AuctionException;
import com.jamith.AuctionManagementSystem.core.auction.remote.AuctionManagerRemote;
import com.jamith.AuctionManagementSystem.core.user.dto.ProfileDTO;
import com.jamith.AuctionManagementSystem.core.user.exception.UserException;
import com.jamith.AuctionManagementSystem.core.user.remote.UserSessionManagerRemote;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
//
//@WebServlet({"/create-auction", "/update-auction"})
//public class AuctionServlet extends HttpServlet {
//    @EJB
//    private UserSessionManagerRemote userSessionManager;
//
//    @EJB
//    private AuctionManagerRemote auctionManager;
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        if ("/update-auction".equals(request.getServletPath())) {
//            HttpSession httpSession = request.getSession(false);
//            if (httpSession == null || httpSession.getAttribute("sessionToken") == null) {
//                response.sendRedirect("login.jsp");
//                return;
//            }
//            String sessionToken = (String) httpSession.getAttribute("sessionToken");
//            try {
//                ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
//                if (!"SELLER".equals(profile.getRole())) {
//                    userSessionManager.logout(sessionToken);
//                    httpSession.invalidate();
//                    response.sendRedirect("login.jsp");
//                    return;
//                }
//                Long auctionId = Long.parseLong(request.getParameter("auctionId"));
//                AuctionDTO auction = auctionManager.getAuctionDetails(auctionId);
//                if (!auction.getSellerId().equals(profile.getUserId())) {
//                    throw new AuctionException("Not authorized to update this auction");
//                }
//                request.setAttribute("auctionId", auction.getAuctionId());
//                request.setAttribute("itemName", auction.getItemName());
//                request.setAttribute("description", auction.getDescription());
//                request.setAttribute("startPrice", auction.getStartPrice());
//                request.setAttribute("bidIncrement", auction.getBidIncrement());
//                request.setAttribute("startTime", auction.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
//                request.setAttribute("endTime", auction.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
//                request.getRequestDispatcher("update-auction.jsp").forward(request, response);
//            } catch (UserException | AuctionException e) {
//                request.setAttribute("error", e.getMessage());
//                request.getRequestDispatcher("seller-profile.jsp").forward(request, response);
//            }
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession httpSession = request.getSession(false);
//        if (httpSession == null || httpSession.getAttribute("sessionToken") == null) {
//            response.sendRedirect("login.jsp");
//            return;
//        }
//        String sessionToken = (String) httpSession.getAttribute("sessionToken");
//        String path = request.getServletPath();
//
//        try {
//            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
//            if (!"SELLER".equals(profile.getRole())) {
//                userSessionManager.logout(sessionToken);
//                httpSession.invalidate();
//                response.sendRedirect("login.jsp");
//                return;
//            }
//
//            AuctionDTO auction = new AuctionDTO();
//            auction.setItemName(request.getParameter("itemName"));
//            auction.setDescription(request.getParameter("description"));
//            String startPriceStr = request.getParameter("startPrice");
//            if (startPriceStr != null && !startPriceStr.isEmpty()) {
//                auction.setStartPrice(new BigDecimal(startPriceStr));
//            }
//            String bidIncrementStr = request.getParameter("bidIncrement");
//            if (bidIncrementStr != null && !bidIncrementStr.isEmpty()) {
//                auction.setBidIncrement(new BigDecimal(bidIncrementStr));
//            }
//            String startTimeStr = request.getParameter("startTime");
//            if (startTimeStr != null && !startTimeStr.isEmpty()) {
//                auction.setStartTime(LocalDateTime.parse(startTimeStr));
//            }
//            String endTimeStr = request.getParameter("endTime");
//            if (endTimeStr != null && !endTimeStr.isEmpty()) {
//                auction.setEndTime(LocalDateTime.parse(endTimeStr));
//            }
//
//            if ("/create-auction".equals(path)) {
//                auction.setSellerId(profile.getUserId());
//                auctionManager.createAuction(auction, sessionToken);
//                response.sendRedirect("seller-profile");
//            } else if ("/update-auction".equals(path)) {
//                auction.setAuctionId(Long.parseLong(request.getParameter("auctionId")));
//                auctionManager.updateAuction(auction, sessionToken);
//                response.sendRedirect("seller-profile");
//            }
//        } catch (UserException | AuctionException | NumberFormatException | DateTimeParseException e) {
//            request.setAttribute("error", e.getMessage());
//            request.getRequestDispatcher(path.substring(1) + ".jsp").forward(request, response);
//        }
//    }
//}



@WebServlet({"/create-auction", "/update-auction", "/delete-auction", "/list-auctions"})
public class AuctionServlet extends HttpServlet {
    @EJB
    private UserSessionManagerRemote userSessionManager;

    @EJB
    private AuctionManagerRemote auctionManager;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        String sessionToken = (httpSession != null) ? (String) httpSession.getAttribute("sessionToken") : null;
        String path = request.getServletPath();

        if (sessionToken == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"SELLER".equals(profile.getRole())) {
                userSessionManager.logout(sessionToken);
                httpSession.invalidate();
                response.sendRedirect("login.jsp");
                return;
            }

            if ("/list-auctions".equals(path)) {
                request.setAttribute("auctions", auctionManager.listSellerAuctions(sessionToken));
                request.getRequestDispatcher("list-auctions.jsp").forward(request, response);
            } else if ("/update-auction".equals(path)) {
                Long auctionId = Long.parseLong(request.getParameter("auctionId"));
                AuctionDTO auction = auctionManager.getAuctionDetails(auctionId);
                if (!auction.getSellerId().equals(profile.getUserId())) {
                    throw new AuctionException("Not authorized to update this auction");
                }
                request.setAttribute("auctionId", auction.getAuctionId());
                request.setAttribute("itemName", auction.getItemName());
                request.setAttribute("description", auction.getDescription());
                request.setAttribute("startPrice", auction.getStartPrice());
                request.setAttribute("bidIncrement", auction.getBidIncrement());
                request.setAttribute("startTime", auction.getStartTime().format(FORMATTER));
                request.setAttribute("endTime", auction.getEndTime().format(FORMATTER));
                request.getRequestDispatcher("update-auction.jsp").forward(request, response);
            }
        } catch (UserException | AuctionException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        String sessionToken = (httpSession != null) ? (String) httpSession.getAttribute("sessionToken") : null;
        String path = request.getServletPath();

        if (sessionToken == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"SELLER".equals(profile.getRole())) {
                userSessionManager.logout(sessionToken);
                httpSession.invalidate();
                response.sendRedirect("login.jsp");
                return;
            }

            if ("/create-auction".equals(path) || "/update-auction".equals(path)) {
                AuctionDTO auction = new AuctionDTO();
                auction.setItemName(request.getParameter("itemName"));
                auction.setDescription(request.getParameter("description"));
                String startPriceStr = request.getParameter("startPrice");
                if (startPriceStr != null && !startPriceStr.isEmpty()) {
                    auction.setStartPrice(new BigDecimal(startPriceStr));
                }
                String bidIncrementStr = request.getParameter("bidIncrement");
                if (bidIncrementStr != null && !bidIncrementStr.isEmpty()) {
                    auction.setBidIncrement(new BigDecimal(bidIncrementStr));
                }
                String startTimeStr = request.getParameter("startTime");
                if (startTimeStr != null && !startTimeStr.isEmpty()) {
                    auction.setStartTime(LocalDateTime.parse(startTimeStr, FORMATTER));
                }
                String endTimeStr = request.getParameter("endTime");
                if (endTimeStr != null && !endTimeStr.isEmpty()) {
                    auction.setEndTime(LocalDateTime.parse(endTimeStr, FORMATTER));
                }

                if ("/create-auction".equals(path)) {
                    auction.setSellerId(profile.getUserId());
                    auctionManager.createAuction(auction, sessionToken);
                } else {
                    auction.setAuctionId(Long.parseLong(request.getParameter("auctionId")));
                    auctionManager.updateAuction(auction, sessionToken);
                }
                response.sendRedirect("seller-profile");
            } else if ("/delete-auction".equals(path)) {
                Long auctionId = Long.parseLong(request.getParameter("auctionId"));
                auctionManager.deleteAuction(auctionId, sessionToken);
                response.sendRedirect("list-auctions");
            }
        } catch (UserException | AuctionException | NumberFormatException | DateTimeParseException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}