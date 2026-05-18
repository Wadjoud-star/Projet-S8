package com.clubsport.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.clubsport.service.EluLicenceService;
import com.clubsport.util.JsonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JSON pour l'autocomplétion des communes (espace élu), filtrée par région / département.
 */
@WebServlet("/elu/licences/communes-search")
public class EluCommuneSearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EluLicenceService eluLicenceService = new EluLicenceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String q = req.getParameter("q");
        String codeRegion = req.getParameter("codeRegion");
        String codeDepartement = req.getParameter("codeDepartement");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        try {
            List<Map<String, String>> rows = eluLicenceService.rechercherCommunes(
                    q == null ? "" : q,
                    codeRegion,
                    codeDepartement,
                    20);
            resp.getWriter().write(JsonUtil.toJson(rows));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("[]");
        }
    }
}
