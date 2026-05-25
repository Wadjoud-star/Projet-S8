<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion club</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/WEB-INF/jsp/acteur/club-detail.css">

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f9;
            margin: 0;
        }

        .container {
            width: 85%;
            margin: 40px auto;
        }

        .title {
            background: linear-gradient(135deg, #1f2933, #334e68);
            color: white;
            padding: 35px;
            border-radius: 18px;
            margin-bottom: 30px;
        }

        .content {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
        }

        .card {
            background: white;
            padding: 28px;
            border-radius: 16px;
            box-shadow: 0 5px 18px rgba(0,0,0,0.08);
        }

        .card h2 {
            margin-bottom: 20px;
            color: #1f2933;
        }

        .info p {
            margin-bottom: 12px;
            font-size: 16px;
        }

        label {
            font-weight: bold;
            display: block;
            margin-top: 15px;
        }

        input {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 8px;
        }

        button {
            margin-top: 25px;
            background: #2563eb;
            color: white;
            border: none;
            padding: 12px 22px;
            border-radius: 8px;
            cursor: pointer;
        }

        button:hover {
            background: #1d4ed8;
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

<main class="container">
    <section class="title">
        <h1>Gérer l’espace club</h1>
        <p>Modifier les informations publiques de votre club sportif.</p>
    </section>

    <section class="content">

        <!-- 左边：当前数据 -->
        <div class="card info">
            <h2>Données actuelles</h2>

            <p><strong>Nom :</strong> ${club.nom}</p>
            <p><strong>Adresse :</strong> ${club.adresse}</p>
            <p><strong>Code postal :</strong> ${club.codePostal}</p>
            <p><strong>Nombre de licenciés :</strong> ${club.nbLicencies}</p>
            <p><strong>Femmes :</strong> ${club.nbFemmes}</p>
            <p><strong>Hommes :</strong> ${club.nbHommes}</p>
            <a href="/acteur/membres-club">
    <button type="button">Afficher</button>
</a>
        </div>

        <!-- 右边：修改表单 -->
        <div class="card">
            <h2>Modifier les données</h2>

            <form action="${pageContext.request.contextPath}/acteur/gestion-club" method="post">
                <label>Nom du club</label>
                <input type="text" name="nom" value="${club.nom}" required>

                <label>Adresse</label>
                <input type="text" name="adresse" value="${club.adresse}" required>

                <label>Code postal</label>
                <input type="text" name="codePostal" value="${club.codePostal}" required>

                <label>Nombre de licenciés</label>
                <input type="number" name="nbLicencies" value="${club.nbLicencies}" required>

                <label>Nombre de femmes</label>
                <input type="number" name="nbFemmes" value="${club.nbFemmes}" required>

                <label>Nombre d’hommes</label>
                <input type="number" name="nbHommes" value="${club.nbHommes}" required>

                <button type="submit">Enregistrer</button>
                <a href="/acteur" class="retour-btn"> Retour</a>
            </form>
        </div>

    </section>
</main>

</body>
</html>
