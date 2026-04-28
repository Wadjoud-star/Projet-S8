package com.clubsport.admin.dao;

import com.clubsport.admin.model.Utilisateur;
import com.clubsport.admin.util.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    /**
     * Récupère tous les utilisateurs d'un rôle donné.
     */
    public List<Utilisateur> getUtilisateursParRole(String role) {
        List<Utilisateur> liste = new ArrayList<>();

        String sql = """
            SELECT id, nom, prenom, email, mot_de_passe_hash, role
            FROM utilisateur
            WHERE role = ?
            ORDER BY nom ASC
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe_hash"),
                        rs.getString("role")
                );
                liste.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    /**
     * Supprime un utilisateur par son ID.
     */
    public boolean supprimerUtilisateur(int id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour un utilisateur (nom, prénom, email, rôle).
     */
    public boolean modifierUtilisateur(Utilisateur u) {
        String sql = """
            UPDATE utilisateur
            SET nom = ?, prenom = ?, email = ?, role = ?
            WHERE id = ?
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getPrenom());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getRole());
            stmt.setInt(5, u.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
