<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Publier actualité</title>

<style>
body {
    font-family: Arial, sans-serif;
    background: #f4f6f9;
    margin: 0;
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
    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

h2 {
    margin-bottom: 20px;
}

.actualite-text {
    white-space: pre-line;
    line-height: 1.6;
    font-size: 16px;
    max-height: 500px;
    overflow-y: auto;
    padding-right: 10px;
}

textarea {
    width: 100%;
    height: 320px;
    padding: 15px;
    font-size: 16px;
    box-sizing: border-box;
    border-radius: 8px;
    border: 1px solid #ccc;
    resize: vertical;
}

button {
    margin-top: 20px;
    padding: 12px 20px;
    background: #2563eb;
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
}

button:hover {
    background: #1d4ed8;
}

.retour-btn {
    display: inline-block;
    margin-left: 15px;
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

    <!-- 左边 -->
    <div class="card">
        <h2>Actualité actuelle</h2>

        <div class="actualite-text">
            ${club.actualite}
        </div>
    </div>

    <!-- 右边 -->
    <div class="card">
        <h2>Publier une actualité</h2>

        <form action="${pageContext.request.contextPath}/acteur/actualites" method="post">
            <textarea 
                name="actualite"
                placeholder="Écrivez une nouvelle actualité..."
                required></textarea>

            <button type="submit">Publier</button>

            <a href="${pageContext.request.contextPath}/acteur" class="retour-btn">
                Retour
            </a>
        </form>
    </div>

</div>

</body>
</html>
