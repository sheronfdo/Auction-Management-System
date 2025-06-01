package com.jamith.AuctionManagementSystem.seller.servlet;

import com.jamith.AuctionManagementSystem.core.user.dto.CredentialsDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.ProfileDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.SessionDTO;
import com.jamith.AuctionManagementSystem.core.user.dto.UserDTO;
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
@WebServlet({"/login", "/register", "/logout"})
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
                    response.sendRedirect("/admin/dashboard");
                } else if ("SELLER".equals(profile.getRole())) {
                    response.sendRedirect("seller-profile");
                } else {
                    response.sendRedirect("/buyer/buyer-profile");
                }
            } else if ("/register".equals(path)) {
                UserDTO user = new UserDTO();
                user.setEmail(request.getParameter("email"));
                user.setPassword(request.getParameter("password"));
                user.setRole(request.getParameter("role"));
                user.setFirstName(request.getParameter("firstName"));
                user.setLastName(request.getParameter("lastName"));
                userSessionManager.register(user);
                response.sendRedirect("login.jsp");
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
            request.getRequestDispatcher(path.substring(1) + ".jsp").forward(request, response);
        }
    }
}