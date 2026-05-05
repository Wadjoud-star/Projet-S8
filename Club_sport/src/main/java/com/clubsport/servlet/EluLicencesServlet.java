package com.clubsport.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.clubsport.model.StatLicenceElu;
import com.clubsport.service.EluLicenceService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Contrôleur : formulaire (GET) puis affichage des statistiques licences (POST).
 */
@WebServlet("/elu/licences")
public class EluLicencesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EluLicenceService eluLicenceService = new EluLicenceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/elus/licences-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codeCommune = req.getParameter("codeCommune");
        String codeFederation = req.getParameter("codeFederation");

        if (codeCommune == null || codeCommune.isBlank() || codeFederation == null || codeFederation.isBlank()) {
            req.setAttribute("erreur", "Merci de renseigner le code commune et le code fédération.");
            req.getRequestDispatcher("/WEB-INF/jsp/elus/licences-form.jsp").forward(req, resp);
            return;
        }

        try {
            Optional<StatLicenceElu> stat = eluLicenceService.consulterLicences(codeCommune, codeFederation);
            req.setAttribute("stat", stat.orElse(null));
            req.setAttribute("codeCommune", codeCommune.trim());
            req.setAttribute("codeFederation", codeFederation.trim());
            req.getRequestDispatcher("/WEB-INF/jsp/elus/licences-resultat.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données.", e);
        }
    }
}
