package com.clubsport.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.clubsport.dao.EluVisualisationDAO;
import com.clubsport.model.ClassementCommune;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/elu/visualisation")
public class EluVisualisationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EluVisualisationDAO dao = new EluVisualisationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        chargerPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        chargerPage(req, resp);
    }

    private void chargerPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {

            String nomRegion = req.getParameter("region");
            String codeFederation = req.getParameter("codeFederation");

            // Remplace codeCommune par nomCommune
            String nomCommune = req.getParameter("nomCommune");

            // Chargement listes filtres
            List<String> regions = dao.listerRegions();
            List<String> federations = dao.listerFederations();

            // Données filtrées
            List<ClassementCommune> classement =
                    dao.getClassementCommunes(
                            nomRegion,
                            codeFederation,
                            nomCommune
                    );

            int totalHommes =
                    dao.getTotalHommesFiltre(
                            nomRegion,
                            codeFederation,
                            nomCommune
                    );

            int totalFemmes =
                    dao.getTotalFemmesFiltre(
                            nomRegion,
                            codeFederation,
                            nomCommune
                    );

            int total =
                    dao.getTotalFiltre(
                            nomRegion,
                            codeFederation,
                            nomCommune
                    );

            // Envoi JSP
            req.setAttribute("regions", regions);
            req.setAttribute("federations", federations);

            req.setAttribute("region", nomRegion);
            req.setAttribute("codeFederation", codeFederation);

            // Nouveau filtre commune
            req.setAttribute("nomCommune", nomCommune);

            req.setAttribute("classement", classement);

            req.setAttribute("totalHommes", totalHommes);
            req.setAttribute("totalFemmes", totalFemmes);
            req.setAttribute("total", total);

        } catch (SQLException e) {

            req.setAttribute(
                    "erreur",
                    "Erreur base de données : " + e.getMessage()
            );
        }

        req.getRequestDispatcher("/WEB-INF/jsp/elus/visualisation.jsp")
                .forward(req, resp);
    }
}
