package com.jamith.AuctionManagementSystem.admin.servlet;

import com.jamith.AuctionManagementSystem.core.auction.dto.AuctionDTO;
import com.jamith.AuctionManagementSystem.core.auction.dto.BidDTO;
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
import java.util.List;

@WebServlet({"/close-auction", "/create-auction", "/auction-details", "/delete-auction"})
public class AuctionServlet extends HttpServlet {
    @EJB
    private UserSessionManagerRemote userSessionManager;

    @EJB
    private AuctionManagerRemote auctionManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null || httpSession.getAttribute("sessionToken") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String sessionToken = (String) httpSession.getAttribute("sessionToken");
        String path = request.getServletPath();
        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"ADMIN".equals(profile.getRole())) {
                userSessionManager.logout(sessionToken);
                httpSession.invalidate();
                response.sendRedirect("login.jsp");
                return;
            }
            if ("/auction-details".equals(path)) {
                Long auctionId = Long.parseLong(request.getParameter("auctionId"));
                AuctionDTO auction = auctionManager.getAuctionDetails(auctionId);
                List<BidDTO> bids = auctionManager.getBidsForAuction(auctionId);
                request.setAttribute("auction", auction);
                request.setAttribute("bids", bids);
                request.getRequestDispatcher("auction-details.jsp").forward(request, response);
            } else if ("/create-auction".equals(path)) {
                request.getRequestDispatcher("create-auction.jsp").forward(request, response);
            }
        } catch (UserException | AuctionException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null || httpSession.getAttribute("sessionToken") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String sessionToken = (String) httpSession.getAttribute("sessionToken");
        String path = request.getServletPath();
        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"ADMIN".equals(profile.getRole())) {
                userSessionManager.logout(sessionToken);
                httpSession.invalidate();
                response.sendRedirect("login.jsp");
                return;
            }
            if ("/close-auction".equals(path)) {
                Long auctionId = Long.parseLong(request.getParameter("auctionId"));
                auctionManager.closeAuction(auctionId, sessionToken);
                response.sendRedirect("dashboard");
            } else if ("/create-auction".equals(path)) {
                AuctionDTO auction = new AuctionDTO();
                auction.setItemName(request.getParameter("itemName"));
                auction.setDescription(request.getParameter("description"));
                auction.setStartPrice(new BigDecimal(request.getParameter("startPrice")));
                auction.setBidIncrement(new BigDecimal(request.getParameter("bidIncrement")));
                auction.setStartTime(LocalDateTime.parse(request.getParameter("startTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
                auction.setEndTime(LocalDateTime.parse(request.getParameter("endTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
                auctionManager.createAuction(auction, sessionToken);
                response.sendRedirect("dashboard");
            } else if ("/delete-auction".equals(path)) {
                Long auctionId = Long.parseLong(request.getParameter("auctionId"));
                auctionManager.deleteAuction(auctionId, sessionToken);
                response.sendRedirect("dashboard");
            }
        } catch (UserException | AuctionException | NumberFormatException | java.time.format.DateTimeParseException e) {
            request.setAttribute("error", e.getMessage());
            String jsp = "/close-auction".equals(path) ? "close-auction.jsp" : "create-auction.jsp";
            request.getRequestDispatcher(jsp).forward(request, response);
        }
    }
}