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


    public List<Club> rechercherPar(String colonne, String valeur) {// declaration methode pour retourner liste 
        List<Club> clubs = new ArrayList<>();// initialisation de la liste
        String sql;// variable pour la requète sql
// selectionne es clubs filtrés par le nom de la fédération 
        if (colonne.equals("federation")) {
            sql = """
                SELECT c.*
                FROM club c
                JOIN federation f ON c.code_federation = f.code_federation
                WHERE LOWER(f.nom_federation) LIKE LOWER(?)
            """;
        } 
 // sinon pour les autres critères on utilise la classe club 
        else {
            sql = "SELECT * FROM club WHERE LOWER(" + colonne + ") LIKE LOWER(?)";
        }
// ouvre une connection a la bdd
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
// remplace les ? par les valeurs 
            stmt.setString(1, "%" + valeur + "%"); // recherche partielle insensible à la casse
// execute et recupere la requete
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

// construction d'un club 
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
                        null // la région 
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

 //Charge les statistiques d’un club avec code_federation.

    private StatistiqueLicencies chargerStatistiques(Connection conn, int idClub) throws SQLException {

        //récupère le code_federation du club
        String codeFederation = null;
        String sqlFed = "SELECT code_federation FROM club WHERE id_club = ?";
        try (PreparedStatement stmtFed = conn.prepareStatement(sqlFed)) {
            stmtFed.setInt(1, idClub);
            ResultSet rsFed = stmtFed.executeQuery();
            if (rsFed.next()) {
                codeFederation = rsFed.getString("code_federation");
            }
        }

        if (codeFederation == null) {
            return null;
        }

        // récupère les statistiques associées à cette fédération
        String sql = "SELECT * FROM statistique_licencies WHERE code_federation = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codeFederation);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
        
            	
                return new StatistiqueLicencies(
                        rs.getInt("id"),
                        rs.getInt("total_licencies"),
                        rs.getInt("licencies_femmes"),
                        rs.getInt("licencies_hommes"),
                        null,
                        null
                );
            }
        }
        return null;
    }

// charger les clubs de la bdd relie a a colonne espace club
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
