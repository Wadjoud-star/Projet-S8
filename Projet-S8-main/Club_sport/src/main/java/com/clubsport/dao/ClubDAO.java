package com.clubsport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.clubsport.model.Club;
import com.clubsport.util.ConnexionDB;

public class ClubDAO {

    public Club findById(int idClub) {
        Club club = null;

        String sql = """
            SELECT c.*, e.actualites, e.horaires, e.cotisations
            FROM club c
            LEFT JOIN espace_club e ON c.id_club = e.id_club
            WHERE c.id_club = ?
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idClub);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                club = new Club();

                club.setIdClub(rs.getInt("id_club"));
                club.setNom(rs.getString("nom"));
                club.setAdresse(rs.getString("adresse"));
                club.setCodePostal(rs.getString("code_postal"));
                club.setLatitude(rs.getDouble("latitude"));
                club.setLongitude(rs.getDouble("longitude"));
                club.setNbLicencies(rs.getInt("nb_licencies"));
                club.setNbFemmes(rs.getInt("nb_femmes"));
                club.setNbHommes(rs.getInt("nb_hommes"));
                club.setCodeFederation(rs.getString("code_federation"));
                club.setCodeCommune(rs.getString("code_commune"));

                club.setActualite(rs.getString("actualites"));
                club.setHoraires(rs.getString("horaires"));
                club.setCotisation(rs.getString("cotisations"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return club;
    }

    public boolean userHasClub(int userId) {
        String sql = "SELECT COUNT(*) FROM espace_club WHERE id = ?";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int createClub(Club club, int userId) {
        String sql = """
            INSERT INTO club (
                nom, adresse, code_postal, latitude, longitude,
                nb_licencies, nb_femmes, nb_hommes,
                code_federation, code_commune
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, club.getNom());
            ps.setString(2, club.getAdresse());
            ps.setString(3, club.getCodePostal());
            ps.setDouble(4, club.getLatitude());
            ps.setDouble(5, club.getLongitude());
            ps.setInt(6, club.getNbLicencies());
            ps.setInt(7, club.getNbFemmes());
            ps.setInt(8, club.getNbHommes());
            ps.setString(9, club.getCodeFederation());
            ps.setString(10, club.getCodeCommune());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next()) {
                int idClub = keys.getInt(1);
                createEspaceClub(idClub, userId);
                return idClub;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

  private void createEspaceClub(int idClub, int userId) {
    String sql = """
        INSERT INTO espace_club (
            id, actualites, horaires, cotisations, id_club
        )
        VALUES (?, ?, ?, ?, ?)
    """;

    try (Connection conn = ConnexionDB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, userId);
        ps.setString(2, "");
        ps.setString(3, "");
        ps.setBigDecimal(4, java.math.BigDecimal.ZERO);
        ps.setInt(5, idClub);

        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public Club findByUserId(int userId) {
    Club club = null;

    String sql = """
        SELECT c.*, e.actualites, e.horaires, e.cotisations
        FROM club c
        JOIN espace_club e ON c.id_club = e.id_club
        WHERE e.id = ?
    """;

    try (Connection conn = ConnexionDB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            club = new Club();

            club.setIdClub(rs.getInt("id_club"));
            club.setNom(rs.getString("nom"));
            club.setAdresse(rs.getString("adresse"));
            club.setCodePostal(rs.getString("code_postal"));
            club.setLatitude(rs.getDouble("latitude"));
            club.setLongitude(rs.getDouble("longitude"));
            club.setNbLicencies(rs.getInt("nb_licencies"));
            club.setNbFemmes(rs.getInt("nb_femmes"));
            club.setNbHommes(rs.getInt("nb_hommes"));
            club.setCodeFederation(rs.getString("code_federation"));
            club.setCodeCommune(rs.getString("code_commune"));
            club.setActualite(rs.getString("actualites"));
            club.setHoraires(rs.getString("horaires"));
            club.setCotisation(rs.getString("cotisations"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return club;
}
    public void update(Club club) {
        String sql = """
            UPDATE club
            SET nom=?,
                adresse=?,
                code_postal=?,
                nb_licencies=?,
                nb_femmes=?,
                nb_hommes=?
            WHERE id_club=?
        """;

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, club.getNom());
            ps.setString(2, club.getAdresse());
            ps.setString(3, club.getCodePostal());
            ps.setInt(4, club.getNbLicencies());
            ps.setInt(5, club.getNbFemmes());
            ps.setInt(6, club.getNbHommes());
            ps.setInt(7, club.getIdClub());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateActualite(int idClub, String actualite) {
        String sql = "UPDATE espace_club SET actualites=? WHERE id_club=?";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, actualite);
            ps.setInt(2, idClub);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateHoraires(int idClub, String horaires) {
        String sql = "UPDATE espace_club SET horaires=? WHERE id_club=?";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, horaires);
            ps.setInt(2, idClub);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCotisation(int idClub, String cotisation) {
    String sql = "UPDATE espace_club SET cotisations=?, date_maj=NOW() WHERE id_club=?";

    try (Connection conn = ConnexionDB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setBigDecimal(1, new java.math.BigDecimal(cotisation.trim()));
        ps.setInt(2, idClub);

        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
