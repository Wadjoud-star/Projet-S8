<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>

<%
List<Map<String, String>> regions =
    (List<Map<String, String>>) request.getAttribute("regions");

List<Map<String, String>> communes =
    (List<Map<String, String>>) request.getAttribute("communes");

List<Map<String, String>> federations =
    (List<Map<String, String>>) request.getAttribute("federations");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Créer mon club</title>
</head>
<body>

<h1>Créer mon club</h1>

<form action="<%= request.getContextPath() %>/acteur/creer-club" method="post">

    <label>Nom du club</label><br>
    <input type="text" name="nom" required><br><br>

    <label>Adresse</label><br>
    <input type="text" name="adresse" required><br><br>

    <label>Code postal</label><br>
    <input type="text" name="codePostal" required><br><br>

    <label>Région</label><br>
    <select id="regionSelect" required>
        <option value="">-- Choisir une région --</option>

        <% for (Map<String, String> r : regions) { %>
            <option value="<%= r.get("code") %>">
                <%= r.get("nom") %>
            </option>
        <% } %>
    </select><br><br>

    <label>Commune</label><br>
    <select name="codeCommune" id="communeSelect" required>
        <option value="">-- Choisir une commune --</option>

        <% for (Map<String, String> c : communes) { %>
            <option
                value="<%= c.get("code") %>"
                data-region="<%= c.get("codeRegion") %>">
                <%= c.get("nom") %> - <%= c.get("code") %>
            </option>
        <% } %>
    </select><br><br>

    <label>Fédération</label><br>
    <select name="codeFederation" required>
        <option value="">-- Choisir une fédération --</option>

        <% for (Map<String, String> f : federations) { %>
            <option value="<%= f.get("code") %>">
                <%= f.get("nom") %>
            </option>
        <% } %>
    </select><br><br>

    <label>Nombre de licenciés</label><br>
    <input type="number" name="nbLicencies" required><br><br>

    <label>Nombre de femmes</label><br>
    <input type="number" name="nbFemmes" required><br><br>

    <label>Nombre d’hommes</label><br>
    <input type="number" name="nbHommes" required><br><br>

    <button type="submit">Créer</button>
</form>

<script>
const regionSelect = document.getElementById("regionSelect");
const communeSelect = document.getElementById("communeSelect");
const allOptions = Array.from(communeSelect.options);

regionSelect.addEventListener("change", function () {
    const selectedRegion = this.value;

    communeSelect.innerHTML =
        '<option value="">-- Choisir une commune --</option>';

    allOptions.forEach(option => {
        if (!option.value) return;

        if (option.dataset.region === selectedRegion) {
            communeSelect.appendChild(option.cloneNode(true));
        }
    });
});
</script>

</body>
</html>
