<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.but3.simondesdevises.model.Question" %>
<%@ page import="fr.but3.simondesdevises.model.Choix" %>
<%
Question question = (Question) request.getAttribute("question");
String username = (String) request.getAttribute("username");
%>
<html>
<body>
<p>Connecte : <%= username %></p>
<% if (question == null) { %>
  <h1>Aucune question active</h1>
<% } else { %>
  <h1><%= question.getLibelle() %></h1>
  <ul>
  <% for (Choix choix : question.getChoix()) { %>
    <li><%= choix.getLibchoix() %> : <%= choix.getNbChoix() %></li>
  <% } %>
  </ul>
<% } %>
<p><a href="/activer">Retour a l'activation</a></p>
</body>
</html>
