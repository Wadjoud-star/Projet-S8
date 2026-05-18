/**
 * 
 */
package com.clubsport.dao;

import com.clubsport.model.*;
import com.clubsport.util.*;

import java.util.*;
import java.sql.*;

/**
 * La classe StatLicencieDAO est une classe qui permet de recuperer les
 * licencies h et f par espace géographique
 * 
 * @author bpenw
 * @version 1.0
 */
public class StatLicenciesDAO {

	private boolean estRenseigne(String val) {
		return val != null && !val.trim().isEmpty();
	}

	public List<StatLicenceElu> findParRegion(String codeRegion, String codeFederation) throws SQLException {
		String sql = "SELECT sl.code_commune, c.nom_commune, c.code_departement, " + "  c.code_region, r.nom_region, "
				+ "  sl.code_federation, f.nom_federation, " + "  SUM(sl.total_licencies) AS total_licencies, "
				+ "  SUM(sl.licencies_femmes) AS licencies_femmes, " + "  SUM(sl.licencies_hommes) AS licencies_hommes "
				+ "FROM statistique_licencies sl " + "INNER JOIN commune     c ON sl.code_commune    = c.code_commune "
				+ "INNER JOIN region      r ON c.code_region      = r.code_region "
				+ "INNER JOIN federation  f ON sl.code_federation = f.code_federation " + "WHERE c.code_region = ? "
				+ (estRenseigne(codeFederation) ? "AND sl.code_federation = ? " : "")
				+ "GROUP BY sl.code_commune, c.nom_commune, c.code_departement, "
				+ "  c.code_region, r.nom_region, sl.code_federation, f.nom_federation";
		List<StatLicenceElu> liste = new ArrayList<>();

		try (Connection conn = ConnexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, codeRegion);
			if (estRenseigne(codeFederation))
				ps.setString(2, codeFederation);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					StatLicenceElu stat = new StatLicenceElu();
					stat.setCodeCommune(rs.getString("code_commune"));
					stat.setNomCommune(rs.getString("nom_commune"));
					stat.setCodeDepartement(rs.getString("code_departement"));
					stat.setCodeRegion(rs.getString("code_region"));
					stat.setNomRegion(rs.getString("nom_region"));
					stat.setCodeFederation(rs.getString("code_federation"));
					stat.setNomFederation(rs.getString("nom_federation"));
					stat.setTotalLicencies(rs.getInt("total_licencies"));
					stat.setLicenciesFemmes(rs.getInt("licencies_femmes"));
					stat.setLicenciesHommes(rs.getInt("licencies_hommes"));
					liste.add(stat);
				}
			}
		}
		return liste;
	}

	public List<StatLicenceElu> findParCommune(String nomCommune, String codeFederation) throws SQLException {
		String sql = "SELECT sl.code_commune, c.nom_commune, c.code_departement, " + "  c.code_region, r.nom_region, "
				+ "  sl.code_federation, f.nom_federation, " + "  SUM(sl.total_licencies) AS total_licencies, "
				+ "  SUM(sl.licencies_femmes) AS licencies_femmes, " + "  SUM(sl.licencies_hommes) AS licencies_hommes "
				+ "FROM statistique_licencies sl " + "INNER JOIN commune     c ON sl.code_commune    = c.code_commune "
				+ "INNER JOIN region      r ON c.code_region      = r.code_region "
				+ "INNER JOIN federation  f ON sl.code_federation = f.code_federation " + "WHERE c.nom_commune LIKE ? "
				+ (estRenseigne(codeFederation) ? "AND sl.code_federation = ? " : "")
				+ "GROUP BY sl.code_commune, c.nom_commune, c.code_departement, "
				+ "  c.code_region, r.nom_region, sl.code_federation, f.nom_federation";
		List<StatLicenceElu> liste = new ArrayList<>();

		try (Connection conn = ConnexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, nomCommune+"%");
			if (estRenseigne(codeFederation))
				ps.setString(2, codeFederation);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					StatLicenceElu stat = new StatLicenceElu();
					stat.setCodeCommune(rs.getString("code_commune"));
					stat.setNomCommune(rs.getString("nom_commune"));
					stat.setCodeDepartement(rs.getString("code_departement"));
					stat.setCodeRegion(rs.getString("code_region"));
					stat.setNomRegion(rs.getString("nom_region"));
					stat.setCodeFederation(rs.getString("code_federation"));
					stat.setNomFederation(rs.getString("nom_federation"));
					stat.setTotalLicencies(rs.getInt("total_licencies"));
					stat.setLicenciesFemmes(rs.getInt("licencies_femmes"));
					stat.setLicenciesHommes(rs.getInt("licencies_hommes"));
					liste.add(stat);
				}
			}
		}
		return liste;
	}
}
