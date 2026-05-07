package com.clubsport.servlet;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

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
			out.print("{\"Message\": " + "\"Cet utilisateur n'existe pas\"}");
		} else {
			boolean validate = udao.validateUser(email, password);
			if (validate) {
				HttpSession session = request.getSession(true);
				session.setAttribute("Email", email);
				session.setAttribute("Role", role);
				response.setStatus(HttpServletResponse.SC_OK);
				out.print("{\"message\": \" Login is OK" + "\", \"Email\": \"" + email + "\"" + ", \"Role\": \"" + role
						+ "\"" + "}");
			} else {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				out.print("{\"message\": \" FORBIDDEN!" + "\", \"error\": \"Invalid login or password" + "\"}");
			}
		}
		out.flush();
	}

}
