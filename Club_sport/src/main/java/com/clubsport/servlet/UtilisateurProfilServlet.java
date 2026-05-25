package com.clubsport.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.clubsport.dao.FederationDAO;
import com.clubsport.dao.UserDAO;
import com.clubsport.dao.UtilisateurFederationDAO;
import com.clubsport.model.Federation;
import com.clubsport.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/utilisateur/profil")
@MultipartConfig
public class UtilisateurProfilServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final UserDAO userDAO = new UserDAO();
	private final FederationDAO federationDAO = new FederationDAO();
	private final UtilisateurFederationDAO federationPrefDAO = new UtilisateurFederationDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!chargerPage(req, resp, null)) {
			return;
		}
		req.getRequestDispatcher("/WEB-INF/jsp/utilisateur/profil.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (!UtilisateurServletHelper.estUtilisateur(session)) {
			resp.sendRedirect(req.getContextPath() + "/errorLogin.html");
			return;
		}

		Integer userId = resolveUserId(session);
		if (userId == null) {
			req.setAttribute("erreur", "Compte introuvable.");
			chargerPage(req, resp, null);
			req.getRequestDispatcher("/WEB-INF/jsp/utilisateur/profil.jsp").forward(req, resp);
			return;
		}

		String telephone = safe(req.getParameter("telephone"));
		String bio = safe(req.getParameter("bio"));
		String[] fedCodes = req.getParameterValues("federations");
		List<String> codes = fedCodes == null ? List.of() : Arrays.asList(fedCodes);

		String newPhotoPath = null;
		try {
			Part photoPart = req.getPart("photoProfil");
			if (photoPart != null && photoPart.getSize() > 0) {
				String contentType = photoPart.getContentType();
				if (contentType == null
						|| (!contentType.equals("image/png") && !contentType.equals("image/jpeg")
								&& !contentType.equals("image/webp"))) {
					req.setAttribute("erreur", "Photo : PNG, JPEG ou WebP uniquement.");
					chargerPage(req, resp, userId);
					req.getRequestDispatcher("/WEB-INF/jsp/utilisateur/profil.jsp").forward(req, resp);
					return;
				}
				String fileName = photoPart.getSubmittedFileName();
				String uniqueName = System.currentTimeMillis() + "_"
						+ (fileName != null ? fileName : "profil");
				File dir = resolveProfilUploadDir(req);
				if (!dir.exists() && !dir.mkdirs()) {
					req.setAttribute("erreur", "Impossible d'enregistrer la photo.");
					chargerPage(req, resp, userId);
					req.getRequestDispatcher("/WEB-INF/jsp/utilisateur/profil.jsp").forward(req, resp);
					return;
				}
				photoPart.write(new File(dir, uniqueName).getAbsolutePath());
				newPhotoPath = "uploads/profils/" + uniqueName;
			}

			userDAO.updateProfil(userId, telephone, bio, newPhotoPath);
			federationPrefDAO.remplacerFederations(userId, codes);

			String msg = URLEncoder.encode("Profil enregistré.", StandardCharsets.UTF_8);
			resp.sendRedirect(req.getContextPath() + "/utilisateur/profil?ok=" + msg);
		} catch (SQLException e) {
			e.printStackTrace();
			req.setAttribute("erreur", "Erreur base de données (migration utilisateur exécutée ?).");
			chargerPage(req, resp, userId);
			req.getRequestDispatcher("/WEB-INF/jsp/utilisateur/profil.jsp").forward(req, resp);
		}
	}

	private boolean chargerPage(HttpServletRequest req, HttpServletResponse resp, Integer userIdHint)
			throws IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");

		HttpSession session = req.getSession(false);
		if (!UtilisateurServletHelper.estUtilisateur(session)) {
			resp.sendRedirect(req.getContextPath() + "/errorLogin.html");
			return false;
		}

		Integer userId = userIdHint != null ? userIdHint : resolveUserId(session);
		if (userId == null) {
			req.setAttribute("erreur", "Compte introuvable.");
			return true;
		}

		try {
			User user = userDAO.getById(userId);
			List<Federation> federations = federationDAO.findAll();
			List<String> selected = federationPrefDAO.listCodesByUtilisateur(userId);

			req.setAttribute("user", user);
			req.setAttribute("federations", federations);
			req.setAttribute("federationsSelection", selected);

			String ok = req.getParameter("ok");
			if (ok != null && !ok.isBlank()) {
				req.setAttribute("succes", ok);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			req.setAttribute("erreur", "Impossible de charger le profil.");
		}
		return true;
	}

	private Integer resolveUserId(HttpSession session) {
		Integer id = UtilisateurServletHelper.userIdFromSession(session);
		if (id != null && id > 0) {
			return id;
		}
		String email = (String) session.getAttribute("Email");
		if (email == null || email.isBlank()) {
			return null;
		}
		try {
			User u = userDAO.getUserbymail(email);
			if (u != null && u.getId() > 0) {
				session.setAttribute("UserId", u.getId());
				return u.getId();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private File resolveProfilUploadDir(HttpServletRequest request) {
		File docker = new File("/data/uploads/profils");
		if (docker.isDirectory() && docker.canWrite()) {
			return docker;
		}
		File dockerRoot = new File("/data/uploads");
		if (dockerRoot.isDirectory() && dockerRoot.canWrite()) {
			return new File(dockerRoot, "profils");
		}
		String real = request.getServletContext().getRealPath("/uploads/profils");
		if (real != null) {
			return new File(real);
		}
		return new File("uploads/profils");
	}

	private String safe(String value) {
		return value == null ? "" : value.trim();
	}
}
