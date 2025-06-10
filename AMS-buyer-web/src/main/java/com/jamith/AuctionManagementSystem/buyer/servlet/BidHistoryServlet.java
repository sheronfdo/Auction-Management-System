package com.jamith.AuctionManagementSystem.buyer.servlet;

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
import java.util.List;

@WebServlet("/bid-history")
public class BidHistoryServlet extends HttpServlet {
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

        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"BUYER".equals(profile.getRole())) {
                userSessionManager.logout(sessionToken);
                httpSession.invalidate();
                response.sendRedirect("login.jsp");
                return;
            }
            List<BidDTO> bids = auctionManager.getBidHistory(sessionToken);
            request.setAttribute("bids", bids);
            request.getRequestDispatcher("bid-history.jsp").forward(request, response);
        } catch (UserException | AuctionException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}