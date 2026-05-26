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

    <label>Région</label><br>
    <select id="regionSelect" required>
        <option value="">-- Choisir une région --</option>
        <c:forEach var="r" items="${regions}">
            <option value="${r.code}">${r.nom}</option>
        </c:forEach>
    </select><br><br>

    <label>Commune</label><br>
    <select name="codeCommune" id="communeSelect" required>
        <option value="">-- Choisir une commune --</option>
        <c:forEach var="c" items="${communes}">
            <option value="${c.code}" data-region="${c.codeRegion}">
                ${c.nom} (${c.code})
            </option>
        </c:forEach>
    </select><br><br>

    <label>Fédération</label><br>
    <select name="codeFederation" required>
        <option value="">-- Choisir une fédération --</option>
        <c:forEach var="f" items="${federations}">
            <option value="${f.code}">
                ${f.nom} (${f.code})
            </option>
        </c:forEach>
    </select><br><br>

    <label>Nombre de licenciés</label><br>
    <input type="number" name="nbLicencies" required><br><br>

    <label>Nombre de femmes</label><br>
    <input type="number" name="nbFemmes" required><br><br>

    <label>Nombre d’hommes</label><br>
    <input type="number" name="nbHommes" required><br><br>

    <button type="submit">Créer</button>

</form>

<br>

<a href="${pageContext.request.contextPath}/acteur">
    <button type="button">Retour</button>
</a>

<script>
    const regionSelect = document.getElementById("regionSelect");
    const communeSelect = document.getElementById("communeSelect");
    const allCommunes = Array.from(communeSelect.options);

    regionSelect.addEventListener("change", function () {
        const selectedRegion = this.value;

        communeSelect.innerHTML = "";

        allCommunes.forEach(option => {
            if (option.value === "") {
                communeSelect.appendChild(option);
                return;
            }

            if (option.dataset.region === selectedRegion) {
                communeSelect.appendChild(option);
            }
        });

        communeSelect.value = "";
    });
</script>

</body>
</html>
