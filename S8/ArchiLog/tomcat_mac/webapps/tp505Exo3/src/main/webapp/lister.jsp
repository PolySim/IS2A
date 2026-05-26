<html>
  <body>
    <%@ page import="fr.apgis4.Lister" %>
    <%@ page import="fr.apgis4.model.Auteur" %>
    <%@ page import="fr.apgis4.model.Livre" %>
    <% Lister lister = new Lister(); %>

    <% for (Auteur auteur : lister.getAuteurs()) { %>
      <h1><%= auteur %></h1>
      <% for (Livre livre : auteur.getLivres()) { %>
        <ol><%= livre %></ol>
      <% } %>
    <% } %>

  </body>
</html>
