package com.clubsport.servlet;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.clubsport.dao.*;
import com.clubsport.util.*;
import com.clubsport.model.*;

/**
 * Servlet implementation class AuthentificationServlet
 */
@WebServlet("/api/login")
public class AuthentificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AuthentificationServlet() {
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
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String role = request.getParameter("type");
		UserDAO udao = new UserDAO();
		User u = udao.getUserbymail(email);
		if (u == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			//out.print("{\"Message\": " + "\"Cet utilisateur n'existe pas\"}");
			String msg = URLEncoder.encode("Cet utilisateur n'existe pas", StandardCharsets.UTF_8);

			response.sendRedirect(request.getContextPath() + "/errorLogin.html?message=" + msg);
			return;
		} else {
			boolean validate = udao.validateUser(email, password, role);
			if (validate) {
				HttpSession session = request.getSession(true);
				session.setAttribute("Email", email);
				session.setAttribute("Role", role);
				response.setStatus(HttpServletResponse.SC_OK);
				out.print("{\"email\":\"" + email +"\", "+"\"role\":\"" + role + "\"}");
				if(u.getRole().equals("elu"))
					response.sendRedirect("/elu");
				else
					response.sendRedirect("/acteur");
			} else {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				//out.print("{\"message\": \" FORBIDDEN!" + "\", \"error\": \"Invalid login or password" + "\"}");
				String msg = URLEncoder.encode("Login ou mot de passe incorrect", StandardCharsets.UTF_8);

				response.sendRedirect(request.getContextPath() + "/errorLogin.html?message=" + msg);
				return;
			}
		}
		out.flush();
	}

}
