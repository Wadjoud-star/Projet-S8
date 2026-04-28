package com.clubsport.admin.dao;

import com.clubsport.admin.model.HistoriqueConnexion;
import com.clubsport.admin.model.Utilisateur;
import com.clubsport.admin.util.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueConnexionDAO {

    /**
     * Récupère tout l'historique des connexions, trié du plus récent au plus ancien.
     */
    public List<HistoriqueConnexion> getHistorique() {
        List<HistoriqueConnexion> liste = new ArrayList<>();

        String sql = """
            SELECT h.id, h.date_connexion, h.adresse_ip, h.login, h.succes,
                   u.id AS uid, u.nom, u.prenom, u.email, u.mot_de_passe_hash, u.role
            FROM historique_connexion h
            LEFT JOIN utilisateur u ON h.id_utilisateur = u.id
            ORDER BY h.date_connexion DESC
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                // Construction de l'utilisateur associé
                Utilisateur user = new Utilisateur(
                        rs.getInt("uid"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe_hash"),
                        rs.getString("role")
                );

                // Construction de l'historique
                HistoriqueConnexion h = new HistoriqueConnexion(
                        rs.getInt("id"),
                        rs.getTimestamp("date_connexion"),
                        rs.getString("adresse_ip"),
                        rs.getString("login"),
                        rs.getBoolean("succes"),
                        user
                );

                liste.add(h);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    /**
     * Ajoute une entrée dans l'historique des connexions.
     */
    public boolean ajouterConnexion(Utilisateur utilisateur, String adresseIP, boolean succes) {

        String sql = """
            INSERT INTO historique_connexion (id_utilisateur, login, adresse_ip, succes, date_connexion)
            VALUES (?, ?, ?, ?, NOW())
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utilisateur.getId());
            stmt.setString(2, utilisateur.getEmail()); // login utilisé
            stmt.setString(3, adresseIP);
            stmt.setBoolean(4, succes);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
