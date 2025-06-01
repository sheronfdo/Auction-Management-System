package com.jamith.AuctionManagementSystem.admin.servlet;

import com.jamith.AuctionManagementSystem.core.user.dto.CredentialsDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.SessionDTO;
import com.jamith.AuctionManagementSystem.core.user.exception.UserException;
import com.jamith.AuctionManagementSystem.core.user.remote.UserSessionManagerRemote;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet({"/login", "/logout"})
public class LoginServlet extends HttpServlet {
    @EJB
    private UserSessionManagerRemote userSessionManager;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        try {
            if ("/login".equals(path)) {
                CredentialsDTO credentials = new CredentialsDTO();
                credentials.setEmail(request.getParameter("email"));
                credentials.setPassword(request.getParameter("password"));
                SessionDTO session = userSessionManager.login(credentials);
                request.getSession().setAttribute("sessionToken", session.getSessionToken());
                String role = userSessionManager.getUserProfile().getRole();
                if ("ADMIN".equals(role)) {
                    response.sendRedirect("dashboard");
                } else {
                    userSessionManager.logout();
                    request.getSession().invalidate();
                    request.setAttribute("error", "Access denied: Admin role required");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else if ("/logout".equals(path)) {
                userSessionManager.logout();
                request.getSession().invalidate();
                response.sendRedirect("login.jsp");
            }
        } catch (UserException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}