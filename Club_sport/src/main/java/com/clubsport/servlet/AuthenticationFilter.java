package com.clubsport.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

@WebFilter(urlPatterns = {
        "/elu", "/elu/licences", "/elu/licences/communes-search", "/elu/licences/export",
        "/elu/cartographie", "/elu/cartographie/data",
        "/elu/visualisation", "/elu/export-pdf",
        "/acteur",
        "/utilisateur", "/utilisateur/profil"})

public class AuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		HttpSession session = request.getSession(false);
		
		if (session != null && session.getAttribute("Email") != null && session.getAttribute("Role") != null) {
			arg2.doFilter(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/json");
			//PrintWriter out = response.getWriter();
			//out.print("{\"error\" : \"Authentification required\"}");
			String msg = URLEncoder.encode("Veuillez vous identifier pour accéder à cette page",
					StandardCharsets.UTF_8);
			response.sendRedirect(request.getContextPath() + "/errorLogin.html?message=" + msg);
			return;
		}
	}

}
