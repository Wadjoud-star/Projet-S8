<<<<<<< HEAD

=======
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
>>>>>>> 0a4ad8d5c00de387559b527b3aa3c30741c9471c
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Espace Élus | Dashboard</title>
    
    <!-- Bootstrap 5 & Google Fonts -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <!-- Icons FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #f8f9fa; color: #334155; }
        .sidebar { min-width: 280px; max-width: 280px; min-height: 100vh; background: #1e293b; color: white; transition: all 0.3s; }
        .sidebar .nav-link { color: #cbd5e1; padding: 12px 20px; border-radius: 8px; margin: 5px 15px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: #334155; color: white; }
        .sidebar .nav-link i { margin-right: 10px; width: 20px; text-align: center; }
        .main-content { flex-grow: 1; padding: 40px; }
        .stat-card { border: none; border-radius: 12px; transition: transform 0.2s, box-shadow 0.2s; background: white; }
        .stat-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.05); }
        .icon-box { width: 48px; height: 48px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-bottom: 15px; }
    </style>
</head>
<body>

<div class="d-flex">
    <!-- SIDEBAR -->
    <nav class="sidebar shadow">
        <div class="p-4 text-center">
            <h4 class="fw-bold mb-0">Sport<span class="text-primary">Data</span></h4>
            <small class="text-muted">Portail Collectivités</small>
        </div>
        <hr class="mx-3 border-secondary">
        <div class="nav flex-column">
            <a href="#" class="nav-link active"><i class="fas fa-th-large"></i> Dashboard</a>
            <a href="<%= request.getContextPath() %>/elu/licences" class="nav-link"><i class="fas fa-chart-line"></i> Statistiques Licences</a>
            <!-- Futurs modules ici -->
            <a href="#" class="nav-link"><i class="fas fa-map-marked-alt"></i> Cartographie</a>
            <a href="#" class="nav-link"><i class="fas fa-file-export"></i> Exports DATA</a>
        </div>
        <div class="mt-auto p-3">
            <a href="<%= request.getContextPath() %>/index.html" class="btn btn-outline-light w-100 btn-sm">
                <i class="fas fa-sign-out-alt"></i> Quitter l'espace
            </a>
        </div>
    </nav>

    <!-- MAIN CONTENT -->
    <main class="main-content">
        <div class="container-fluid">
            <div class="mb-5">
                <h1 class="h2 fw-bold mb-1">Espace Élus</h1>
                <p class="text-muted mb-0">Bienvenue sur votre outil d'analyse territoriale.</p>
            </div>

            <!-- GRID DES MODULES -->
            <div class="row g-4">
                <!-- Carte Statistiques -->
                <div class="col-md-6 col-lg-4">
                    <div class="card stat-card shadow-sm h-100">
                        <div class="card-body p-4">
                            <div class="icon-box bg-primary-subtle text-primary">
                                <i class="fas fa-users-viewfinder fs-4"></i>
                            </div>
                            <h5 class="fw-bold">Statistiques Licences</h5>
                            <p class="text-muted small">Analyse croisée par commune, genre et fédération sur votre territoire.</p>
                            <a href="<%= request.getContextPath() %>/elu/licences" class="btn btn-primary btn-sm w-100 rounded-pill mt-3">
                                Accéder au module
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Future Carte (Placeholder) -->
                <div class="col-md-6 col-lg-4">
                    <div class="card stat-card shadow-sm h-100 opacity-75 border-dashed">
                        <div class="card-body p-4 d-flex flex-column align-items-center justify-content-center text-center">
                            <i class="fas fa-plus-circle fs-1 text-light-emphasis mb-3"></i>
                            <h5 class="text-muted">Nouveau module</h5>
                            <p class="small text-muted">Bientôt disponible pour vos analyses.</p>
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