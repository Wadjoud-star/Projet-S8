<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Espace &Eacute;lus | Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #f8f9fa; color: #334155; }
        .sidebar { min-width: 280px; max-width: 280px; min-height: 100vh; background: #1e293b; color: white; display: flex; flex-direction: column; }
        .sidebar .nav-link { color: #cbd5e1; padding: 12px 20px; border-radius: 8px; margin: 5px 15px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: #334155; color: white; }
        .sidebar .nav-link i { margin-right: 10px; width: 20px; text-align: center; }
        .main-content { flex-grow: 1; padding: 40px; }
        .stat-card { border: none; border-radius: 12px; transition: transform 0.2s, box-shadow 0.2s; background: white; }
        .stat-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.05); }
        .icon-box { width: 48px; height: 48px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-bottom: 15px; }
        .welcome-banner { background: linear-gradient(135deg, #1e40af 0%, #3b82f6 100%); color: white; border-radius: 12px; padding: 1.5rem 2rem; }
    </style>
</head>
<body>
<%
    String prenom = request.getAttribute("prenom") == null ? "\u00e9lu" : String.valueOf(request.getAttribute("prenom"));
    String ctx = request.getContextPath();
%>
<div class="d-flex">
    <nav class="sidebar shadow">
        <div class="p-4 text-center">
            <h4 class="fw-bold mb-0">Sport<span class="text-primary">Data</span></h4>
            <small class="text-muted">Portail Collectivit&eacute;s</small>
        </div>
        <hr class="mx-3 border-secondary">
        <div class="nav flex-column flex-grow-1">
            <a href="<%= ctx %>/elu" class="nav-link active"><i class="fas fa-th-large"></i> Dashboard</a>
            <a href="<%= ctx %>/elu/licences" class="nav-link"><i class="fas fa-chart-line"></i> Statistiques Licences</a>
            <a href="<%= ctx %>/elu/visualisation" class="nav-link"><i class="fas fa-chart-pie"></i> Visualisation</a>
            <a href="<%= ctx %>/elu/cartographie" class="nav-link"><i class="fas fa-map-marked-alt"></i> Cartographie</a>
            <a href="<%= ctx %>/elu/licences" class="nav-link"><i class="fas fa-file-export"></i> Exports CSV</a>
        </div>
        <div class="p-3 mt-auto">
            <a href="<%= ctx %>/api/logout" class="btn btn-outline-light w-100 btn-sm mb-2">
                <i class="fas fa-sign-out-alt"></i> D&eacute;connexion
            </a>
            <a href="<%= ctx %>/index.html" class="btn btn-link btn-sm w-100 text-muted text-decoration-none">
                Retour au site public
            </a>
        </div>
    </nav>

    <main class="main-content">
        <div class="container-fluid">
            <div class="welcome-banner mb-4 shadow-sm">
                <h1 class="h3 fw-bold mb-1">Bonjour <%= prenom %></h1>
                <p class="mb-0 opacity-90">Bienvenue dans votre espace &eacute;lu &mdash; analyse territoriale des licences sportives.</p>
            </div>
            <div class="mb-4">
                <h2 class="h5 fw-bold text-muted mb-0">Vos modules</h2>
            </div>

            <div class="row g-4">
                <div class="col-md-6 col-lg-4">
                    <div class="card stat-card shadow-sm h-100">
                        <div class="card-body p-4">
                            <div class="icon-box bg-primary-subtle text-primary">
                                <i class="fas fa-users-viewfinder fs-4"></i>
                            </div>
                            <h5 class="fw-bold">Statistiques Licences</h5>
                            <p class="text-muted small">Analyse crois&eacute;e par commune, genre et f&eacute;d&eacute;ration.</p>
                            <a href="<%= ctx %>/elu/licences" class="btn btn-primary btn-sm w-100 rounded-pill mt-3">Acc&eacute;der</a>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 col-lg-4">
                    <div class="card stat-card shadow-sm h-100">
                        <div class="card-body p-4">
                            <div class="icon-box bg-success-subtle text-success">
                                <i class="fas fa-chart-pie fs-4"></i>
                            </div>
                            <h5 class="fw-bold">Visualisation</h5>
                            <p class="text-muted small">Graphiques H/F, classement des communes et export PDF.</p>
                            <a href="<%= ctx %>/elu/visualisation" class="btn btn-success btn-sm w-100 rounded-pill mt-3">Acc&eacute;der</a>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 col-lg-4">
                    <div class="card stat-card shadow-sm h-100">
                        <div class="card-body p-4">
                            <div class="icon-box bg-info-subtle text-info">
                                <i class="fas fa-map-marked-alt fs-4"></i>
                            </div>
                            <h5 class="fw-bold">Cartographie</h5>
                            <p class="text-muted small">Carte choropl&egrave;the par r&eacute;gion et d&eacute;tail communal.</p>
                            <a href="<%= ctx %>/elu/cartographie" class="btn btn-info btn-sm w-100 rounded-pill mt-3 text-white">Ouvrir la carte</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
