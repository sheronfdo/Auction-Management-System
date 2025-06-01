package com.jamith.AuctionManagementSystem.buyer.servlet;

import com.jamith.AuctionManagementSystem.core.auction.dto.AuctionDTO;
import com.jamith.AuctionManagementSystem.core.auction.dto.AuctionSummaryDTO;
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

@WebServlet({"/auctions", "/auction-details"})
public class AuctionServlet extends HttpServlet {
    @EJB
    private UserSessionManagerRemote userSessionManager;

    @EJB
    private AuctionManagerRemote auctionManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        if (httpSession == null || httpSession.getAttribute("sessionToken") == null) {
            response.sendRedirect("http://localhost:8080/login.jsp");
            return;
        }
        String sessionToken = (String) httpSession.getAttribute("sessionToken");
        String path = request.getServletPath();

        try {
            ProfileDTO profile = userSessionManager.getUserProfile(sessionToken);
            if (!"BUYER".equals(profile.getRole())) {
                userSessionManager.logout(sessionToken);
                httpSession.invalidate();
                response.sendRedirect("http://localhost:8080/login.jsp");
                return;
            }

            if ("/auctions".equals(path)) {
                List<AuctionSummaryDTO> auctions = auctionManager.listActiveAuctions();
                request.setAttribute("auctions", auctions);
                request.setAttribute("firstName", profile.getFirstName());
                request.setAttribute("lastName", profile.getLastName());
                request.getRequestDispatcher("auctions.jsp").forward(request, response);
            } else if ("/auction-details".equals(path)) {
                Long auctionId = Long.parseLong(request.getParameter("auctionId"));
                AuctionDTO auction = auctionManager.getAuctionDetails(auctionId);
                request.setAttribute("auctionId", auction.getAuctionId());
                request.setAttribute("itemName", auction.getItemName());
                request.setAttribute("description", auction.getDescription());
                request.setAttribute("startPrice", auction.getStartPrice());
                request.setAttribute("bidIncrement", auction.getBidIncrement());
                request.setAttribute("currentBid", auction.getCurrentBid());
                request.setAttribute("status", auction.getStatus());
                request.setAttribute("endTime", auction.getEndTime());
                request.getRequestDispatcher("auction-details.jsp").forward(request, response);
            }
        } catch (UserException | AuctionException e) {
            httpSession.invalidate();
            response.sendRedirect("http://localhost:8080jsp");
        }
    }
}