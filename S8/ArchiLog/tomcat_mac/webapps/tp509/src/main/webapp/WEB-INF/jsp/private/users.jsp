<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.apgis4.tp509.MonController.UserRow" %>
<%
  List<UserRow> users = (List<UserRow>) request.getAttribute("users");
%>
<h1>Table users</h1>
<table border="1">
  <tr>
    <th>username</th>
    <th>password</th>
    <th>enabled</th>
  </tr>
  <% for (UserRow user : users) { %>
  <tr>
    <td><%= user.username() %></td>
    <td><%= user.password() %></td>
    <td><%= user.enabled() %></td>
  </tr>
  <% } %>
</table>
