<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.clubsport.model.StatLicenceElu" %>
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
        .results-wrap { background: #fff; border-radius: 12px; border: 1px solid #e2e8f0; }
        .results-wrap .section-title { font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.04em; color: #64748b; font-weight: 600; margin-bottom: 0.35rem; }
        .kpi-tile { border-radius: 10px; background: #f8fafc; border: 1px solid #e2e8f0; padding: 1rem 1.1rem; height: 100%; }
    </style>
</head>
<body>
<%
    String selectedRegion = request.getAttribute("codeRegion") == null ? "" : String.valueOf(request.getAttribute("codeRegion"));
    String selectedDepartement = request.getAttribute("codeDepartement") == null ? "" : String.valueOf(request.getAttribute("codeDepartement"));
    String codeCommune = request.getAttribute("codeCommune") == null ? "" : String.valueOf(request.getAttribute("codeCommune"));
    String codeFederation = request.getAttribute("codeFederation") == null ? "" : String.valueOf(request.getAttribute("codeFederation"));
    String genre = request.getAttribute("genre") == null ? "TOTAL" : String.valueOf(request.getAttribute("genre"));
    String communeLibelle = request.getAttribute("communeLibelle") == null ? "" : String.valueOf(request.getAttribute("communeLibelle"));
    List<Map<String, String>> regions = (List<Map<String, String>>) request.getAttribute("regions");
    List<Map<String, String>> departements = (List<Map<String, String>>) request.getAttribute("departements");
    List<Map<String, String>> federations = (List<Map<String, String>>) request.getAttribute("federations");
    StatLicenceElu stat = (StatLicenceElu) request.getAttribute("stat");
%>
<%!
    static String escAttr(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;");
    }
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
            <a href="<%= request.getContextPath() %>/elu/cartographie" class="nav-link"><i class="fas fa-map-marked-alt"></i> Cartographie</a>
            <a href="<%= request.getContextPath() %>/elu/licences" class="nav-link"><i class="fas fa-file-export"></i> Exports DATA</a>
        </div>
        <div class="mt-auto p-3">
            <a href="<%= request.getContextPath() %>/api/logout" class="btn btn-outline-light w-100 btn-sm mb-2">
                <i class="fas fa-sign-out-alt"></i> Déconnexion
            </a>
            <a href="<%= request.getContextPath() %>/elu" class="btn btn-link btn-sm w-100 text-muted text-decoration-none">
                Retour au dashboard
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
                    <form method="post" action="<%= request.getContextPath() %>/elu/licences" class="row g-3" id="licences-filters-form" autocomplete="off">
                        <div class="col-md-2">
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
                        <div class="col-md-2">
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
                        <div class="col-md-4 position-relative">
                            <label class="form-label" for="communeSearch">Commune</label>
                            <input class="form-control" id="communeSearch" type="search"
                                   placeholder="Tapez un nom ou un code INSEE…" maxlength="120"
                                   value="<%= escAttr(communeLibelle) %>">
                            <input type="hidden" id="codeCommune" name="codeCommune" value="<%= escAttr(codeCommune) %>">
                            <div id="communeGeoAlert" class="alert alert-warning py-2 px-3 mt-2 mb-0 small d-none" role="alert"></div>
                            <ul class="list-group position-absolute w-100 shadow-sm mt-1 d-none" id="communeSuggestions"
                                style="z-index: 1050; max-height: 240px; overflow-y: auto;"></ul>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label" for="codeFederation">Fédération</label>
                            <% if (federations == null || federations.isEmpty()) { %>
                            <input class="form-control" id="codeFederation" name="codeFederation" type="text" maxlength="10" required placeholder="Code" value="<%= escAttr(codeFederation) %>">
                            <% } else { %>
                            <select class="form-select" id="codeFederation" name="codeFederation" required>
                                <option value="" disabled <%= codeFederation.isEmpty() ? "selected" : "" %>>Choisir…</option>
                                <% for (Map<String, String> fed : federations) {
                                       String fc = fed.get("code");
                                       String fl = fed.get("label");
                                %>
                                    <option value="<%= fc %>" <%= fc != null && fc.equals(codeFederation) ? "selected" : "" %>><%= fl %></option>
                                <% } %>
                            </select>
                            <% } %>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label" for="genre">Genre</label>
                            <select class="form-select" id="genre" name="genre">
                                <option value="TOTAL" <%= "TOTAL".equalsIgnoreCase(genre) ? "selected" : "" %>>Total</option>
                                <option value="F" <%= "F".equalsIgnoreCase(genre) ? "selected" : "" %>>Femmes</option>
                                <option value="H" <%= "H".equalsIgnoreCase(genre) ? "selected" : "" %>>Hommes</option>
                            </select>
                        </div>
                        <div class="col-12 d-flex flex-wrap gap-2 align-items-center">
                            <button type="submit" class="btn btn-primary px-4 rounded-pill">
                                <i class="fas fa-search me-2"></i>Afficher
                            </button>
                            <button type="submit" class="btn btn-outline-success px-4 rounded-pill"
                                    formaction="<%= request.getContextPath() %>/elu/licences/export"
                                    title="Télécharge un CSV avec une ligne par commune du périmètre choisi">
                                <i class="fas fa-file-csv me-2"></i>Exporter CSV (détail communes)
                            </button>
                            <small class="text-muted">Fichier <strong>.csv</strong> : une ligne d’en-têtes, puis une ligne par commune (filtres inclus). Ouvrez le <strong>.csv</strong> téléchargé, pas un ancien fichier .numbers.</small>
                        </div>
                    </form>
                </div>
            </div>

            <% if (stat != null) { %>
            <div class="results-wrap shadow-sm p-4 p-md-5 mt-5">
                <h2 class="h5 fw-bold mb-4">Synthèse agrégée</h2>
                <div class="row g-3 mb-4">
                    <div class="col-md-4">
                        <div class="kpi-tile">
                            <div class="section-title">Valeur selon genre</div>
                            <div class="fs-4 fw-bold text-primary"><%= stat.getValeurGenre() %></div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="kpi-tile">
                            <div class="section-title">Genre</div>
                            <div class="fw-semibold"><%= escAttr(stat.getGenre()) %></div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="kpi-tile">
                            <div class="section-title">Fédération</div>
                            <div class="fw-semibold small"><%= escAttr(stat.getNomFederation()) %> <span class="text-muted">(<%= escAttr(stat.getCodeFederation()) %>)</span></div>
                        </div>
                    </div>
                </div>
                <div class="table-responsive">
                    <table class="table table-sm table-bordered align-middle mb-0">
                        <tbody>
                            <tr><th class="bg-light" style="width: 28%;">Région</th><td><%= stat.getNomRegion() == null || stat.getNomRegion().isBlank() ? "—" : escAttr(stat.getNomRegion()) %>
                                <% if (stat.getCodeRegion() != null && !stat.getCodeRegion().isBlank()) { %><span class="text-muted">(<%= escAttr(stat.getCodeRegion()) %>)</span><% } %></td></tr>
                            <tr><th class="bg-light">Département</th><td><% if (stat.getCodeDepartement() == null || stat.getCodeDepartement().isBlank()) { %>—<% } else { %><%= escAttr(stat.getCodeDepartement()) %><%
                                if (stat.getNomDepartement() != null && !stat.getNomDepartement().isBlank()) { %> <span class="text-muted">— <%= escAttr(stat.getNomDepartement()) %></span><% } %><% } %></td></tr>
                            <tr><th class="bg-light">Commune</th><td><% if (stat.getNomCommune() == null || stat.getNomCommune().isBlank()) { %>—<% } else { %><%= escAttr(stat.getNomCommune()) %> <span class="text-muted">(<%= escAttr(stat.getCodeCommune()) %>)</span><% } %></td></tr>
                            <tr><th class="bg-light">Total licences</th><td class="fw-semibold"><%= stat.getTotalLicencies() %></td></tr>
                            <tr><th class="bg-light">Licenciées (F)</th><td><%= stat.getLicenciesFemmes() %></td></tr>
                            <tr><th class="bg-light">Licenciés (H)</th><td><%= stat.getLicenciesHommes() %></td></tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <% } else if (request.getMethod() != null && "POST".equalsIgnoreCase(request.getMethod()) && request.getAttribute("erreur") == null && request.getAttribute("codeFederation") != null && !String.valueOf(request.getAttribute("codeFederation")).isBlank()) { %>
            <div class="alert alert-info mt-4">Aucune donnée ne correspond aux critères sélectionnés.</div>
            <% } %>

        </div>
    </main>
</div>

<script>
(function () {
    const ctx = '<%= request.getContextPath() %>';
    const regionSelect = document.getElementById('codeRegion');
    const departementSelect = document.getElementById('codeDepartement');
    const input = document.getElementById('communeSearch');
    const hidden = document.getElementById('codeCommune');
    const list = document.getElementById('communeSuggestions');
    const geoAlert = document.getElementById('communeGeoAlert');
    const form = document.getElementById('licences-filters-form');
    if (!regionSelect || !departementSelect) return;

    let timer = null;

    function regionLabel() {
        const opt = regionSelect.options[regionSelect.selectedIndex];
        return opt && opt.value ? opt.text.trim() : '';
    }

    function showAlert(msg) {
        if (!geoAlert) return;
        if (!msg) {
            geoAlert.classList.add('d-none');
            geoAlert.textContent = '';
        } else {
            geoAlert.textContent = msg;
            geoAlert.classList.remove('d-none');
        }
    }

    function hideList() {
        if (!list) return;
        list.classList.add('d-none');
        list.innerHTML = '';
    }

    function clearCommune() {
        if (input) input.value = '';
        if (hidden) hidden.value = '';
        hideList();
    }

    function filterDepartements() {
        const region = regionSelect.value;
        const current = departementSelect.value;
        let keep = false;
        Array.from(departementSelect.options).forEach(function (opt, i) {
            if (i === 0) {
                opt.disabled = false;
                opt.hidden = false;
                return;
            }
            const ok = !region || opt.getAttribute('data-region') === region;
            opt.disabled = !ok;
            opt.hidden = !ok;
            if (ok && opt.value === current) keep = true;
        });
        if (!keep) departementSelect.value = '';
    }

    function searchUrl(q) {
        let u = ctx + '/elu/licences/communes-search?q=' + encodeURIComponent(q);
        if (regionSelect.value) u += '&codeRegion=' + encodeURIComponent(regionSelect.value);
        if (departementSelect.value) u += '&codeDepartement=' + encodeURIComponent(departementSelect.value);
        return u;
    }

    regionSelect.addEventListener('change', function () {
        filterDepartements();
        clearCommune();
        showAlert('');
    });

    departementSelect.addEventListener('change', function () {
        clearCommune();
        showAlert('');
    });

    filterDepartements();

    if (input && hidden && list) {
        input.addEventListener('input', function () {
            if (timer) clearTimeout(timer);
            const q = input.value.trim();
            if (q.length < 2) {
                hideList();
                if (q.length === 0) hidden.value = '';
                showAlert('');
                return;
            }
            showAlert('');
            timer = setTimeout(function () {
                fetch(searchUrl(q), { headers: { 'Accept': 'application/json' } })
                    .then(function (r) { return r.json(); })
                    .then(function (rows) {
                        list.innerHTML = '';
                        if (!rows || rows.length === 0) {
                            hideList();
                            if (regionSelect.value || departementSelect.value) {
                                const zone = departementSelect.value
                                    ? 'le département sélectionné'
                                    : 'la région « ' + regionLabel() + ' »';
                                showAlert('Cette commune ne fait pas partie de ' + zone + '.');
                            }
                            return;
                        }
                        rows.forEach(function (row) {
                            const btn = document.createElement('button');
                            btn.type = 'button';
                            btn.className = 'list-group-item list-group-item-action py-2 text-start';
                            btn.textContent = row.label || (row.nom + ' — ' + row.code);
                            btn.addEventListener('click', function () {
                                hidden.value = row.code;
                                input.value = row.label || (row.nom + ' — ' + row.code);
                                hideList();
                                showAlert('');
                            });
                            list.appendChild(btn);
                        });
                        list.classList.remove('d-none');
                    })
                    .catch(function () { hideList(); });
            }, 280);
        });

        document.addEventListener('click', function (ev) {
            if (!list.contains(ev.target) && ev.target !== input) hideList();
        });
    }

    if (form) {
        form.addEventListener('submit', function () {
            if (input && input.value.trim().length < 2) hidden.value = '';
        });
    }
})();
</script>
</body>
</html>
