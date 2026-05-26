<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.clubsport.model.User" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mon espace | SportData</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #f8f9fa; color: #334155; }
        .sidebar { min-width: 260px; max-width: 260px; min-height: 100vh; background: #0f766e; color: white; display: flex; flex-direction: column; }
        .sidebar .nav-link { color: #ccfbf1; padding: 12px 20px; border-radius: 8px; margin: 5px 15px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: rgba(255,255,255,0.15); color: white; }
        .sidebar .nav-link i { margin-right: 10px; width: 20px; text-align: center; }
        .main-content { flex-grow: 1; padding: 40px; }
        .welcome-banner { background: linear-gradient(135deg, #0d9488 0%, #14b8a6 100%); color: white; border-radius: 12px; padding: 1.5rem 2rem; }
        .card-tile { border: none; border-radius: 12px; background: white; transition: transform 0.2s; }
        .card-tile:hover { transform: translateY(-4px); box-shadow: 0 8px 20px rgba(0,0,0,0.06); }
        .profile-recap { border: none; border-radius: 12px; background: white; }
        .avatar { width: 96px; height: 96px; object-fit: cover; border-radius: 50%; border: 3px solid #14b8a6; }
        .avatar-placeholder { width: 96px; height: 96px; border-radius: 50%; background: #e2e8f0; display: flex; align-items: center; justify-content: center; font-size: 2rem; color: #64748b; }
        .fed-badge { background: #ecfdf5; color: #0f766e; font-size: 0.8rem; }
    </style>
</head>
<body>
<%
    String ctx = request.getContextPath();
    String prenom = request.getAttribute("prenom") == null ? "utilisateur" : String.valueOf(request.getAttribute("prenom"));
    User user = (User) request.getAttribute("user");
    List<String> federationsLabels = (List<String>) request.getAttribute("federationsLabels");
    if (federationsLabels == null) federationsLabels = List.of();

    String photoUrl = "";
    if (user != null && user.getPhotoProfil() != null && !user.getPhotoProfil().isBlank()) {
        photoUrl = ctx + "/" + user.getPhotoProfil();
    }
%>
<div class="d-flex">
    <nav class="sidebar shadow">
        <div class="p-4 text-center">
            <h4 class="fw-bold mb-0">Sport<span class="text-warning">Data</span></h4>
            <small class="text-white-50">Espace membre</small>
        </div>
        <hr class="mx-3 border-light opacity-25">
        <div class="nav flex-column flex-grow-1">
            <a href="<%= ctx %>/utilisateur" class="nav-link active"><i class="fas fa-home"></i> Accueil</a>
            <a href="<%= ctx %>/utilisateur/profil" class="nav-link"><i class="fas fa-user-circle"></i> Mon profil</a>
        </div>
        <div class="p-3 mt-auto">
            <a href="<%= ctx %>/api/logout" class="btn btn-outline-light w-100 btn-sm mb-2">
                <i class="fas fa-sign-out-alt"></i> D&eacute;connexion
            </a>
            <a href="<%= ctx %>/index.html" class="btn btn-link btn-sm w-100 text-white-50 text-decoration-none">
                Site public
            </a>
        </div>
    </nav>

    <main class="main-content">
        <div class="welcome-banner mb-4 shadow-sm">
            <h1 class="h3 fw-bold mb-1">Bonjour <%= prenom %> !</h1>
            <p class="mb-0 opacity-90">Bienvenue dans votre espace personnel SportData.</p>
        </div>

        <% if (request.getAttribute("erreur") != null) { %>
            <div class="alert alert-warning mb-4"><%= request.getAttribute("erreur") %></div>
        <% } %>

        <div class="card profile-recap shadow-sm p-4 p-md-5 mb-4">
            <div class="d-flex flex-wrap justify-content-between align-items-start gap-2 mb-4">
                <h2 class="h5 fw-bold mb-0"><i class="fas fa-id-card me-2 text-success"></i>Mon profil</h2>
                <a href="<%= ctx %>/utilisateur/profil" class="btn btn-success btn-sm rounded-pill px-3">
                    <i class="fas fa-pen me-1"></i> Modifier
                </a>
            </div>

            <% if (user == null) { %>
                <p class="text-muted mb-0">Profil indisponible. <a href="<%= ctx %>/utilisateur/profil">Compl&eacute;ter mon profil</a></p>
            <% } else { %>
            <div class="row g-4 align-items-start">
                <div class="col-auto text-center">
                    <% if (!photoUrl.isEmpty()) { %>
                        <img src="<%= photoUrl %>" alt="Photo de profil" class="avatar">
                    <% } else { %>
                        <div class="avatar-placeholder"><i class="fas fa-user"></i></div>
                    <% } %>
                </div>
                <div class="col">
                    <p class="mb-1"><span class="text-muted small">Nom</span><br><strong><%= user.getNom() %></strong></p>
                    <p class="mb-1"><span class="text-muted small">E-mail</span><br><%= user.getEmail() %></p>
                    <p class="mb-1"><span class="text-muted small">T&eacute;l&eacute;phone</span><br>
                        <% if (user.getTelephone() != null && !user.getTelephone().isBlank()) { %>
                            <%= user.getTelephone() %>
                        <% } else { %>
                            <span class="text-muted fst-italic">Non renseign&eacute;</span>
                        <% } %>
                    </p>
                    <p class="mb-2"><span class="text-muted small">Pr&eacute;sentation</span></p>
                    <% if (user.getBio() != null && !user.getBio().isBlank()) { %>
                        <p class="mb-2" style="white-space: pre-wrap;"><%= user.getBio() %></p>
                    <% } else { %>
                        <p class="mb-2 text-muted fst-italic">Non renseign&eacute;e</p>
                    <% } %>
                    <p class="mb-1"><span class="text-muted small">Sports / f&eacute;d&eacute;rations favorites</span></p>
                    <% if (federationsLabels.isEmpty()) { %>
                        <span class="text-muted fst-italic small">Aucune s&eacute;lection</span>
                    <% } else { %>
                        <div class="d-flex flex-wrap gap-2">
                            <% for (String label : federationsLabels) { %>
                                <span class="badge rounded-pill fed-badge px-3 py-2"><%= label %></span>
                            <% } %>
                        </div>
                    <% } %>
                </div>
            </div>
            <% } %>
        </div>

        <div class="row g-4">
            <div class="col-md-12">
                <div class="card card-tile shadow-sm p-4 h-100 border-start border-4 border-warning">
                    <div class="mb-3" style="font-size:2rem;color:#ca8a04;"><i class="fas fa-bell"></i></div>
                    <h2 class="h5 fw-bold">Fil &amp; abonnements</h2>
                    <p class="text-muted small mb-0">Publications des acteurs sportifs, likes et notifications &mdash; bient&ocirc;t sur l&apos;application mobile.</p>
                </div>
            </div>
        </div>
    </main>
</div>
</body>
</html>
