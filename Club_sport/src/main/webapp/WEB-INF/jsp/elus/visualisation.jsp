<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.clubsport.model.ClassementCommune" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Visualisation — Élus</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/Style.css">
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body class="bg-light">
<div class="container py-4">

  <h1 class="h3 mb-4">Visualisation des statistiques</h1>

  <% if (request.getAttribute("erreur") != null) { %>
    <div class="alert alert-danger"><%= request.getAttribute("erreur") %></div>
  <% } %>

  <%-- Formulaire filtres --%>
  <form method="get" action="<%= request.getContextPath() %>/elu/visualisation" class="row g-3 mb-4">

    <div class="col-md-4">
      <label class="form-label">Région</label>
      <select class="form-select" name="region" onchange="this.form.submit()">
        <option value="">Toutes les régions</option>
        <% List<String> regions = (List<String>) request.getAttribute("regions");
           String regionSel = (String) request.getAttribute("region");
           if (regions != null) for (String r : regions) { %>
          <option value="<%= r %>" <%= r.equals(regionSel) ? "selected" : "" %>><%= r %></option>
        <% } %>
      </select>
    </div>

    <div class="col-md-4">
      <label class="form-label">Fédération</label>
      <select class="form-select" name="federation">
        <option value="">Toutes les fédérations</option>
        <% List<String> federations = (List<String>) request.getAttribute("federations");
           String fedSel = (String) request.getAttribute("federation");
           if (federations != null) for (String f : federations) { %>
          <option value="<%= f %>" <%= f.equals(fedSel) ? "selected" : "" %>><%= f %></option>
        <% } %>
      </select>
    </div>

    <div class="col-md-4 d-flex align-items-end">
      <button type="submit" class="btn btn-primary me-2">Filtrer</button>
      <a class="btn btn-outline-secondary" href="<%= request.getContextPath() %>/elu">Accueil élus</a>
    </div>

  </form>

  <%-- Résumé totaux --%>
  <% if (request.getAttribute("total") != null) { %>
  <div class="row g-3 mb-4">
    <div class="col-md-4">
      <div class="card text-center p-3">
        <div class="fs-4 fw-bold"><%= request.getAttribute("total") %></div>
        <div class="text-muted">Total licenciés</div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card text-center p-3">
        <div class="fs-4 fw-bold text-primary"><%= request.getAttribute("totalHommes") %></div>
        <div class="text-muted">Hommes</div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card text-center p-3">
        <div class="fs-4 fw-bold text-warning"><%= request.getAttribute("totalFemmes") %></div>
        <div class="text-muted">Femmes</div>
      </div>
    </div>
  </div>

  <%-- Graphiques --%>
  <div class="row g-4">

    <%-- Camembert H/F --%>
    <div class="col-md-5">
      <div class="card p-3">
        <h2 class="h6 mb-3">Répartition Hommes / Femmes</h2>
        <canvas id="chartHF"></canvas>
      </div>
    </div>

    <%-- Classement communes --%>
    <% List<ClassementCommune> classement = (List<ClassementCommune>) request.getAttribute("classement");
       if (classement != null && !classement.isEmpty()) { %>
    <div class="col-md-7">
      <div class="card p-3">
        <h2 class="h6 mb-3">Top 10 communes — taux de licenciés (%)</h2>
        <canvas id="chartClassement"></canvas>
      </div>
    </div>

    <%-- Tableau classement --%>
    <div class="col-12">
      <div class="card p-3">
        <h2 class="h6 mb-3">Détail classement</h2>
        <table class="table table-bordered table-sm">
          <thead class="table-light">
            <tr><th>Rang</th><th>Commune</th><th>Licenciés</th><th>Taux (%)</th></tr>
          </thead>
          <tbody>
            <% for (int i = 0; i < classement.size(); i++) {
                 ClassementCommune cc = classement.get(i); %>
            <tr>
              <td><%= i + 1 %></td>
              <td><%= cc.getNomCommune() %></td>
              <td><%= cc.getTotalLicencies() %></td>
              <td><%= cc.getTauxLicencies() %> %</td>
            </tr>
            <% } %>
          </tbody>
        </table>
      </div>
    </div>
    <% } %>

  </div><%-- /row --%>

  <script>
    new Chart(document.getElementById('chartHF'), {
      type: 'pie',
      data: {
        labels: ['Hommes', 'Femmes'],
        datasets: [{
          data: [<%= request.getAttribute("totalHommes") %>, <%= request.getAttribute("totalFemmes") %>],
          backgroundColor: ['#4e79a7', '#f28e2b']
        }]
      },
      options: { plugins: { legend: { position: 'bottom' } } }
    });

    <% if (classement != null && !classement.isEmpty()) { %>
    new Chart(document.getElementById('chartClassement'), {
      type: 'bar',
      data: {
        labels: <%= request.getAttribute("labelsCommunes") %>,
        datasets: [{
          label: 'Taux (%)',
          data: <%= request.getAttribute("dataPourcentages") %>,
          backgroundColor: '#0ea5b7'
        }]
      },
      options: {
        indexAxis: 'y',
        plugins: { legend: { display: false } },
        scales: { x: { beginAtZero: true } }
      }
    });
    <% } %>
  </script>

  <% } %>

  <div class="mt-4">
    <a class="btn btn-outline-primary" href="<%= request.getContextPath() %>/elu/licences">Recherche licences</a>
    <a class="btn btn-outline-secondary ms-2" href="<%= request.getContextPath() %>/elu">Accueil élus</a>
  </div>

</div>
</body>
</html>