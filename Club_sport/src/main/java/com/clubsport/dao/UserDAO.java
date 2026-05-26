package com.clubsport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.clubsport.model.User;
import com.clubsport.util.ConnexionDB;

/**
 * Accès à la table utilisateur.
 */
public class UserDAO {

	private static final String SELECT_USER = "SELECT id, nom, email, mot_de_passe_hash, role, photo_identite, "
			+ "statut_verification, photo_profil, telephone, bio FROM utilisateur WHERE ";

	public boolean addUser(User u) {
		String sql = "INSERT INTO utilisateur (nom, email, mot_de_passe_hash, role, photo_identite, statut_verification) VALUES(?,?,?,?,?,?)";
		try (Connection conn = ConnexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, u.getNom());
			stmt.setString(2, u.getEmail());
			stmt.setString(3, u.getPassword());
			stmt.setString(4, u.getRole());
			stmt.setString(5, u.getIdentitePath());
			stmt.setString(6, u.getStatut());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public User getUserbymail(String email) {
		String sql = "SELECT* FROM utilisateur WHERE email = ?";
		try (Connection conn = ConnexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				User u = new User(rs.getString("email"), rs.getString("nom"), rs.getString("mot_de_passe_hash"),
						rs.getString("role"), rs.getString("photo_identite"), rs.getString("statut_verification"));
				u.setId(rs.getInt("id"));
				return u;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean validateUser(String email, String password) {
		String sql = "SELECT* FROM utilisateur WHERE email = ?";
		try (Connection conn = ConnexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				String hash = rs.getString("mot_de_passe_hash");
	            return BCrypt.checkpw(password, hash);
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	public User getById(int id) throws SQLException {
		try (Connection conn = ConnexionDB.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SELECT_USER + "id = ?")) {
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapUser(rs);
				}
			}
		}
		return null;
	}

	public boolean updateProfil(int id, String telephone, String bio, String photoProfil) throws SQLException {
		String sql = "UPDATE utilisateur SET telephone = ?, bio = ?, photo_profil = COALESCE(?, photo_profil) WHERE id = ?";
		try (Connection conn = ConnexionDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, emptyToNull(telephone));
			ps.setString(2, emptyToNull(bio));
			ps.setString(3, photoProfil);
			ps.setInt(4, id);
			return ps.executeUpdate() > 0;
		}
	}

	/*public boolean verifyPassword(User user, String plainPassword) {
		if (user == null || plainPassword == null) {
			 return false;
	}
	}*/

	public static void main(String[] args) {
		User u = new User("tata", "tata@gmail.com", "Afikjksndvkjbb##hvk]]", "elu", "u");
		UserDAO udao = new UserDAO();
		boolean validate = udao.addUser(u);
		if(validate) {
			System.out.println("Success!");
		}else {
			System.out.println("Failed!");
		}
	}
	private User mapUser(ResultSet rs) throws SQLException {
		User u = new User(
				rs.getString("email"),
				rs.getString("nom"),
				rs.getString("mot_de_passe_hash"),
				rs.getString("role"),
				rs.getString("photo_identite"),
				rs.getString("statut_verification"));
		u.setId(rs.getInt("id"));
		u.setPhotoProfil(rs.getString("photo_profil"));
		u.setTelephone(rs.getString("telephone"));
		u.setBio(rs.getString("bio"));
		return u;
	}

	private String emptyToNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value.trim();
	}
}
