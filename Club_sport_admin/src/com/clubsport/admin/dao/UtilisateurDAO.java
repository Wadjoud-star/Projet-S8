package com.clubsport.admin.dao;

import com.clubsport.admin.model.Utilisateur;
import com.clubsport.admin.util.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    // permet de récuperer les utilisateurs en faisant une liste. On la trie par ordre alpha
    public List<Utilisateur> getUtilisateursParRole(String role) {
        List<Utilisateur> liste = new ArrayList<>();

        String sql = """
            SELECT id, nom, email, mot_de_passe_hash, role
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

    // supprimer un utilisateur
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

    // mettre a jour les informations d'un utilisateur a partir des infos rentrées dans les champs 
    public boolean modifierUtilisateur(Utilisateur u) {
        String sql = """
            UPDATE utilisateur
            SET nom = ?, email = ?, role = ?
            WHERE id = ?
        """;

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

    // récupérer l'id d'un utilisateur 
    public Utilisateur getUtilisateurParId(int id) {
        String sql = """
            SELECT id, nom, email, mot_de_passe_hash, role
            FROM utilisateur
            WHERE id = ?
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe_hash"),
                        rs.getString("role")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // --- AJOUTER UN UTILISATEUR ---
    public boolean ajouterUtilisateur(Utilisateur u) {
    	String sql = """
    		    INSERT INTO utilisateur (nom, email, mot_de_passe_hash, role)
    		    VALUES (?, ?, ?, ?)
    		""";

    		try (Connection conn = ConnexionDB.getConnection();
    		     PreparedStatement stmt = conn.prepareStatement(sql)) {

    		    stmt.setString(1, u.getNom());
    		    stmt.setString(2, u.getEmail());
    		    stmt.setString(3, ""); // mot de passe vide
    		    stmt.setString(4, u.getRole());

    		    return stmt.executeUpdate() > 0;
    		}
 catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
