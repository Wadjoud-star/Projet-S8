package com.clubsport.dao;

import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

import com.clubsport.util.*;
import com.clubsport.model.*;

/**
 * UserDAO est la classe qui sert de database connection entre la classe User
 * et la table utilisateur
 * 
 * @author bpenw
 * @version 1.0
 */
public class UserDAO {
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

	public User getUserbymail(String email) throws SQLException {
		String sql = "SELECT * FROM utilisateur WHERE email = ?";
		try (Connection conn = ConnexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return new User(rs.getString("email"), rs.getString("nom"), rs.getString("mot_de_passe_hash"),
							rs.getString("role"), rs.getString("photo_identite"), rs.getString("statut_verification"));
				}
			}
		}
		return null;
	}

	/** Vérifie le mot de passe pour un utilisateur déjà chargé (rôle lu en base). */
	public boolean verifyPassword(User user, String plainPassword) {
		if (user == null || plainPassword == null) {
			return false;
		}
		String hash = user.getPassword();
		if (hash == null || hash.isBlank()) {
			return false;
		}
		try {
			return BCrypt.checkpw(plainPassword, hash);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public static void main(String[] args) {
		User u = new User("tata", "tata@gmail.com", "Afikjksndvkjbb##hvk]]", "elu", "uploads/demo.pdf");
		UserDAO udao = new UserDAO();
		boolean validate = udao.addUser(u);
		if (validate) {
			System.out.println("Success!");
		} else {
			System.out.println("Failed!");
		}
	}
}
