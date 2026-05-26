package com.clubsport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clubsport.util.ConnexionDB;

/**
 * Préférences sportives : fédérations favorites d'un utilisateur lambda.
 */
public class UtilisateurFederationDAO {

	public List<String> listCodesByUtilisateur(int idUtilisateur) throws SQLException {
		String sql = "SELECT code_federation FROM utilisateur_federation WHERE id_utilisateur = ? ORDER BY code_federation";
		List<String> codes = new ArrayList<>();
		try (Connection conn = ConnexionDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, idUtilisateur);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					codes.add(rs.getString("code_federation"));
				}
			}
		}
		return codes;
	}

	public void remplacerFederations(int idUtilisateur, List<String> codes) throws SQLException {
		try (Connection conn = ConnexionDB.getConnection()) {
			conn.setAutoCommit(false);
			try {
				try (PreparedStatement del = conn.prepareStatement(
						"DELETE FROM utilisateur_federation WHERE id_utilisateur = ?")) {
					del.setInt(1, idUtilisateur);
					del.executeUpdate();
				}
				if (codes != null && !codes.isEmpty()) {
					String ins = "INSERT INTO utilisateur_federation (id_utilisateur, code_federation) VALUES (?, ?)";
					try (PreparedStatement ps = conn.prepareStatement(ins)) {
						for (String code : codes) {
							if (code == null || code.isBlank()) {
								continue;
							}
							ps.setInt(1, idUtilisateur);
							ps.setString(2, code.trim());
							ps.addBatch();
						}
						ps.executeBatch();
					}
				}
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				conn.setAutoCommit(true);
			}
		}
	}
}
