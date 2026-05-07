<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Espace Élus | Statistiques Licences</title>
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
        .filter-card { border: none; border-radius: 12px; background: white; }
    </style>
</head>
<body>
<%
    String selectedRegion = request.getAttribute("codeRegion") == null ? "" : String.valueOf(request.getAttribute("codeRegion"));
    String selectedDepartement = request.getAttribute("codeDepartement") == null ? "" : String.valueOf(request.getAttribute("codeDepartement"));
    String codeCommune = request.getAttribute("codeCommune") == null ? "" : String.valueOf(request.getAttribute("codeCommune"));
    String codeFederation = request.getAttribute("codeFederation") == null ? "" : String.valueOf(request.getAttribute("codeFederation"));
    String genre = request.getAttribute("genre") == null ? "TOTAL" : String.valueOf(request.getAttribute("genre"));
    List<Map<String, String>> regions = (List<Map<String, String>>) request.getAttribute("regions");
    List<Map<String, String>> departements = (List<Map<String, String>>) request.getAttribute("departements");
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
        <div class="mt-auto p-3">
            <a href="<%= request.getContextPath() %>/index.html" class="btn btn-outline-light w-100 btn-sm">
                <i class="fas fa-sign-out-alt"></i> Quitter l'espace
            </a>
        </div>
    </nav>

    <main class="main-content">
        <div class="container-fluid">
            <h1 class="h3 fw-bold mb-4">Filtres statistiques licences</h1>
            <% if (request.getAttribute("erreur") != null) { %>
                <div class="alert alert-warning"><%= request.getAttribute("erreur") %></div>
            <% } %>
            <% if (request.getAttribute("erreurGeo") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("erreurGeo") %></div>
            <% } %>

            <div class="card filter-card shadow-sm">
                <div class="card-body p-4">
                    <form method="post" action="<%= request.getContextPath() %>/elu/licences" class="row g-3">
                        <div class="col-md-3">
                            <label class="form-label" for="codeRegion">Région</label>
                            <select class="form-select" id="codeRegion" name="codeRegion">
                                <option value="">Toutes les régions</option>
                                <% if (regions != null) { for (Map<String, String> region : regions) {
                                       String rc = region.get("code");
                                       String rl = region.get("label");
                                %>
                                    <option value="<%= rc %>" <%= rc != null && rc.equals(selectedRegion) ? "selected" : "" %>>
                                        <%= rl %>
                                    </option>
                                <% } } %>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label" for="codeDepartement">Département</label>
                            <select class="form-select" id="codeDepartement" name="codeDepartement">
                                <option value="">Tous les départements</option>
                                <% if (departements != null) { for (Map<String, String> dept : departements) {
                                       String dc = dept.get("code");
                                       String dl = dept.get("label");
                                       String pr = dept.get("parentRegion");
                                %>
                                    <option value="<%= dc %>"
                                            data-region="<%= pr %>"
                                            <%= dc != null && dc.equals(selectedDepartement) ? "selected" : "" %>>
                                        <%= dl %>
                                    </option>
                                <% } } %>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label" for="codeCommune">Code commune</label>
                            <input class="form-control" id="codeCommune" name="codeCommune" type="text" maxlength="10" value="<%= codeCommune %>">
                        </div>
                        <div class="col-md-2">
                            <label class="form-label" for="codeFederation">Code fédération</label>
                            <input class="form-control" id="codeFederation" name="codeFederation" type="text" maxlength="10" required value="<%= codeFederation %>">
                        </div>
                        <div class="col-md-2">
                            <label class="form-label" for="genre">Genre</label>
                            <select class="form-select" id="genre" name="genre">
                                <option value="TOTAL" <%= "TOTAL".equalsIgnoreCase(genre) ? "selected" : "" %>>Total</option>
                                <option value="F" <%= "F".equalsIgnoreCase(genre) ? "selected" : "" %>>Femmes</option>
                                <option value="H" <%= "H".equalsIgnoreCase(genre) ? "selected" : "" %>>Hommes</option>
                            </select>
                        </div>
                        <div class="col-12">
                            <button type="submit" class="btn btn-primary px-4 rounded-pill">
                                <i class="fas fa-search me-2"></i>Afficher
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>
</div>

<script>
    (function () {
        const regionSelect = document.getElementById('codeRegion');
        const departementSelect = document.getElementById('codeDepartement');

        function filterDepartements() {
            const selectedRegion = regionSelect.value;
            const currentDepartment = departementSelect.value;
            let keepCurrent = false;

            Array.from(departementSelect.options).forEach((option, index) => {
                if (index === 0) {
                    option.hidden = false;
                    return;
                }
                const deptRegion = option.getAttribute('data-region');
                const visible = !selectedRegion || deptRegion === selectedRegion;
                option.hidden = !visible;
                if (visible && option.value === currentDepartment) {
                    keepCurrent = true;
                }
            });

            if (!keepCurrent) {
                departementSelect.value = '';
            }
        }

        regionSelect.addEventListener('change', filterDepartements);
        filterDepartements();
    })();
</script>
</body>
</html>
