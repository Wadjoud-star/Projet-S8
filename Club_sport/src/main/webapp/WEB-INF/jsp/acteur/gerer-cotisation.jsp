<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gérer les cotisations</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f9;
            margin: 0;
        }

        .container {
            width: 90%;
            margin: 40px auto;
        }

        .content {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
        }

        .card {
            background: white;
            padding: 35px;
            border-radius: 18px;
            box-shadow: 0 5px 18px rgba(0,0,0,0.08);
        }

        h2 {
            margin-bottom: 25px;
            color: #1f2933;
        }

        p {
            font-size: 28px;
            font-weight: bold;
            color: #2563eb;
        }

        label {
            display: block;
            margin-bottom: 12px;
            font-weight: bold;
        }

        input[type="number"] {
            width: 100%;
            padding: 14px;
            font-size: 18px;
            border: 1px solid #ccc;
            border-radius: 10px;
            margin-bottom: 25px;
            box-sizing: border-box;
        }

        button {
            background: #2563eb;
            color: white;
            border: none;
            padding: 12px 22px;
            border-radius: 10px;
            cursor: pointer;
            font-size: 16px;
        }

        button:hover {
            background: #1d4ed8;
        }

        .retour-btn {
            margin-left: 20px;
            text-decoration: none;
            color: #1f2933;
            font-weight: bold;
            font-size: 18px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="content">

        <!-- 左边 -->
        <div class="card">
            <h2>Cotisation actuelle</h2>
            <p>${club.cotisation} €</p>
        </div>

        <!-- 右边 -->
        <div class="card">
            <h2>Modifier la cotisation</h2>

            <form action="${pageContext.request.contextPath}/acteur/cotisations" method="post">

                <label>Montant (€)</label>
                <input
                    type="number"
                    name="cotisation"
                    step="0.01"
                    min="0"
                    value="${club.cotisation}"
                    required
                >

                <button type="submit">Enregistrer</button>

                <a href="${pageContext.request.contextPath}/acteur" class="retour-btn">
                    Retour
                </a>

            </form>
        </div>

    </div>
</div>

</body>
</html>
