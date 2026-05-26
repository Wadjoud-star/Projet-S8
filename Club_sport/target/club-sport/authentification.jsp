<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Page de connexion</title>
<link rel="stylesheet" href="Style.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<div class="card">
		<%
			String erreur = (String) session.getAttribute("erreur");
			if(erreur != null){
				session.removeAttribute("erreur");
		%>
		<div class="alert alert-danger">
			<%= erreur %>
		</div>
		<%
    }
%>
		<div class="brand">
			<h1>⚽ Club de sport</h1>
			<p>Connectez-vous A  votre espace</p>
		</div>
		<form id="login-form" method="post" action="api/login">
			<div class="mb-3">
				<label for="email" class="form-label">Adresse e-mail</label> <input
					type="email" id="email" class="form-control" name="email"
					placeholder="Email" required />
			</div>
			<div class="mb-4">
				<label for="password" class="form-label">Mot de passe</label> <input
					type="password" id="password" class="form-control"

					placeholder="Mot de passe" name="password" required />
			</div>
			<p class="text-muted small mb-4">Votre espace (élu ou
				acteur) est défini lors de l'inscription.</p>

					placeholder="•••••••" name="password" required/>
			</div>
			<p class="text-muted small mb-4">Votre espace (&eacute;lu ou acteur) est d&eacute;fini lors de l'inscription.</p>

			<div class="text-center">
				<button type="submit" class="btn btn-success" id="submit-btn">
					Se connecter</button>
			</div>
		</form>
		<p class="text-center mt-3 text-muted" style="font-size: 0.88rem">
			Vous avez un statut particulier et pas de compte ? <a
				href="inscription.jsp" class="text-success">S'inscrire</a>
		</p>
		<p class="text-center mt-3">
			<a href="index.html" class="text-decoration-none">← Retour à la
				page d'accueil </a>
		</p>
	</div>
</body>
</html>