<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Espace &Eacute;lus | Cartographie</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #f8f9fa; color: #334155; }
        .sidebar { min-width: 280px; max-width: 280px; min-height: 100vh; background: #1e293b; color: white; display: flex; flex-direction: column; }
        .sidebar .nav-link { color: #cbd5e1; padding: 12px 20px; border-radius: 8px; margin: 5px 15px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: #334155; color: white; }
        .sidebar .nav-link i { margin-right: 10px; width: 20px; text-align: center; }
        .main-content { flex-grow: 1; padding: 40px; }
        .filter-card { border: none; border-radius: 12px; background: white; }
        #carto-map { height: 560px; border-radius: 12px; z-index: 0; }
        .carto-legend { background: rgba(255,255,255,0.92); padding: 10px 12px; border-radius: 8px; line-height: 1.5; font-size: 12px; box-shadow: 0 1px 4px rgba(0,0,0,0.12); }
        .carto-legend i { display: inline-block; width: 18px; height: 12px; margin-right: 6px; border: 1px solid #94a3b8; vertical-align: middle; }
    </style>
</head>
<body>
<%
    String ctx = request.getContextPath();
    List<Map<String, String>> regions = (List<Map<String, String>>) request.getAttribute("regions");
    List<Map<String, String>> departements = (List<Map<String, String>>) request.getAttribute("departements");
    List<Map<String, String>> federations = (List<Map<String, String>>) request.getAttribute("federations");
%>
<div class="d-flex">
    <nav class="sidebar shadow">
        <div class="p-4 text-center">
            <h4 class="fw-bold mb-0">Sport<span class="text-primary">Data</span></h4>
            <small class="text-muted">Portail Collectivit&eacute;s</small>
        </div>
        <hr class="mx-3 border-secondary">
        <div class="nav flex-column flex-grow-1">
            <a href="<%= ctx %>/elu" class="nav-link"><i class="fas fa-th-large"></i> Dashboard</a>
            <a href="<%= ctx %>/elu/licences" class="nav-link"><i class="fas fa-chart-line"></i> Statistiques Licences</a>
            <a href="<%= ctx %>/elu/visualisation" class="nav-link"><i class="fas fa-chart-pie"></i> Visualisation</a>
            <a href="<%= ctx %>/elu/cartographie" class="nav-link active"><i class="fas fa-map-marked-alt"></i> Cartographie</a>
            <a href="<%= ctx %>/elu/licences" class="nav-link"><i class="fas fa-file-export"></i> Exports CSV</a>
        </div>
        <div class="p-3 mt-auto">
            <a href="<%= ctx %>/api/logout" class="btn btn-outline-light w-100 btn-sm mb-2">
                <i class="fas fa-sign-out-alt"></i> D&eacute;connexion
            </a>
            <a href="<%= ctx %>/elu" class="btn btn-link btn-sm w-100 text-muted text-decoration-none">
                Retour au dashboard
            </a>
        </div>
    </nav>

    <main class="main-content">
        <div class="container-fluid">
            <h1 class="h3 fw-bold mb-2">Cartographie des licences</h1>
            <p class="text-muted mb-4">Carte choropl&egrave;the par r&eacute;gion ; d&eacute;tail par commune lorsqu&apos;une r&eacute;gion ou un d&eacute;partement est s&eacute;lectionn&eacute;.</p>

            <% if (request.getAttribute("erreurGeo") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("erreurGeo") %></div>
            <% } %>

            <div class="card filter-card shadow-sm mb-4">
                <div class="card-body p-4">
                    <form class="row g-3" id="carto-filters" autocomplete="off">
                        <div class="col-md-3">
                            <label class="form-label" for="codeRegion">R&eacute;gion</label>
                            <select class="form-select" id="codeRegion" name="codeRegion">
                                <option value="">Toutes les r&eacute;gions</option>
                                <% if (regions != null) { for (Map<String, String> region : regions) { %>
                                    <option value="<%= region.get("code") %>"><%= region.get("label") %></option>
                                <% } } %>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label" for="codeDepartement">D&eacute;partement</label>
                            <select class="form-select" id="codeDepartement" name="codeDepartement">
                                <option value="">Tous les d&eacute;partements</option>
                                <% if (departements != null) { for (Map<String, String> dept : departements) { %>
                                    <option value="<%= dept.get("code") %>"
                                            data-region="<%= dept.get("parentRegion") %>">
                                        <%= dept.get("label") %>
                                    </option>
                                <% } } %>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label" for="codeFederation">F&eacute;d&eacute;ration</label>
                            <% if (federations == null || federations.isEmpty()) { %>
                            <input class="form-control" id="codeFederation" name="codeFederation" type="text" maxlength="10" required placeholder="Code">
                            <% } else { %>
                            <select class="form-select" id="codeFederation" name="codeFederation" required>
                                <option value="" disabled selected>Choisir&hellip;</option>
                                <% for (Map<String, String> fed : federations) { %>
                                    <option value="<%= fed.get("code") %>"><%= fed.get("label") %></option>
                                <% } %>
                            </select>
                            <% } %>
                        </div>
                        <div class="col-12">
                            <button type="button" class="btn btn-primary px-4 rounded-pill" id="btnAfficherCarto">
                                <i class="fas fa-map me-2"></i>Afficher sur la carte
                            </button>
                            <span class="text-muted small ms-2" id="carto-status"></span>
                        </div>
                    </form>
                </div>
            </div>

            <div class="card shadow-sm border-0">
                <div class="card-body p-3">
                    <div id="carto-map"></div>
                </div>
            </div>
        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<script>
    window.ELU_CARTO_CTX = '<%= ctx %>';
    window.ELU_CARTO_GEOJSON = '<%= ctx %>/regions.geojson';
</script>
<script src="<%= ctx %>/js/elu-cartographie.js?v=20260523"></script>
</body>
</html>
