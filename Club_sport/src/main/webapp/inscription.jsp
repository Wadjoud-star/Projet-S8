<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Page de création de compte</title>
<link rel="stylesheet" href="Style.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<div class="card">
		<div class="brand">
			<h1>⚽ Club de sport</h1>
			<p>Créez un compte</p>
		</div>
		<form id="login-form" method="post" action="api/inscription"
			enctype="multipart/form-data">
			<div class="row mb-3">
				<div class="col">
					<label for="prenom" class="form-label">Prénom</label> <input
						type="text" id="prenom" class="form-control" placeholder="Jean"
						name="prenom" required />
				</div>
				<div class="col">
					<label for="nom" class="form-label">Nom</label> <input type="text"
						id="nom" class="form-control" placeholder="Dupont" name="nom"
						required />
				</div>
			</div>
			<div class="mb-3">
				<label for="email" class="form-label">Adresse e-mail</label> <input
					type="email" id="email" class="form-control"
					placeholder="vous@exemple.com" name="email" required />
			</div>
			<div class="mb-4">
				<label for="password" class="form-label">Mot de passe</label> <input
					type="password" id="password" class="form-control"
					placeholder="Mot de passe" name="password" name="password" required />
			</div>
			<div class="mb-4">
				<label for="password-confirm" class="form-label">Confirmer
					le mot de passe</label> <input type="password" id="password-confirm"
					class="form-control" placeholder="Retaper le mot de passe"
					name="confirm" required />
			</div>
			<div class="mb-4">
				<label for="type" class="form-label">Vous êtes :</label> <select
					class="form-select" id="type" name="type" required>
					<option value="acteur">Acteur du monde sportif (President
						de club, entraîneur...)</option>
					<option value="elu">Elu (maires, députés...)</option>
				</select>
			</div>
			<div class="mb-4">
				<label for="piece-identite" class="form-label">Veuillez
					joindre une pièce d'identité pour vérification</label><input type="file"
					id="identite" name="identite" required>
			</div>
			<div class="text-center">
				<button type="submit" class="btn btn-primary px-5" id="submit-btn">
					S'inscrire</button>
			</div>
		</form>
		<p class="text-center mt-3 text-muted" style="font-size: 0.88rem">
			Vous avez un statut particulier et avez un compte ? <a
				href="authentification.jsp" class="text-success">Se connecter</a>
		</p>
		<p class="text-center mt-3">
			<a href="index.html" class="text-decoration-none"> ← Retour à la
				page d'accueil </a>
		</p>
		<% String erreur = (String) session.getAttribute("erreur"); if(erreur
		!= null){ session.removeAttribute("erreur"); %>
		<div class="alert alert-danger"><%= erreur %></div>
		<% } %>
	</div>
</body>
</html>