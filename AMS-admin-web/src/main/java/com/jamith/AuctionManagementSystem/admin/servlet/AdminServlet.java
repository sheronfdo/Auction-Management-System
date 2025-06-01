package com.jamith.AuctionManagementSystem.admin.servlet;

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

@WebServlet({"/dashboard", "/admin-profile"})
public class AdminServlet extends HttpServlet {

    @EJB
    private UserSessionManagerRemote userSessionManager;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        try {
            ProfileDTO profile = userSessionManager.getUserProfile();
            if (!"ADMIN".equals(profile.getRole())) {
                response.sendRedirect("/buyer/login.jsp");
                return;
            }
            request.setAttribute("email", profile.getEmail());
            request.setAttribute("firstName", profile.getFirstName());
            request.setAttribute("lastName", profile.getLastName());
            String jsp = "/admin-profile".equals(path) ? "profile.jsp" : "dashboard.jsp";
            request.getRequestDispatcher(jsp).forward(request, response);
        } catch (UserException e) {
            response.sendRedirect("/admin/login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("/admin-profile".equals(request.getServletPath())) {
            try {
                ProfileDTO profile = new ProfileDTO();
                profile.setEmail(request.getParameter("email"));
                profile.setFirstName(request.getParameter("firstName"));
                profile.setLastName(request.getParameter("lastName"));
                profile.setPassword(request.getParameter("password"));
                userSessionManager.updateProfile(profile);
                response.sendRedirect("admin-profile");
            } catch (UserException e) {
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("profile.jsp").forward(request, response);
            }
        }
    }
}