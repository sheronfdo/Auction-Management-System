package com.jamith.AuctionManagementSystem.buyer.servlet;

import com.jamith.AuctionManagementSystem.core.auction.dto.AuctionDTO;
import com.jamith.AuctionManagementSystem.core.auction.dto.AuctionSummaryDTO;
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
import java.util.List;


@WebServlet({"/auctions", "/auction-details", "/place-bid"})
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
            if (!"BUYER".equals(profile.getRole())) {
                userSessionManager.logout(sessionToken);
                httpSession.invalidate();
                response.sendRedirect("login.jsp");
                return;
            }

            if ("/auctions".equals(path)) {
                List<AuctionSummaryDTO> auctions = auctionManager.listActiveAuctions();
                request.setAttribute("auctions", auctions);
                request.setAttribute("firstName", profile.getFirstName());
                request.getRequestDispatcher("auctions.jsp").forward(request, response);
            } else if ("/auction-details".equals(path)) {
                Long auctionId = Long.parseLong(request.getParameter("auctionId"));
                AuctionDTO auction = auctionManager.getAuctionDetails(auctionId);
                List<BidDTO> bids = auctionManager.getBidsForAuction(auctionId);
                request.setAttribute("auction", auction);
                request.setAttribute("bids", bids);
                request.getRequestDispatcher("auction-details.jsp").forward(request, response);
            }
        } catch (UserException | AuctionException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
            if (!"BUYER".equals(profile.getRole())) {
                userSessionManager.logout(sessionToken);
                httpSession.invalidate();
                response.sendRedirect("login.jsp");
                return;
            }

            if ("/place-bid".equals(path)) {
                Long auctionId = Long.parseLong(request.getParameter("auctionId"));
                String bidAmountStr = request.getParameter("bidAmount");
                if (bidAmountStr == null || bidAmountStr.isEmpty()) {
                    throw new AuctionException("Bid amount is required");
                }
                BigDecimal bidAmount = new BigDecimal(bidAmountStr);
                auctionManager.placeBid(auctionId, bidAmount, sessionToken);
                response.sendRedirect("auction-details?auctionId=" + auctionId);
            }
        } catch (UserException | AuctionException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}