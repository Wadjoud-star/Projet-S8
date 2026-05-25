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
        loadFilterData(req);
        req.getRequestDispatcher("/WEB-INF/jsp/elus/licences-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codeRegion = req.getParameter("codeRegion");
        String codeDepartement = req.getParameter("codeDepartement");
        String codeCommune = req.getParameter("codeCommune");
        String codeFederation = req.getParameter("codeFederation");

        req.setAttribute("codeRegion", safe(codeRegion));
        req.setAttribute("codeDepartement", safe(codeDepartement));
        req.setAttribute("codeCommune", safe(codeCommune));
        req.setAttribute("codeFederation", safe(codeFederation));

        if (codeFederation == null || codeFederation.isBlank()) {
            req.setAttribute("erreur", "Merci de choisir une fédération.");
            loadFilterData(req);
            req.getRequestDispatcher("/WEB-INF/jsp/elus/licences-form.jsp").forward(req, resp);
            return;
        }

        try {
            if (!safe(codeCommune).isEmpty()
                    && !eluLicenceService.communeDansPerimetre(safe(codeCommune), safe(codeRegion), safe(codeDepartement))) {
                req.setAttribute("erreur",
                        "La commune choisie ne correspond pas à la région ou au département sélectionné.");
                loadFilterData(req);
                req.getRequestDispatcher("/WEB-INF/jsp/elus/licences-form.jsp").forward(req, resp);
                return;
            }
            if (!safe(codeCommune).isEmpty()) {
                req.setAttribute("communeLibelle",
                        eluLicenceService.libelleCommune(safe(codeCommune)).orElse(safe(codeCommune)));
            }
            Optional<StatLicenceElu> stat = eluLicenceService.consulterLicences(
                    codeFederation, codeRegion, codeDepartement, codeCommune);
            req.setAttribute("stat", stat.orElse(null));
            loadFilterData(req);
            req.getRequestDispatcher("/WEB-INF/jsp/elus/licences-form.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données.", e);
        }
    }

    private void loadFilterData(HttpServletRequest req) {
        List<Map<String, String>> regions = Collections.emptyList();
        List<Map<String, String>> departements = Collections.emptyList();
        List<Map<String, String>> federations = Collections.emptyList();
        try {
            regions = eluLicenceService.listerRegions();
            departements = eluLicenceService.listerDepartements();
            federations = eluLicenceService.listerFederations();
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute(
                    "erreurGeo",
                    "Les listes régions/départements/fédérations n'ont pas pu être chargées (vérifie MySQL et la publication Eclipse).");
        }
        if (regions == null) {
            regions = new ArrayList<>();
        }
        if (departements == null) {
            departements = new ArrayList<>();
        }
        if (federations == null) {
            federations = new ArrayList<>();
        }
        req.setAttribute("regions", regions);
        req.setAttribute("departements", departements);
        req.setAttribute("federations", federations);
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
