/**
 * 
 */
package com.clubsport.dao;

import com.clubsport.model.Commune;
import com.clubsport.util.ConnexionDB;
import java.sql.*;
import java.util.*;

/**
 * 
 */
public class CommuneDAO {
	 public List<Commune> rechercheParNom(String recherche) throws SQLException {
	        List<Commune> liste = new ArrayList<>();
	        String sql = "SELECT code_commune, nom_commune FROM commune WHERE nom_commune LIKE ? ORDER BY nom_commune LIMIT 10";

	        try (Connection conn = ConnexionDB.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setString(1, "%" +recherche + "%");

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    liste.add(new Commune(rs.getString("code_commune"), rs.getString("nom_commune")));
	                }
	            }
	        }
	        return liste;
	    }
}

