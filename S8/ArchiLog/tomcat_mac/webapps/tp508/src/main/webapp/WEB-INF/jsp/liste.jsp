<%@ page import="java.util.ArrayList, fr.apgis4.tp508.Model.Etudiant" %>
<h1>Liste des étudiants</h1>
<table>
  <thead><tr><th>Nom</th><th>Prénom</th></tr></thead>
  <tbody>
  <%
    ArrayList<Etudiant> etudiants = (ArrayList<Etudiant>) request.getAttribute("etudiants");
    if (etudiants != null) {
      for (Etudiant e : etudiants) {
  %>
      <tr><td><%= e.getNom() %></td><td><%= e.getPrenom() %></td></tr>
  <%
      }
    }
  %>
  </tbody>
</table>
