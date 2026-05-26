
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Publier actualité</title>

<style>
body {
    font-family: Arial;
    background: #f4f6f9;
}

.container {
    width: 90%;
    margin: 40px auto;
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 30px;
}

.card {
    background: white;
    padding: 30px;
    border-radius: 15px;
}

textarea {
    width: 100%;
    height: 250px;
    padding: 15px;
    font-size: 16px;
}

button {
    margin-top: 20px;
    padding: 12px 20px;
    background: #2563eb;
    color: white;
    border: none;
    border-radius: 8px;
}
        .retour-btn {
    display: inline-block;
    margin-bottom: 20px;
    padding: 10px 18px;
    background: white;
    color: #1e3a5f;
    text-decoration: none;
    border-radius: 8px;
    font-weight: bold;
    transition: 0.2s;
}

.retour-btn:hover {
    background: #f3f4f6;
}
</style>
</head>

<body>

<div class="container">

    <div class="card">
        <h2>Actualité actuelle</h2>
        <p>${club.actualite}</p>
    </div>

    <div class="card">
        <h2>Publier une actualité</h2>
<form action="${pageContext.request.contextPath}/acteur/actualites" method="post">
            <textarea name="actualite">${club.actualite}</textarea>
            <button type="submit">Publier</button>
            <a href="/acteur" class="retour-btn"> Retour</a>
        </form>
    </div>

</div>

</body>
</html>
