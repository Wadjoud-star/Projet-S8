
<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Gérer les horaires</title>
<style>
body { font-family: Arial; background: #f4f6f9; }
.container { width: 90%; margin: 40px auto; display: grid; grid-template-columns: 1fr 1fr; gap: 30px; }
.card { background: white; padding: 30px; border-radius: 15px; box-shadow: 0 5px 18px rgba(0,0,0,0.08); }
textarea { width: 100%; height: 260px; padding: 15px; font-size: 16px; border-radius: 8px; border: 1px solid #ccc; }
button { margin-top: 20px; padding: 12px 20px; background: #2563eb; color: white; border: none; border-radius: 8px; cursor: pointer; }
button:hover { background: #1d4ed8; }
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
        <h2>Horaires actuels</h2>
        <p>${club.horaires}</p>
    </div>

    <div class="card">
        <h2>Modifier les horaires</h2>
        <form action="/acteur/gerer-horaires" method="post">
            <textarea name="horaires">${club.horaires}</textarea>
            <button type="submit">Enregistrer</button>
            <a href="/acteur" class="retour-btn"> Retour</a>
        </form>
    </div>
</div>

</body>
</html>