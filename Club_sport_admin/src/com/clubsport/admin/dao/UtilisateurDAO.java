package com.clubsport.admin.dao;

import com.clubsport.admin.model.Utilisateur;
import com.clubsport.admin.util.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    // Récupère tous les utilisateurs d'un rôle donné (admin, club, federation)
    public List<Utilisateur> getUtilisateursParRole(String role) {
        List<Utilisateur> liste = new ArrayList<>();

        // Requête SQL filtrée par rôle
        String sql = "SELECT id, nom, email, mot_de_passe_hash, role FROM utilisateur WHERE role = ?";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Injection du rôle dans la requête
            stmt.setString(1, role);

            ResultSet rs = stmt.executeQuery();

            // Construction des objets Utilisateur à partir des résultats SQL
            while (rs.next()) {
                liste.add(new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe_hash"),
                        rs.getString("role")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    // Supprime un utilisateur par son ID
    public boolean supprimerUtilisateur(int id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0; // true si suppression OK

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Met à jour  nom, email et rôle utilisateur
    public boolean modifierUtilisateur(Utilisateur u) {
        String sql = "UPDATE utilisateur SET nom = ?, email = ?, role = ? WHERE id = ?";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getRole());
            stmt.setInt(4, u.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
