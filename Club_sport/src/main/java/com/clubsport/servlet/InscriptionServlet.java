package com.clubsport.servlet;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.clubsport.dao.*;
import com.clubsport.model.*;
import com.clubsport.util.*;

import org.mindrot.bcrypt.*;

/**
 * Servlet implementation class InscriptionServlet
 */
@WebServlet("/api/inscription")
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
		if (nom == null || email == null || password == null || role == null || confirm == null || nom.isEmpty()
				|| email.isEmpty() || password.isEmpty() || role.isEmpty() || confirm.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			//out.print("{\"Message\": " + "\"Parametres manquants\"}");
			String msg = URLEncoder.encode("Paramètres manquants", StandardCharsets.UTF_8);

			response.sendRedirect(request.getContextPath() + "/errorRegister.html?message=" + msg);
			return;
		}
		if (!password.equals(confirm)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			//out.print("{\"Message\": " + "\"Les deux mots de passes ne correspondent pas\"}");
			String msg = URLEncoder.encode("Les mots de passe ne correspondent pas", StandardCharsets.UTF_8);

			response.sendRedirect(request.getContextPath() + "/errorRegister.html?message=" + msg);
			return;
		}
		UserDAO udao = new UserDAO();
		User u = new User(email, nom, pass_hash, role);
		boolean validate = udao.addUser(u);
		if (validate) {
			response.setStatus(HttpServletResponse.SC_CREATED);
			String msg = URLEncoder.encode("Compte créé avec succès", StandardCharsets.UTF_8);

			response.sendRedirect(request.getContextPath() + "/successRegister.html?message=" + msg);
			return;
		} else {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			//out.print("{\"Message\": " + "\"Email existant\"}");
			String msg = URLEncoder.encode("Ce compte existe déjà", StandardCharsets.UTF_8);

			response.sendRedirect(request.getContextPath() + "/errorRegister.html?message=" + msg);
			return;
		}
	}

}
