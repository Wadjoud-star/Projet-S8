package com.clubsport.admin.dao;

import com.clubsport.admin.model.AuditAction;
import com.clubsport.admin.util.ConnexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditDAO {

    // Récupère toutes les actions d’audit enregistrées dans la base.
     //Jointure avec la table utilisateur pour afficher le nom de l’admin.
    
    public List<AuditAction> getAllActions() {
        List<AuditAction> liste = new ArrayList<>();

        String sql = """
            SELECT a.id, a.id_admin, u.nom AS admin_nom,
                   a.action, a.details, a.date_action
            FROM audit_action a
            LEFT JOIN utilisateur u ON a.id_admin = u.id
            ORDER BY a.date_action DESC
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Construction des objets AuditAction depuis le ResultSet
            while (rs.next()) {
                AuditAction action = new AuditAction(
                        rs.getInt("id"),
                        rs.getInt("id_admin"),
                        rs.getString("admin_nom"),
                        rs.getString("action"),
                        rs.getString("details"),
                        rs.getTimestamp("date_action").toLocalDateTime()
                );
                liste.add(action);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    // Enregistre une nouvelle action d’audit.

    public static boolean enregistrerAction(int idAdmin, String typeAction, String details) {
        String sql = """
            INSERT INTO audit_action (id_admin, action, details, date_action)
            VALUES (?, ?, ?, NOW())
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAdmin);
            stmt.setString(2, typeAction);
            stmt.setString(3, details);

            return stmt.executeUpdate() > 0; // true si au moins 1 ligne insérée

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
