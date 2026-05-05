<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.clubsport.model.StatLicenceElu" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Résultat — licences</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/Style.css">
</head>
<body class="bg-light">
<div class="container py-4">
  <h1 class="h3 mb-3">Résultat</h1>
  <%
    StatLicenceElu stat = (StatLicenceElu) request.getAttribute("stat");
    if (stat == null) {
  %>
    <div class="alert alert-info">Aucune ligne trouvée pour cette commune et cette fédération.</div>
  <% } else { %>
    <table class="table table-bordered bg-white">
      <tbody>
        <tr><th>Commune</th><td><%= stat.getNomCommune() %> (<%= stat.getCodeCommune() %>)</td></tr>
        <tr><th>Fédération</th><td><%= stat.getNomFederation() %> (<%= stat.getCodeFederation() %>)</td></tr>
        <tr><th>Total licences</th><td><%= stat.getTotalLicencies() %></td></tr>
        <tr><th>Licenciées (F)</th><td><%= stat.getLicenciesFemmes() %></td></tr>
        <tr><th>Licenciés (H)</th><td><%= stat.getLicenciesHommes() %></td></tr>
      </tbody>
    </table>
  <% } %>
  <a class="btn btn-outline-primary" href="<%= request.getContextPath() %>/elu/licences">Nouvelle recherche</a>
  <a class="btn btn-outline-secondary" href="<%= request.getContextPath() %>/elu">Accueil élus</a>
</div>
</body>
</html>
