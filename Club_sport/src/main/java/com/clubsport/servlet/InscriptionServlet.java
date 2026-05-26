package com.clubsport.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import com.clubsport.dao.UserDAO;
import com.clubsport.model.User;

/**
 * Inscription avec pièce d'identité et statut En_ATTENTE.
 */
@WebServlet("/api/inscription")
@MultipartConfig
public class InscriptionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json; charset=UTF-8");

		String prenom = request.getParameter("prenom");
		String nomFamille = request.getParameter("nom");
		String nom = (prenom != null && !prenom.isBlank())
				? prenom.trim() + " " + (nomFamille != null ? nomFamille.trim() : "")
				: nomFamille;
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirm = request.getParameter("confirm");
		String role = request.getParameter("type");

		Part filePart;
		try {
			filePart = request.getPart("identite");
		} catch (Exception e) {
			filePart = null;
		}

		if (filePart == null || filePart.getSize() == 0) {
			redirectError(request, response, "Pièce d'identité obligatoire");
			return;
		}

		String contentType = filePart.getContentType();
		if (contentType == null
				|| (!contentType.equals("application/pdf") && !contentType.equals("image/png")
						&& !contentType.equals("image/jpeg"))) {
			redirectError(request, response,
					"Format non supporté : PDF, PNG ou JPEG uniquement");
			return;
		}

		if (nomFamille == null || nomFamille.isBlank() || prenom == null || prenom.isBlank()
				|| email == null || password == null || role == null || confirm == null
				|| nom == null || nom.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
			redirectError(request, response, "Paramètres manquants");
			return;
		}

		if (!password.equals(confirm)) {
			redirectError(request, response, "Les mots de passe ne correspondent pas");
			return;
		}

		if (password.length() < 8) {
			redirectError(request, response, "Le mot de passe doit avoir au moins 8 caractères");
			return;
		}

		String passHash = BCrypt.hashpw(password, BCrypt.gensalt());
		String fileName = filePart.getSubmittedFileName();
		String uniqueName = UUID.randomUUID() + "_" + (fileName != null ? fileName : "identite");
		String dbPath = "uploads/" + uniqueName;
		File uploadDir = resolveUploadDir(request);
		if (!uploadDir.exists() && !uploadDir.mkdirs()) {
			redirectError(request, response, "Impossible de créer le dossier d'upload");
			return;
		}
		filePart.write(new File(uploadDir, uniqueName).getAbsolutePath());

		User u = new User(email, nom, passHash, role, dbPath);
		UserDAO udao = new UserDAO();
		if (udao.addUser(u)) {
			String msg = URLEncoder.encode(
					"Compte créé. En attente de validation par l'administrateur",
					StandardCharsets.UTF_8);
			response.sendRedirect(request.getContextPath() + "/successRegister.html?message=" + msg);
		} else {
			redirectError(request, response, "Un compte utilise déjà cette adresse");
		}
	}

	private File resolveUploadDir(HttpServletRequest request) {
		File docker = new File("/data/uploads");
		if (docker.isDirectory() && docker.canWrite()) {
			return docker;
		}
		String real = request.getServletContext().getRealPath("/uploads");
		if (real != null) {
			return new File(real);
		}
		return new File("uploads");
	}

	private void redirectError(HttpServletRequest request, HttpServletResponse response, String message)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		HttpSession session = request.getSession();
		session.setAttribute("erreur", message);
		response.sendRedirect(request.getContextPath() + "/inscription.jsp");
	}
}
