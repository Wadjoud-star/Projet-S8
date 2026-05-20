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
            SELECT id, nom, email, mot_de_passe_hash, role, photo_identite, statut_verification
            FROM utilisateur
            WHERE role = ?
            ORDER BY nom ASC
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // on construit un utilisateur complet
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe_hash"),
                        rs.getString("role"),
                        rs.getString("photo_identite"),
                        rs.getString("statut_verification")
                );
                liste.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    // mettre à jour le statut de vérification (VALIDÉ, REFUSÉ, EN_ATTENTE)
    public static boolean updateStatutVerification(int idUtilisateur, String statut) {
        String sql = "UPDATE utilisateur SET statut_verification = ? WHERE id = ?";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, statut);      // nouveau statut
            stmt.setInt(2, idUtilisateur);  // id de l'utilisateur à modifier

            return stmt.executeUpdate() > 0; // true si au moins 1 ligne modifiée

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    // mettre à jour les informations d'un utilisateur à partir des infos rentrées dans les champs
    // (ne modifie pas le mot de passe ici)
    public boolean modifierUtilisateur(Utilisateur u) {
        String sql = """
            UPDATE utilisateur
            SET nom = ?, email = ?, role = ?, photo_identite = ?, statut_verification = ?
            WHERE id = ?
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getRole());
            stmt.setString(4, u.getPhotoIdentite());
            stmt.setString(5, u.getStatutVerification());
            stmt.setInt(6, u.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // récupérer un utilisateur par son id
    public Utilisateur getUtilisateurParId(int id) {
        String sql = """
            SELECT id, nom, email, mot_de_passe_hash, role, photo_identite, statut_verification
            FROM utilisateur
            WHERE id = ?
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // on renvoie un utilisateur complet
                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe_hash"),
                        rs.getString("role"),
                        rs.getString("photo_identite"),
                        rs.getString("statut_verification")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // --- AJOUTER UN UTILISATEUR ---
    // Le mot de passe est déjà hashé dans l'UI avant d'arriver ici
    public boolean ajouterUtilisateur(Utilisateur u) {
        String sql = """
            INSERT INTO utilisateur (nom, email, mot_de_passe_hash, role, photo_identite, statut_verification)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getEmail());

            // si jamais le hash est null, on met une chaîne vide (sécurité)
            stmt.setString(3, u.getMotDePasseHash() != null ? u.getMotDePasseHash() : "");

            stmt.setString(4, u.getRole());
            stmt.setString(5, u.getPhotoIdentite());
            stmt.setString(6, u.getStatutVerification());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
