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
  <p>Un administrateur doit d'abord activer une question.</p>
<% } else { %>
  <h1><%= question.getLibelle() %></h1>
  <form method="post" action="/voter">
  <% for (Choix choix : question.getChoix()) { %>
    <p>
      <input type="radio" name="cno" value="<%= choix.getCno() %>" required>
      <%= choix.getLibchoix() %>
    </p>
  <% } %>
    <button type="submit">Voter</button>
  </form>
<% } %>
</body>
</html>
