package com.clubsport.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Données JSON pour la cartographie (régions ou communes).
 * ?niveau=regions|communes&amp;codeFederation=...&amp;codeRegion=...&amp;codeDepartement=...
 */
@WebServlet("/elu/cartographie/data")
public class EluCartographieDataServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EluLicenceService eluLicenceService = new EluLicenceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String niveau = safe(req.getParameter("niveau"));
        String codeFederation = safe(req.getParameter("codeFederation"));
        String codeRegion = safe(req.getParameter("codeRegion"));
        String codeDepartement = safe(req.getParameter("codeDepartement"));

        if (codeFederation.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeJson(resp, List.of(Map.of("error", "codeFederation obligatoire")));
            return;
        }

        try {
            List<Map<String, String>> rows;
            if ("communes".equalsIgnoreCase(niveau)) {
                rows = eluLicenceService.agregerLicencesParCommune(codeFederation, codeRegion, codeDepartement);
            } else {
                rows = eluLicenceService.agregerLicencesParRegion(codeFederation, codeDepartement);
            }
            writeJson(resp, rows);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeJson(resp, List.of(Map.of("error", "Erreur base de données")));
        }
    }

    private void writeJson(HttpServletResponse resp, Object data) throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            out.print(JsonUtil.toJson(data));
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
