package com.clubsport.dao;

import java.sql.*;

import org.mindrot.bcrypt.BCrypt;

import com.clubsport.util.*;
import com.clubsport.model.*;

/**
 * UserDAO est la classe qui sert de database connection avec la classe User
 * elle même
 * 
 * @author bpenw
 * @version 1.0
 */
public class UserDAO {
	public boolean addUser(User u) {
		String sql = "INSERT INTO utilisateur (nom, email, mot_de_passe_hash, role) VALUES(?,?,?,?)";
		try (Connection conn = ConnexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, u.getNom());
			stmt.setString(2, u.getEmail());
			stmt.setString(3, u.getPassword());
			stmt.setString(4, u.getRole());

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
				return new User(rs.getString("email"), rs.getString("nom"), rs.getString("mot_de_passe_hash"),
						rs.getString("role"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean validateUser(String email, String password, String role) {
		String sql = "SELECT* FROM utilisateur WHERE email = ? AND role = ?";
		try (Connection conn = ConnexionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			stmt.setString(2, role);
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
	public static void main(String[] args) {
		User u = new User("tata", "tata@gmail.com", "Afikjksndvkjbb##hvk]]", "elu");
		UserDAO udao = new UserDAO();
		boolean validate = udao.addUser(u);
		if(validate) {
			System.out.println("Success!");
		}else {
			System.out.println("Failed!");
		}
	}
}
