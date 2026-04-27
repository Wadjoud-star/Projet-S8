package com.clubsport.admin.dao;

import com.clubsport.admin.model.Club;
import com.clubsport.admin.util.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClubDAO {

    // Recherche générique : colonne = critère (nom, code_postal, adresse...)
    // valeur : texte saisi 
    public List<Club> rechercherPar(String colonne, String valeur) {
        List<Club> liste = new ArrayList<>();

        // Requête  selon le critère choisi
        String sql = "SELECT id_club, nom, adresse, code_postal, nb_licencies, nb_hommes, nb_femmes "
                   + "FROM club WHERE " + colonne + " LIKE ?";

        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // recherche partielle
            stmt.setString(1, "%" + valeur + "%");

            ResultSet rs = stmt.executeQuery();

            // Construction des  Club à partir des résultats 
            while (rs.next()) {
                liste.add(new Club(
                        rs.getInt("id_club"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("code_postal"),
                        rs.getInt("nb_licencies"),
                        rs.getInt("nb_hommes"),
                        rs.getInt("nb_femmes")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }
}
