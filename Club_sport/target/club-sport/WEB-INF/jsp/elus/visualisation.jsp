<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.clubsport.model.ClassementCommune" %>

<%
  List<String> regions = (List<String>) request.getAttribute("regions");
  String regionSel = (String) request.getAttribute("region");

  List<String> federations = (List<String>) request.getAttribute("federations");
  String fedSel = (String) request.getAttribute("codeFederation");

  String codeCommuneSel = (String) request.getAttribute("codeCommune");

  List<ClassementCommune> classement =
      (List<ClassementCommune>) request.getAttribute("classement");

  StringBuilder labelsCommunes = new StringBuilder("[");
  StringBuilder dataLicencies  = new StringBuilder("[");

  if (classement != null) {
      for (int i = 0; i < classement.size(); i++) {
          ClassementCommune cc = classement.get(i);
          labelsCommunes.append("\"")
              .append(cc.getNomCommune().replace("\"", "\\\""))
              .append("\"");
          dataLicencies.append(cc.getTotalLicencies());
          if (i < classement.size() - 1) {
              labelsCommunes.append(",");
              dataLicencies.append(",");
          }
      }
  }
  labelsCommunes.append("]");
  dataLicencies.append("]");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Visualisation</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <style>
    body { background: #f8f9fa; color: #1e293b; }
    .stat-card, .main-card { border: none; border-radius: 14px; background: white; }
  </style>
</head>

<body>
<div class="container-fluid p-4">

  <h1 class="h2 fw-bold mb-1">Visualisation</h1>
  <p class="text-muted mb-4">Statistiques et classements des licenciés par territoire.</p>

  <% if (request.getAttribute("erreur") != null) { %>
    <div class="alert alert-danger"><%= request.getAttribute("erreur") %></div>
  <% } %>

  <form method="get" action="<%= request.getContextPath() %>/elu/visualisation" class="card main-card shadow-sm p-4 mb-4">
    <div class="row g-3">

      <div class="col-md-3">
        <label class="form-label fw-semibold">Région</label>
        <select class="form-select" name="region">
          <option value="">Toutes les régions</option>
          <% if (regions != null) { for (String r : regions) { %>
            <option value="<%= r %>" <%= r.equals(regionSel) ? "selected" : "" %>><%= r %></option>
          <% } } %>
        </select>
      </div>

      <div class="col-md-3">
        <label class="form-label fw-semibold">Fédération</label>
        <select class="form-select" name="codeFederation">
          <option value="">Toutes les fédérations</option>
          <% if (federations != null) {
               for (String f : federations) {
                 String code = f;
                 int pos = f.indexOf(" — ");
                 if (pos == -1) pos = f.indexOf(" - ");
                 if (pos != -1) code = f.substring(0, pos).trim(); %>
            <option value="<%= code %>" <%= code.equals(fedSel) ? "selected" : "" %>><%= f %></option>
          <% } } %>
        </select>
      </div>

      <div class="col-md-3">
        <label class="form-label fw-semibold">Code commune</label>
        <input class="form-control" type="text" name="codeCommune"
               placeholder="ex. 76540"
               value="<%= codeCommuneSel != null ? codeCommuneSel : "" %>">
      </div>

      <div class="col-md-3 d-flex align-items-end gap-2">
        <button type="submit" class="btn btn-primary w-100">Filtrer</button>
        <a href="<%= request.getContextPath() %>/elu/visualisation" class="btn btn-outline-secondary w-100">Réinitialiser</a>
      </div>

    </div>
  </form>

  <%-- Cartes statistiques --%>
  <div class="row g-3 mb-4">
    <div class="col-md-4">
      <div class="card stat-card shadow-sm text-center p-4">
        <div class="fs-2 fw-bold"><%= request.getAttribute("total") != null ? request.getAttribute("total") : 0 %></div>
        <div class="text-muted">Total licenciés</div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card stat-card shadow-sm text-center p-4">
        <div class="fs-2 fw-bold text-primary"><%= request.getAttribute("totalHommes") != null ? request.getAttribute("totalHommes") : 0 %></div>
        <div class="text-muted">Hommes</div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card stat-card shadow-sm text-center p-4">
        <div class="fs-2 fw-bold text-warning"><%= request.getAttribute("totalFemmes") != null ? request.getAttribute("totalFemmes") : 0 %></div>
        <div class="text-muted">Femmes</div>
      </div>
    </div>
  </div>

  <%-- Camembert + Histogramme licenciés --%>
  <div class="row g-4 mb-4">
    <div class="col-md-5">
      <div class="card main-card shadow-sm p-4">
        <h2 class="h6 fw-bold mb-3">Répartition Hommes / Femmes</h2>
        <canvas id="chartHF"></canvas>
      </div>
    </div>

    <% if (classement != null && !classement.isEmpty()) { %>
    <div class="col-md-7">
      <div class="card main-card shadow-sm p-4">
        <h2 class="h6 fw-bold mb-3">Nombre de licenciés par commune</h2>
        <canvas id="chartClassement" style="max-height: 350px;"></canvas>
      </div>
    </div>
    <% } %>
  </div>

  <%-- Tableau classement --%>
  <% if (classement != null && !classement.isEmpty()) { %>
    <div class="card main-card shadow-sm p-4 mt-4">
      <h2 class="h6 fw-bold mb-3">Classement des communes par nombre de licenciés</h2>
      <table class="table table-bordered table-sm">
        <thead class="table-light">
          <tr>
            <th>Rang</th>
            <th>Commune</th>
            <th>Licenciés</th>
            <th>Taux (%)</th>
          </tr>
        </thead>
        <tbody>
          <% for (int i = 0; i < classement.size(); i++) {
               ClassementCommune cc = classement.get(i); %>
            <tr>
              <td><%= i + 1 %></td>
              <td><%= cc.getNomCommune() %></td>
              <td><%= cc.getTotalLicencies() %></td>
              <td><%= String.format("%.2f", cc.getTauxLicencies()) %> %</td>
            </tr>
          <% } %>
        </tbody>
      </table>
    </div>
  <% } else { %>
    <div class="alert alert-info mt-4">Aucune commune trouvée pour cette région/fédération.</div>
  <% } %>

  <%-- Export PDF graphiques uniquement --%>

<button type="button"
        class="btn btn-danger mt-3"
        onclick="exportChartsPDF()">
    Exporter les graphiques PDF
</button>

<form id="exportPdfForm"
      method="post"
      action="<%= request.getContextPath() %>/elu/export-pdf">

    <input type="hidden"
           name="region"
           value="<%= regionSel != null ? regionSel : "" %>">

    <input type="hidden"
           name="codeFederation"
           value="<%= fedSel != null ? fedSel : "" %>">

    <input type="hidden"
           name="codeCommune"
           value="<%= codeCommuneSel != null ? codeCommuneSel : "" %>">

    <input type="hidden" id="chartHFImage" name="chartHF">

    <input type="hidden"
           id="chartClassementImage"
           name="chartClassement">
</form>

</div>

<script>
  // Camembert H/F
  const totalHommes = <%= request.getAttribute("totalHommes") != null ? request.getAttribute("totalHommes") : 0 %>;
  const totalFemmes = <%= request.getAttribute("totalFemmes") != null ? request.getAttribute("totalFemmes") : 0 %>;

  new Chart(document.getElementById('chartHF'), {
    type: 'pie',
    data: {
      labels: ['Hommes', 'Femmes'],
      datasets: [{
        data: [totalHommes, totalFemmes],
        backgroundColor: ['#4e79a7', '#f28e2b']
      }]
    },
    options: { plugins: { legend: { position: 'bottom' } } }
  });

  <% if (classement != null && !classement.isEmpty()) { %>
  // Histogramme licenciés
  const labelsCommunes = <%= labelsCommunes %>;
  const dataLicencies  = <%= dataLicencies %>;

  new Chart(document.getElementById('chartClassement'), {
    type: 'bar',
    data: {
      labels: labelsCommunes,
      datasets: [{
        label: 'Nombre de licenciés',
        data: dataLicencies,
        backgroundColor: '#4e79a7'
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
<script>
function exportChartsPDF() {

    const chartHF =
        document.getElementById("chartHF");

    const chartClassement =
        document.getElementById("chartClassement");

    document.getElementById("chartHFImage").value =
        chartHF.toDataURL("image/png");

    if (chartClassement) {

        document.getElementById("chartClassementImage").value =
            chartClassement.toDataURL("image/png");
    }

    document.getElementById("exportPdfForm").submit();
}
</script>
</body>
</html>