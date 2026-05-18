package com.clubsport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.clubsport.model.Club;
import com.clubsport.util.ConnexionDB;

public class ClubDAO {

    public Club findById(int idClub) {
        Club club = null;

        String sql = "SELECT * FROM club WHERE id_club = ?";

        Connection conn = null;

        try {
            conn = ConnexionDB.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
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

                club.setActualite(rs.getString("actualite"));
                club.setHoraires(rs.getString("horaires"));
                club.setCotisation(rs.getString("cotisation"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnexionDB.fermer(conn);
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

        Connection conn = null;

        try {
            conn = ConnexionDB.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

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
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    public void updateActualite(int idClub, String actualite) {
        String sql = "UPDATE club SET actualite=? WHERE id_club=?";

        Connection conn = null;

        try {
            conn = ConnexionDB.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, actualite);
            ps.setInt(2, idClub);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    public void updateHoraires(int idClub, String horaires) {
        String sql = "UPDATE club SET horaires=? WHERE id_club=?";

        Connection conn = null;

        try {
            conn = ConnexionDB.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, horaires);
            ps.setInt(2, idClub);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnexionDB.fermer(conn);
        }
    }

    public void updateCotisation(int idClub, String cotisation) {
        String sql = "UPDATE club SET cotisation=? WHERE id_club=?";

        Connection conn = null;

        try {
            conn = ConnexionDB.getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cotisation);
            ps.setInt(2, idClub);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnexionDB.fermer(conn);
        }
    }
}