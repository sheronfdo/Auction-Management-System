package com.jamith.AuctionManagementSystem.buyer.servlet;

import com.jamith.AuctionManagementSystem.core.user.dto.ProfileDTO;
import com.jamith.AuctionManagementSystem.core.user.exception.UserException;
import com.jamith.AuctionManagementSystem.core.user.remote.UserSessionManagerRemote;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/buyer-profile")
public class ProfileServlet extends HttpServlet {

    @EJB
    private UserSessionManagerRemote userSessionManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ProfileDTO profile = userSessionManager.getUserProfile();
            request.setAttribute("email", profile.getEmail());
            request.setAttribute("firstName", profile.getFirstName());
            request.setAttribute("lastName", profile.getLastName());
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } catch (UserException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ProfileDTO profile = new ProfileDTO();
            profile.setEmail(request.getParameter("email"));
            profile.setFirstName(request.getParameter("firstName"));
            profile.setLastName(request.getParameter("lastName"));
            profile.setPassword(request.getParameter("password"));
            userSessionManager.updateProfile(profile);
            response.sendRedirect("profile");
        } catch (UserException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        }
    }
}