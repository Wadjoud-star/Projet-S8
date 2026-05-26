<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.clubsport.model.User" %>
<%@ page import="com.clubsport.model.Federation" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mon profil | SportData</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #f8f9fa; color: #334155; }
        .sidebar { min-width: 260px; min-height: 100vh; background: #0f766e; color: white; display: flex; flex-direction: column; }
        .sidebar .nav-link { color: #ccfbf1; padding: 12px 20px; border-radius: 8px; margin: 5px 15px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { background: rgba(255,255,255,0.15); color: white; }
        .sidebar .nav-link i { margin-right: 10px; width: 20px; text-align: center; }
        .main-content { flex-grow: 1; padding: 40px; }
        .profile-card { border: none; border-radius: 12px; background: white; }
        .avatar { width: 120px; height: 120px; object-fit: cover; border-radius: 50%; border: 4px solid #14b8a6; }
        .avatar-placeholder { width: 120px; height: 120px; border-radius: 50%; background: #e2e8f0; display: flex; align-items: center; justify-content: center; font-size: 2.5rem; color: #64748b; }
    </style>
</head>
<body>
<%
    String ctx = request.getContextPath();
    User user = (User) request.getAttribute("user");
    List<Federation> federations = (List<Federation>) request.getAttribute("federations");
    List<String> selected = (List<String>) request.getAttribute("federationsSelection");
    if (selected == null) selected = List.of();

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
            <a href="<%= ctx %>/utilisateur" class="nav-link"><i class="fas fa-home"></i> Accueil</a>
            <a href="<%= ctx %>/utilisateur/profil" class="nav-link active"><i class="fas fa-user-circle"></i> Mon profil</a>
        </div>
        <div class="p-3 mt-auto">
            <a href="<%= ctx %>/api/logout" class="btn btn-outline-light w-100 btn-sm">D&eacute;connexion</a>
        </div>
    </nav>

    <main class="main-content">
        <h1 class="h3 fw-bold mb-4">Mon profil</h1>

        <% if (request.getAttribute("succes") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("succes") %></div>
        <% } %>
        <% if (request.getAttribute("erreur") != null) { %>
            <div class="alert alert-danger"><%= request.getAttribute("erreur") %></div>
        <% } %>

        <% if (user == null) { %>
            <div class="alert alert-warning">Profil indisponible.</div>
        <% } else { %>
        <div class="card profile-card shadow-sm p-4 p-md-5">
            <form method="post" action="<%= ctx %>/utilisateur/profil" enctype="multipart/form-data" class="row g-4">
                <div class="col-md-4 text-center">
                    <% if (!photoUrl.isEmpty()) { %>
                        <img src="<%= photoUrl %>" alt="Photo de profil" class="avatar mb-3">
                    <% } else { %>
                        <div class="avatar-placeholder mx-auto mb-3"><i class="fas fa-user"></i></div>
                    <% } %>
                    <label class="form-label" for="photoProfil">Photo de profil</label>
                    <input class="form-control form-control-sm" type="file" id="photoProfil" name="photoProfil" accept="image/png,image/jpeg,image/webp">
                </div>
                <div class="col-md-8">
                    <div class="mb-3">
                        <label class="form-label text-muted">Nom</label>
                        <input type="text" class="form-control" value="<%= user.getNom() %>" readonly disabled>
                    </div>
                    <div class="mb-3">
                        <label class="form-label text-muted">E-mail</label>
                        <input type="email" class="form-control" value="<%= user.getEmail() %>" readonly disabled>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="telephone">T&eacute;l&eacute;phone</label>
                        <input type="tel" class="form-control" id="telephone" name="telephone" maxlength="20"
                               value="<%= user.getTelephone() != null ? user.getTelephone() : "" %>" placeholder="06 12 34 56 78">
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="bio">Pr&eacute;sentation</label>
                        <textarea class="form-control" id="bio" name="bio" rows="4" maxlength="1000"
                                  placeholder="Quelques mots sur vous, vos sports…"><%= user.getBio() != null ? user.getBio() : "" %></textarea>
                    </div>
                    <div class="mb-4">
                        <label class="form-label">Sports / f&eacute;d&eacute;rations favorites</label>
                        <div class="row g-2" style="max-height: 220px; overflow-y: auto;">
                            <% if (federations != null) {
                                   for (Federation fed : federations) {
                                       boolean checked = selected.contains(fed.getCodeFederation());
                            %>
                            <div class="col-md-6">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="federations"
                                           id="fed_<%= fed.getCodeFederation() %>"
                                           value="<%= fed.getCodeFederation() %>"
                                           <%= checked ? "checked" : "" %>>
                                    <label class="form-check-label small" for="fed_<%= fed.getCodeFederation() %>">
                                        <%= fed.getNomFederation() %>
                                    </label>
                                </div>
                            </div>
                            <%   }
                               } else { %>
                                <p class="text-muted small">Aucune f&eacute;d&eacute;ration en base.</p>
                            <% } %>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-success rounded-pill px-4">
                        <i class="fas fa-save me-2"></i>Enregistrer
                    </button>
                    <a href="<%= ctx %>/utilisateur" class="btn btn-link ms-2">Retour</a>
                </div>
            </form>
        </div>
        <% } %>
    </main>
</div>
</body>
</html>
