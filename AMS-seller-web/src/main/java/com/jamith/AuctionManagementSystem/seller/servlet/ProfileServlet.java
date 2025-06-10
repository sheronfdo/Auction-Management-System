package com.jamith.AuctionManagementSystem.seller.servlet;

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


@WebServlet("/seller-profile")
public class ProfileServlet extends HttpServlet {
    @EJB
    private UserSessionManagerRemote userSessionManager;

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
            if (!"SELLER".equals(profile.getRole())) {
                userSessionManager.logout(sessionToken);
                httpSession.invalidate();
                response.sendRedirect("login.jsp");
                return;
            }
            request.setAttribute("email", profile.getEmail());
            request.setAttribute("firstName", profile.getFirstName());
            request.setAttribute("lastName", profile.getLastName());
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } catch (UserException e) {
            httpSession.invalidate();
            response.sendRedirect("login.jsp");
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
        try {
            ProfileDTO profile = new ProfileDTO();
            profile.setEmail(request.getParameter("email"));
            profile.setFirstName(request.getParameter("firstName"));
            profile.setLastName(request.getParameter("lastName"));
            profile.setPassword(request.getParameter("password"));
            userSessionManager.updateProfile(profile, sessionToken);
            response.sendRedirect("seller-profile");
        } catch (UserException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        }
    }
}