<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Licences par commune et fédération</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/Style.css">
</head>
<body class="bg-light">
<div class="container py-4">
  <h1 class="h3 mb-3">Licences — recherche</h1>
  <% if (request.getAttribute("erreur") != null) { %>
    <div class="alert alert-warning"><%= request.getAttribute("erreur") %></div>
  <% } %>
  <form method="post" action="<%= request.getContextPath() %>/elu/licences" class="row g-3">
    <div class="col-md-4">
      <label class="form-label" for="codeCommune">Code commune (ex. 01001)</label>
      <input class="form-control" id="codeCommune" name="codeCommune" type="text" maxlength="10" required>
    </div>
    <div class="col-md-4">
      <label class="form-label" for="codeFederation">Code fédération (ex. 101)</label>
      <input class="form-control" id="codeFederation" name="codeFederation" type="text" maxlength="10" required>
    </div>
    <div class="col-12">
      <button type="submit" class="btn btn-primary">Afficher</button>
      <a class="btn btn-outline-secondary ms-2" href="<%= request.getContextPath() %>/elu">Retour accueil élus</a>
    </div>
  </form>
</div>
</body>
</html>
