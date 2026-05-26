<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<title>Membres du club</title>
<style>
body { font-family: Arial; background: #f4f6f9; }
.container { width: 85%; margin: 40px auto; background: white; padding: 30px; border-radius: 16px; }
table { width: 100%; border-collapse: collapse; margin-top: 20px; }
th, td { padding: 12px; border-bottom: 1px solid #ddd; text-align: left; }
th { background: #1f2933; color: white; }
button { padding: 10px 16px; background: #2563eb; color: white; border: none; border-radius: 8px; }
</style>
</head>
<body>

<div class="container">
    <h1>Membres du club</h1>

    <a href="/acteur/gestion-club">
        <button>Retour</button>
    </a>

    <table>
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Email</th>
            <th>Rôle</th>
            <th>Statut</th>
        </tr>

        <%
            List<Map<String, String>> membres =
                (List<Map<String, String>>) request.getAttribute("membres");

            if (membres != null) {
                for (Map<String, String> m : membres) {
        %>
        <tr>
            <td><%= m.get("id") %></td>
            <td><%= m.get("nom") %></td>
            <td><%= m.get("email") %></td>
            <td><%= m.get("role") %></td>
            <td><%= m.get("statut") %></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</div>

</body>
</html>