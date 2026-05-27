package com.clubsport.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.clubsport.dao.*;
import com.clubsport.model.*;
import com.clubsport.util.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class StatsServlet
 */
@WebServlet("/api/stats")
public class StatsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StatsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String param(HttpServletRequest request, String name) {
		String v = request.getParameter(name);
		return (v == null) ? "" : v.trim();
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
		StatClubsDAO clubsDAO = new StatClubsDAO();
		StatLicenciesDAO licencesDAO = new StatLicenciesDAO();
		String codeRegion = param(request, "code_region");
		String nomCommune = param(request, "nom_commune");
		String codeFederation = param(request, "code_federation");
		String codesCommunes = param(request, "codes_communes");

		try {
			Map<String, Object> resultat = new HashMap<>();

			if (!codesCommunes.isEmpty()) {
				String[] codes = codesCommunes.split(",");
				resultat.put("clubs", clubsDAO.findParCodesCommunes(codes, codeFederation));
				resultat.put("licences", licencesDAO.findParCodesCommunes(codes, codeFederation));
			} else if (!codeRegion.isEmpty()) {
				resultat.put("clubs", clubsDAO.findParRegion(codeRegion, codeFederation));
				resultat.put("licences", licencesDAO.findParRegion(codeRegion, codeFederation));
			} else if (!nomCommune.isEmpty()) {
				resultat.put("clubs", clubsDAO.findParCommune(nomCommune, codeFederation));
				resultat.put("licences", licencesDAO.findParCommune(nomCommune, codeFederation));
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				resultat.put("erreur", "Parametres manquants.");
			}

			out.print(JsonUtil.toJson(resultat));

		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			Map<String, String> erreur = new HashMap<>();
			erreur.put("erreur", "Erreur base de données : " + e.getMessage());
			out.print(JsonUtil.toJson(erreur));
		}
	}
}
