package com.clubsport.admin.dao;

import com.clubsport.admin.model.HistoriqueConnexion;
import com.clubsport.admin.util.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueConnexionDAO {

    // Récupère toutes les connexions enregistrées dans la base
    public List<HistoriqueConnexion> getHistorique() {
        List<HistoriqueConnexion> liste = new ArrayList<>();

        // Requête SQL simple : on récupère tout l'historique
        String sql = "SELECT id, nom, prenom, date_connexion FROM historique_connexion ORDER BY date_connexion DESC";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            // Construction des objets HistoriqueConnexion à partir des résultats SQL
            while (rs.next()) {
                liste.add(new HistoriqueConnexion(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("date_connexion")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    // Ajoute une nouvelle entrée dans l'historique
    public boolean ajouterConnexion(String nom, String prenom) {
        String sql = "INSERT INTO historique_connexion (nom, prenom, date_connexion) VALUES (?, ?, NOW())";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            stmt.setString(2, prenom);

            return stmt.executeUpdate() > 0; // true si insertion OK

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
