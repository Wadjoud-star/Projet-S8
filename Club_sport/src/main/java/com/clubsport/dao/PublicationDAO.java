package com.clubsport.dao;

import com.clubsport.model.Publication;
import com.clubsport.util.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la table publication. Récupère les publications avec le nom de
 * l'auteur via jointure
 *
 * @author bpenw
 * @version 1.0
 */
public class PublicationDAO {

	/**
	 * Retourne toutes les publications triées par date décroissante (plus récentes
	 * en premier)
	 */
	public List<Publication> findAll() throws SQLException {
		String sql = "SELECT p.titre, p.contenu, p.image_url, p.date_publication, " + "       u.nom AS nom_auteur "
				+ "FROM publication p " + "INNER JOIN utilisateur u ON p.id_auteur = u.id "
				+ "ORDER BY p.date_publication DESC";

		List<Publication> liste = new ArrayList<>();

		try (Connection conn = ConnexionDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Publication pub = new Publication(rs.getString("titre"), rs.getString("contenu"),
						rs.getString("image_url"), rs.getDate("date_publication"));
				pub.setNomAuteur(rs.getString("nom_auteur"));
				liste.add(pub);
			}
		}
		return liste;
	}
}