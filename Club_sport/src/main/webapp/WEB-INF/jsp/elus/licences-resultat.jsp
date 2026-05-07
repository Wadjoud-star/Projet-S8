<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.clubsport.model.StatLicenceElu" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Espace Élus | Résultat</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #f8f9fa; color: #334155; }
        .sidebar { min-width: 280px; max-width: 280px; min-height: 100vh; background: #1e293b; color: white; }
        .sidebar .nav-link { color: #cbd5e1; padding: 12px 20px; border-radius: 8px; margin: 5px 15px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: #334155; color: white; }
        .sidebar .nav-link i { margin-right: 10px; width: 20px; text-align: center; }
        .main-content { flex-grow: 1; padding: 40px; }
        .result-card { border: none; border-radius: 12px; background: white; }
    </style>
</head>
<body>
<%
    StatLicenceElu stat = (StatLicenceElu) request.getAttribute("stat");
%>
<div class="d-flex">
    <nav class="sidebar shadow">
        <div class="p-4 text-center">
            <h4 class="fw-bold mb-0">Sport<span class="text-primary">Data</span></h4>
            <small class="text-muted">Portail Collectivités</small>
        </div>
        <hr class="mx-3 border-secondary">
        <div class="nav flex-column">
            <a href="<%= request.getContextPath() %>/elu" class="nav-link"><i class="fas fa-th-large"></i> Dashboard</a>
            <a href="<%= request.getContextPath() %>/elu/licences" class="nav-link active"><i class="fas fa-chart-line"></i> Statistiques Licences</a>
            <a href="#" class="nav-link"><i class="fas fa-map-marked-alt"></i> Cartographie</a>
            <a href="#" class="nav-link"><i class="fas fa-file-export"></i> Exports DATA</a>
        </div>
    </nav>

    <main class="main-content">
        <div class="container-fluid">
            <h1 class="h3 fw-bold mb-4">Résultat de la recherche</h1>
            <% if (stat == null) { %>
                <div class="alert alert-info">Aucune ligne trouvée avec ces filtres.</div>
            <% } else { %>
                <div class="row g-3 mb-4">
                    <div class="col-md-4">
                        <div class="card result-card shadow-sm"><div class="card-body">
                            <div class="text-muted small">Valeur selon genre</div>
                            <div class="fs-4 fw-bold"><%= stat.getValeurGenre() %></div>
                        </div></div>
                    </div>
                    <div class="col-md-4">
                        <div class="card result-card shadow-sm"><div class="card-body">
                            <div class="text-muted small">Genre sélectionné</div>
                            <div class="fw-semibold"><%= stat.getGenre() %></div>
                        </div></div>
                    </div>
                    <div class="col-md-4">
                        <div class="card result-card shadow-sm"><div class="card-body">
                            <div class="text-muted small">Fédération</div>
                            <div class="fw-semibold"><%= stat.getNomFederation() %> (<%= stat.getCodeFederation() %>)</div>
                        </div></div>
                    </div>
                </div>

                <div class="card result-card shadow-sm">
                    <div class="card-body">
                        <table class="table table-bordered align-middle mb-0">
                            <tbody>
                                <tr><th style="width: 30%;">Région</th><td><%= stat.getNomRegion() == null ? "-" : stat.getNomRegion() %> (<%= stat.getCodeRegion() == null ? "-" : stat.getCodeRegion() %>)</td></tr>
                                <tr><th>Département</th><td><%= stat.getCodeDepartement() == null ? "-" : stat.getCodeDepartement() %></td></tr>
                                <tr><th>Commune</th><td><%= stat.getNomCommune() == null ? "-" : stat.getNomCommune() %> (<%= stat.getCodeCommune() == null ? "-" : stat.getCodeCommune() %>)</td></tr>
                                <tr><th>Total licences</th><td><%= stat.getTotalLicencies() %></td></tr>
                                <tr><th>Licenciées (F)</th><td><%= stat.getLicenciesFemmes() %></td></tr>
                                <tr><th>Licenciés (H)</th><td><%= stat.getLicenciesHommes() %></td></tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            <% } %>

            <div class="mt-3 d-flex gap-2">
                <a class="btn btn-primary rounded-pill px-4" href="<%= request.getContextPath() %>/elu/licences">Nouvelle recherche</a>
                <a class="btn btn-outline-secondary rounded-pill px-4" href="<%= request.getContextPath() %>/elu">Retour accueil élus</a>
            </div>
        </div>
    </main>
</div>
</body>
</html>
