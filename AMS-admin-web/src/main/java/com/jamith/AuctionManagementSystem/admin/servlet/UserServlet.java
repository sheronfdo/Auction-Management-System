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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/user-details")
public class UserServlet extends HttpServlet {
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
            ProfileDTO adminProfile = userSessionManager.getUserProfile(sessionToken);
            if (!"ADMIN".equals(adminProfile.getRole())) {
                userSessionManager.logout(sessionToken);
                httpSession.invalidate();
                response.sendRedirect("login.jsp");
                return;
            }
            String userId = request.getParameter("userId");
            ProfileDTO userProfile = userSessionManager.getUserProfileById(userId, sessionToken);
            request.setAttribute("userProfile", userProfile);
            request.getRequestDispatcher("user-details.jsp").forward(request, response);
        } catch (UserException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("users.jsp").forward(request, response);
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
            ProfileDTO adminProfile = userSessionManager.getUserProfile(sessionToken);
            if (!"ADMIN".equals(adminProfile.getRole())) {
                userSessionManager.logout(sessionToken);
                httpSession.invalidate();
                response.sendRedirect("login.jsp");
                return;
            }
            String userId = request.getParameter("userId");
            String action = request.getParameter("action");
            if ("suspend".equals(action)) {
                userSessionManager.suspendUser(userId, sessionToken);
            } else if ("activate".equals(action)) {
                userSessionManager.activateUser(userId, sessionToken);
            }
            response.sendRedirect("users");
        } catch (UserException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("user-details.jsp").forward(request, response);
        }
    }
}