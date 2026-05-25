package com.clubsport.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clubsport.dao.FederationDAO;
import com.clubsport.dao.UserDAO;
import com.clubsport.dao.UtilisateurFederationDAO;
import com.clubsport.model.Federation;
import com.clubsport.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/utilisateur")
public class UtilisateurAccueilServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final UserDAO userDAO = new UserDAO();
	private final FederationDAO federationDAO = new FederationDAO();
	private final UtilisateurFederationDAO federationPrefDAO = new UtilisateurFederationDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");

		HttpSession session = req.getSession(false);
		if (!UtilisateurServletHelper.estUtilisateur(session)) {
			resp.sendRedirect(req.getContextPath() + "/errorLogin.html?message="
					+ java.net.URLEncoder.encode("Accès réservé aux utilisateurs", java.nio.charset.StandardCharsets.UTF_8));
			return;
		}

		String prenom = "utilisateur";
		User user = null;
		List<String> federationsLabels = List.of();

		try {
			Integer userId = resolveUserId(session);
			if (userId != null) {
				user = userDAO.getById(userId);
				if (user != null) {
					if (user.getNom() != null && !user.getNom().isBlank()) {
						session.setAttribute("Nom", user.getNom());
						prenom = EluAccueilServlet.extrairePrenom(user.getNom());
					}
					federationsLabels = libellesFederations(userId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			req.setAttribute("erreur", "Impossible de charger votre profil.");
		}

		if (user == null) {
			String nom = (String) session.getAttribute("Nom");
			if (nom != null && !nom.isBlank()) {
				prenom = EluAccueilServlet.extrairePrenom(nom);
			}
		}

		req.setAttribute("prenom", prenom);
		req.setAttribute("user", user);
		req.setAttribute("federationsLabels", federationsLabels);
		req.getRequestDispatcher("/WEB-INF/jsp/utilisateur/accueil.jsp").forward(req, resp);
	}

	private List<String> libellesFederations(int userId) throws SQLException {
		List<String> codes = federationPrefDAO.listCodesByUtilisateur(userId);
		if (codes.isEmpty()) {
			return List.of();
		}
		List<String> labels = new ArrayList<>();
		for (Federation fed : federationDAO.findAll()) {
			if (codes.contains(fed.getCodeFederation())) {
				labels.add(fed.getNomFederation());
			}
		}
		return labels;
	}

	private Integer resolveUserId(HttpSession session) throws SQLException {
		Integer id = UtilisateurServletHelper.userIdFromSession(session);
		if (id != null && id > 0) {
			return id;
		}
		String email = (String) session.getAttribute("Email");
		if (email == null || email.isBlank()) {
			return null;
		}
		User u = userDAO.getUserbymail(email);
		if (u != null && u.getId() > 0) {
			session.setAttribute("UserId", u.getId());
			return u.getId();
		}
		return null;
	}
}
