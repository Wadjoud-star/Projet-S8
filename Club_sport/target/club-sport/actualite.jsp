<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.clubsport.model.Publication"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Actualités des clubs</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" href="Actualite.css">
<link rel="stylesheet" href="Style.css">
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-custom fixed-top">
		<div class="container-fluid">
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarText"
				aria-controls="navbarText" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarText">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item"><a class="nav-link" href="index.html">⚽
							Accueil</a></li>
					<li class="nav-item"><a class="nav-link" href="propos.html">ℹ️
							À Propos</a></li>
					<li class="nav-item"><a class="nav-link" href="actualite.jsp">
							📰 Actualités des clubs</a></li>
				</ul>
				<ul class="navbar-nav">
					<li class="nav-item"><a class="nav-link"
						href="authentification.jsp">Connexion</a></li>
					<li class="nav-item"><a class="nav-link"
						href="inscription.html">Inscription</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container pt-5 mt-4">
		<h4 class="fw-semibold mb-4">📰 Actualités des clubs</h4>
		<%
		List<Publication> publications = (List<Publication>) request.getAttribute("publications");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		if (publications == null || publications.isEmpty()) {
		%>
		<div class="alert alert-info">Aucune publication disponible pour
			le moment</div>
		<%
		} else {
		%>
		<div class="row g-4">
			<%
			for (Publication pub : publications) {
			%>
			<div class="col-12 col-md-6 col-lg-4">
				<div class="card card-publication h-100">
					<%
					if (pub.getImageUrl() != null && !pub.getImageUrl().isEmpty()) {
					%>
					<img src="<%=pub.getImageUrl()%>" class="card-img-top"
						alt="<%=pub.getTitre()%>">
					<%
					} else {
					%>
					<div
						style="height: 200px; background: #f0f4ff; display: flex; align-items: center; justify-content: center;">
						<span style="font-size: 48px;">⚽</span>
					</div>
					<%
					}
					%>

					<div class="card-body d-flex flex-column">
						<div
							class="d-flex justify-content-between align-items-center mb-2">
							<span class="badge-date">  <%=pub.getDatePublication() != null ? sdf.format(pub.getDatePublication()) : "Date inconnue"%>
							</span> <span class="auteur"> <%=pub.getNomAuteur() != null ? pub.getNomAuteur() : "Anonyme"%></span>
						</div>
						<h5 class="card-title fw-semibold"><%=pub.getTitre()%></h5>

						<p class="card-text text-muted small flex-grow-1">
							<%
							String contenu = pub.getContenu();
							if (contenu != null) {
								out.print(contenu);
							} else {
								out.print(contenu != null ? contenu : "");
							}
							%>
						</p>
					</div>
				</div>
			</div>
			<%
			}
			%>
		</div>
		<%
		}
		%>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>