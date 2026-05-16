package com.clubsport.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.mindrot.bcrypt.BCrypt;

import com.clubsport.dao.*;
import com.clubsport.model.*;
import com.clubsport.util.*;



/**
 * Servlet implementation class InscriptionServlet
 */
@WebServlet("/api/inscription")
@MultipartConfig
public class InscriptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InscriptionServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		String nom = request.getParameter("nom");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirm = request.getParameter("confirm");
		String pass_hash = BCrypt.hashpw(password, BCrypt.gensalt());
		String role = request.getParameter("type");
		Part filePart = request.getPart("identite");
		String contentType = filePart.getContentType();
		if (!contentType.equals("application/pdf") && !contentType.equals("image/png")
				&& !contentType.equals("image/jpeg")) {

			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String msg = URLEncoder.encode(
					"Format de la pièce jointe non supporté. Les fichiers supportés sont au format pdf, png ou jpg",
					StandardCharsets.UTF_8);

			response.sendRedirect(request.getContextPath() + "/errorRegister.html?message=" + msg);
			return;
		}
		if (nom == null || email == null || password == null || role == null || confirm == null || nom.isEmpty()
				|| email.isEmpty() || password.isEmpty() || role.isEmpty() || confirm.isEmpty() || filePart == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			// out.print("{\"Message\": " + "\"Parametres manquants\"}");
			String msg = URLEncoder.encode("Paramètres manquants", StandardCharsets.UTF_8);

			response.sendRedirect(request.getContextPath() + "/errorRegister.html?message=" + msg);
			return;
		}
		if (!password.equals(confirm)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			// out.print("{\"Message\": " + "\"Les deux mots de passes ne correspondent
			// pas\"}");
			String msg = URLEncoder.encode("Les mots de passe ne correspondent pas", StandardCharsets.UTF_8);

			response.sendRedirect(request.getContextPath() + "/errorRegister.html?message=" + msg);
			return;
		}
		if (password.length() < 8) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String msg = URLEncoder.encode("Le mot de passe doit avoir au moins 8 caractères", StandardCharsets.UTF_8);

			response.sendRedirect(request.getContextPath() + "/errorRegister.html?message=" + msg);
			return;
		}
		String fileName = filePart.getSubmittedFileName();
		String uniqueName = System.currentTimeMillis() + "_" + fileName;
		String uploadPath = "/data/uploads";
/*		File uploadDir = new File(uploadPath);

		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}*/
		String path = "uploads/" + uniqueName;
		UserDAO udao = new UserDAO();
		User u = new User(email, nom, pass_hash, role, path);
		boolean validate = udao.addUser(u);
		if (validate) {
			response.setStatus(HttpServletResponse.SC_CREATED);
			String msg = URLEncoder.encode("Compte créé avec succès. En attente de validation par l'administrateur",
					StandardCharsets.UTF_8);
			filePart.write(uploadPath + File.separator + uniqueName);
			response.sendRedirect(request.getContextPath() + "/successRegister.html?message=" + msg);
			return;
		} else {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			// out.print("{\"Message\": " + "\"Email existant\"}");
			String msg = URLEncoder.encode("Un compte utilise déjà cette adresse", StandardCharsets.UTF_8);

			response.sendRedirect(request.getContextPath() + "/errorRegister.html?message=" + msg);
			return;
		}
	}

}
