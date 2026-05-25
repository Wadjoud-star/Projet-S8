/**
 * 
 */
package com.clubsport.dao;

import com.clubsport.model.*;
import com.clubsport.util.*;

import java.sql.*;
import java.util.*;

/**
 * Récupère le nombre de clubs selon une zone géographique et une fédération
 * 
 * @author bpenw
 * @version 1.0
 */
public class StatClubsDAO {

	private boolean estRenseigne(String val) {
		return val != null && !val.trim().isEmpty();
	}

	public List<StatClubs> findParRegion(String codeRegion, String codeFederation) throws SQLException {
		String sql = "SELECT sc.code_commune, sc.code_federation, " + "  SUM(sc.nb_clubs) AS nb_clubs, "
				+ "  SUM(sc.nb_etablissements_professionnels) AS nb_etablissements_professionnels, "
				+ "  SUM(sc.total_structures) AS total_structures " + "FROM statistique_clubs sc "
				+ "INNER JOIN commune c ON sc.code_commune = c.code_commune " + "WHERE c.code_region = ? "
				+ (estRenseigne(codeFederation) ? "AND sc.code_federation = ? " : "")
				+ "GROUP BY sc.code_commune, sc.code_federation"
				+" ORDER BY nb_clubs DESC";
		List<StatClubs> liste = new ArrayList<>();

		try (Connection conn = ConnexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, codeRegion);
			if (estRenseigne(codeFederation))
				ps.setString(2, codeFederation);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					liste.add(new StatClubs(rs.getInt("nb_clubs"), rs.getInt("nb_etablissements_professionnels"),
							rs.getInt("total_structures"), rs.getString("code_commune"),
							rs.getString("code_federation")));
				}
			}
		}
		return liste;
	}

	public List<StatClubs> findParCommune(String nomCommune, String codeFederation) throws SQLException {
		String sql = "SELECT sc.code_commune, sc.code_federation, " + "  SUM(sc.nb_clubs) AS nb_clubs, "
				+ "  SUM(sc.nb_etablissements_professionnels) AS nb_etablissements_professionnels, "
				+ "  SUM(sc.total_structures) AS total_structures " + "FROM statistique_clubs sc "
				+ "INNER JOIN commune c ON sc.code_commune = c.code_commune " + "WHERE c.nom_commune LIKE ? "
				+ (estRenseigne(codeFederation) ? "AND sc.code_federation = ? " : "")
				+ "GROUP BY sc.code_commune, sc.code_federation"
				+" ORDER BY nb_clubs DESC";
		List<StatClubs> liste = new ArrayList<>();

		try (Connection conn = ConnexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, nomCommune + "%");// le % ici me sert à afficher toutes les communes commencant par le nomm
												// entré
			if (estRenseigne(codeFederation))
				ps.setString(2, codeFederation);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					liste.add(new StatClubs(rs.getInt("nb_clubs"), rs.getInt("nb_etablissements_professionnels"),
							rs.getInt("total_structures"), rs.getString("code_commune"),
							rs.getString("code_federation")));
				}
			}
		}
		return liste;
	}
}
