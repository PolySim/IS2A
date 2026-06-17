<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.but3.simondesdevises.model.Question" %>
<%
List<Question> questions = (List<Question>) request.getAttribute("questions");
String username = (String) request.getAttribute("username");
%>
<html>
<body>
<p>Connecte : <%= username %></p>
<h1>Activer une question</h1>
<form method="post" action="/activer">
<% for (Question question : questions) { %>
  <p>
    <input type="radio" name="qno" value="<%= question.getQno() %>" <%= question.isActive() ? "checked" : "" %> required>
    <%= question.getLibelle() %>
  </p>
<% } %>
  <button type="submit">Activer</button>
</form>
<p><a href="/voir">Voir les resultats</a></p>
</body>
</html>
