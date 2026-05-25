package com.clubsport.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import com.clubsport.dao.UserDAO;
import com.clubsport.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Connexion : le rôle est lu en base (choisi à l'inscription), pas sur le formulaire.
 */
@WebServlet("/api/login")
public class AuthentificationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = safe(request.getParameter("email"));
		String password = request.getParameter("password");

		if (email.isEmpty() || password == null || password.isEmpty()) {
			redirectError(request, response, "Email et mot de passe obligatoires");
			return;
		}

		UserDAO udao = new UserDAO();
		User u;
		try {
			u = udao.getUserbymail(email);
		} catch (SQLException e) {
			e.printStackTrace();
			redirectError(request, response,
					"Impossible de joindre la base (MySQL démarré ? Connexion 127.0.0.1:3306)");
			return;
		}

		if (u == null) {
			redirectError(request, response, "Cet utilisateur n'existe pas");
			return;
		}

		if (!udao.verifyPassword(u, password)) {
			redirectError(request, response, "Login ou mot de passe incorrect");
			return;
		}

		String statut = u.getStatut() == null ? "" : u.getStatut().trim();
		if (!statut.isEmpty() && !isCompteValide(statut)) {
			redirectError(request, response,
					"Inscription en attente de validation par l'administrateur");
			return;
		}

		String role = u.getRole() == null ? "" : u.getRole().trim();
		HttpSession session = request.getSession(true);
		session.setAttribute("Email", u.getEmail());
		session.setAttribute("Role", role);
		session.setAttribute("Nom", u.getNom());
		if (u.getId() > 0) {
			session.setAttribute("UserId", u.getId());
		}

		String ctx = request.getContextPath();
		if ("elu".equalsIgnoreCase(role)) {
			response.sendRedirect(ctx + "/elu");
		} else if ("acteur".equalsIgnoreCase(role)) {
			response.sendRedirect(ctx + "/acteur");
		} else if ("utilisateur".equalsIgnoreCase(role)) {
			response.sendRedirect(ctx + "/utilisateur");
		} else {
			redirectError(request, response, "Rôle du compte non reconnu");
		}
	}

	private void redirectError(HttpServletRequest request, HttpServletResponse response, String message)
			throws IOException {
		String msg = URLEncoder.encode(message, StandardCharsets.UTF_8);
		response.sendRedirect(request.getContextPath() + "/errorLogin.html?message=" + msg);
	}

	private String safe(String value) {
		return value == null ? "" : value.trim();
	}

	/** Aligné avec l'app admin (VERIFIE) et les comptes de test (VALIDE). */
	private boolean isCompteValide(String statut) {
		return "VALIDE".equalsIgnoreCase(statut) || "VERIFIE".equalsIgnoreCase(statut);
	}
}
