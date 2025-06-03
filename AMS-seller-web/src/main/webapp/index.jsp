<%--
  Created by IntelliJ IDEA.
  User: jamith
  Date: 5/31/25
  Time: 9:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    HttpSession httpSession = request.getSession(false);
    String sessionToken = (httpSession != null) ? (String) httpSession.getAttribute("sessionToken") : null;
    if (sessionToken == null) {
        response.sendRedirect("login.jsp");
    } else {
        response.sendRedirect("dashboard.jsp");
    }
%>
<html>
<head>
    <title>Auction System</title>
</head>
<body>
<!-- Redirecting... -->
</body>
</html>