<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Créer mon club</title>
</head>
<body>

<h1>Créer mon club</h1>

<p style="color:red;">${message}</p>

<form action="${pageContext.request.contextPath}/acteur/creer-club" method="post">

    <label>Nom du club</label><br>
    <input type="text" name="nom" required><br><br>

    <label>Adresse</label><br>
    <input type="text" name="adresse" required><br><br>

    <label>Code postal</label><br>
    <input type="text" name="codePostal" required><br><br>

    <label>Nombre de licenciés</label><br>
    <input type="number" name="nbLicencies" required><br><br>

    <label>Nombre de femmes</label><br>
    <input type="number" name="nbFemmes" required><br><br>

    <label>Nombre d’hommes</label><br>
    <input type="number" name="nbHommes" required><br><br>

    <button type="submit">Créer</button>

</form>

<br>
<a href="${pageContext.request.contextPath}/acteur">Retour</a>

</body>
</html>