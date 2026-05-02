package com.clubsport.admin.dao;

import com.clubsport.admin.model.Club;
import com.clubsport.admin.model.Commune;
import com.clubsport.admin.model.Federation;
import com.clubsport.admin.model.StatistiqueLicencies;
import com.clubsport.admin.model.EspaceClub;
import com.clubsport.admin.util.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ClubDAO {

    /**
     * Recherche des clubs selon un critère SQL (colonne) et une valeur.
     * Utilisé par la page PageRechercheClubs.
     */
    public List<Club> rechercherPar(String colonne, String valeur) {
        List<Club> clubs = new ArrayList<>();

        String sql = "SELECT * FROM club WHERE " + colonne + " LIKE ?";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + valeur + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Club club = construireClubDepuisResultSet(rs, conn);
                clubs.add(club);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clubs;
    }

    /**
     * Construit un objet Club complet à partir du ResultSet.
     * Charge également les relations : Commune, Fédération, Statistiques, EspaceClub.
     */
    private Club construireClubDepuisResultSet(ResultSet rs, Connection conn) throws SQLException {

        int idClub = rs.getInt("id_club");

        // --- Chargement de la commune ---
        Commune commune = chargerCommune(conn, rs.getString("code_commune"));

        // --- Chargement de la fédération ---
        Federation federation = chargerFederation(conn, rs.getString("code_federation"));

        // --- Chargement des statistiques ---
        StatistiqueLicencies stats = chargerStatistiques(conn, idClub);

        // --- Chargement de l'espace club ---
        EspaceClub espace = chargerEspaceClub(conn, idClub);

        return new Club(
                idClub,
                rs.getString("nom"),
                rs.getString("adresse"),
                rs.getString("code_postal"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getInt("nb_licencies"),
                rs.getInt("nb_hommes"),
                rs.getInt("nb_femmes"),
                commune,
                federation,
                espace
        );
    }

    // ============================================================
    // MÉTHODES DE CHARGEMENT DES RELATIONS
    // ============================================================

    private Commune chargerCommune(Connection conn, String codeCommune) throws SQLException {
        String sql = "SELECT * FROM commune WHERE code_commune = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codeCommune);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Commune(
                        rs.getString("code_commune"),
                        rs.getString("nom_commune"),
                        rs.getInt("population"),
                        null // la région sera chargée ailleurs si besoin
                );
            }
        }
        return null;
    }

    private Federation chargerFederation(Connection conn, String codeFed) throws SQLException {
        String sql = "SELECT * FROM federation WHERE code_federation = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codeFed);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Federation(
                        rs.getString("code_federation"),
                        rs.getString("nom_federation")
                );
            }
        }
        return null;
    }

    private StatistiqueLicencies chargerStatistiques(Connection conn, int idClub) throws SQLException {
        String sql = "SELECT * FROM statistiques_licencies WHERE id_club = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClub);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new StatistiqueLicencies(
                        rs.getInt("id"),
                        rs.getInt("total_licencies"),
                        rs.getInt("licencies_femmes"),
                        rs.getInt("licencies_hommes"),
                        null, // le club sera injecté après
                        null
                );
            }
        }
        return null;
    }

    private EspaceClub chargerEspaceClub(Connection conn, int idClub) throws SQLException {
        String sql = "SELECT * FROM espace_club WHERE id_club = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idClub);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new EspaceClub(
                        rs.getInt("id"),
                        rs.getString("actualites"),
                        rs.getString("horaires"),
                        rs.getDouble("cotisations"),
                        rs.getDate("date_maj"),
                        null // le club sera injecté après
                );
            }
        }
        return null;
    }
}
