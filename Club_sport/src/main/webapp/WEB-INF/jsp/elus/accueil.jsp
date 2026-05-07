
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Espace élus</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/Style.css">
</head>
<body class="bg-light">
<div class="container py-5">
  <h1 class="mb-3">Espace élus</h1>
  <p class="lead">Consultation des indicateurs (version sprint : licences par commune et fédération).</p>
  <ul class="list-group mb-4">
    <li class="list-group-item">
      <a href="<%= request.getContextPath() %>/elu/licences">Statistiques licences (total, femmes, hommes)</a>
    </li>
  </ul>
  <p><a href="<%= request.getContextPath() %>/index.html">Retour au site public</a></p>
</div>
</body>
</html>
