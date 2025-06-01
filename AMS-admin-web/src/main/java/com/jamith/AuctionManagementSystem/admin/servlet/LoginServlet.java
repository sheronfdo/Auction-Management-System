package com.jamith.AuctionManagementSystem.admin.servlet;

import com.jamith.AuctionManagementSystem.core.user.dto.CredentialsDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.ProfileDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.SessionDTO;
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
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("sessionToken", session.getSessionToken());
                ProfileDTO profile = userSessionManager.getUserProfile(session.getSessionToken());
                if ("ADMIN".equals(profile.getRole())) {
                    response.sendRedirect("dashboard");
                } else {
                    userSessionManager.logout(session.getSessionToken());
                    httpSession.invalidate();
                    request.setAttribute("error", "Access denied: Admin role required");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else if ("/logout".equals(path)) {
                HttpSession httpSession = request.getSession(false);
                if (httpSession != null) {
                    String sessionToken = (String) httpSession.getAttribute("sessionToken");
                    if (sessionToken != null) {
                        userSessionManager.logout(sessionToken);
                    }
                    httpSession.invalidate();
                }
                response.sendRedirect("login.jsp");
            }
        } catch (UserException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}