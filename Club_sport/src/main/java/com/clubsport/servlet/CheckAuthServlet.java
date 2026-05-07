package com.clubsport.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import com.clubsport.model.*;
import com.clubsport.dao.*;

/**
 * Servlet implementation class CheckAuthServlet
 */
@WebServlet("/api/checkauth")
public class CheckAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckAuthServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("Email");
        String role = (String) session.getAttribute("Role");
        if(session != null && email != null && role != null) {
        	response.setStatus(HttpServletResponse.SC_OK);
        	out.print("{\"Message\": "+"\"Authentication is ok\", "+"\"Email\": \""+ email +"\"}");
        }else {
        	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			out.print("{\"error\" : \"Authentication required\"}");
			return;
        }
	}

}
