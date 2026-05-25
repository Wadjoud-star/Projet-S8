package com.clubsport.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import com.clubsport.service.EluLicenceService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/elu/cartographie")
public class EluCartographieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EluLicenceService eluLicenceService = new EluLicenceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        loadFilterData(req);
        req.getRequestDispatcher("/WEB-INF/jsp/elus/cartographie.jsp").forward(req, resp);
    }

    private void loadFilterData(HttpServletRequest req) {
        try {
            req.setAttribute("regions", eluLicenceService.listerRegions());
            req.setAttribute("departements", eluLicenceService.listerDepartements());
            req.setAttribute("federations", eluLicenceService.listerFederations());
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erreurGeo", "Impossible de charger les listes de filtres.");
            req.setAttribute("regions", Collections.emptyList());
            req.setAttribute("departements", Collections.emptyList());
            req.setAttribute("federations", Collections.emptyList());
        }
    }
}
