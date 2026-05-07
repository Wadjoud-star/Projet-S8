package com.clubsport.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        req.setAttribute("genre", "TOTAL");
        loadFilterData(req);
        req.getRequestDispatcher("/WEB-INF/jsp/elus/licences-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codeRegion = req.getParameter("codeRegion");
        String codeDepartement = req.getParameter("codeDepartement");
        String codeCommune = req.getParameter("codeCommune");
        String codeFederation = req.getParameter("codeFederation");
        String genre = req.getParameter("genre");

        req.setAttribute("codeRegion", safe(codeRegion));
        req.setAttribute("codeDepartement", safe(codeDepartement));
        req.setAttribute("codeCommune", safe(codeCommune));
        req.setAttribute("codeFederation", safe(codeFederation));
        req.setAttribute("genre", safe(genre).isEmpty() ? "TOTAL" : safe(genre));

        if (codeFederation == null || codeFederation.isBlank()) {
            req.setAttribute("erreur", "Merci de renseigner le code fédération.");
            loadFilterData(req);
            req.getRequestDispatcher("/WEB-INF/jsp/elus/licences-form.jsp").forward(req, resp);
            return;
        }

        try {
            Optional<StatLicenceElu> stat = eluLicenceService.consulterLicences(
                    codeFederation, genre, codeRegion, codeDepartement, codeCommune);
            req.setAttribute("stat", stat.orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/elus/licences-resultat.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données.", e);
        }
    }

    private void loadFilterData(HttpServletRequest req) {
        List<Map<String, String>> regions = Collections.emptyList();
        List<Map<String, String>> departements = Collections.emptyList();
        try {
            regions = eluLicenceService.listerRegions();
            departements = eluLicenceService.listerDepartements();
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute(
                    "erreurGeo",
                    "Les listes régions/départements n'ont pas pu être chargées (vérifie MySQL et la publication Eclipse).");
        }
        if (regions == null) {
            regions = new ArrayList<>();
        }
        if (departements == null) {
            departements = new ArrayList<>();
        }
        req.setAttribute("regions", regions);
        req.setAttribute("departements", departements);
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
