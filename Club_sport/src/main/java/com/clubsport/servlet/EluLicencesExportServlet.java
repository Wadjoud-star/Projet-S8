package com.clubsport.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.clubsport.model.LicenceExportRow;
import com.clubsport.service.EluLicenceService;
import com.clubsport.util.CsvExportUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Export CSV : une ligne par commune, avec total + répartition femme / homme (sans filtre genre).
 */
@WebServlet("/elu/licences/export")
public class EluLicencesExportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final EluLicenceService eluLicenceService = new EluLicenceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        export(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        export(req, resp);
    }

    private void export(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codeRegion = safe(req.getParameter("codeRegion"));
        String codeDepartement = safe(req.getParameter("codeDepartement"));
        String codeCommune = safe(req.getParameter("codeCommune"));
        String codeFederation = safe(req.getParameter("codeFederation"));

        if (codeFederation.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "La fédération est obligatoire pour l'export.");
            return;
        }

        try {
            if (!codeCommune.isEmpty()
                    && !eluLicenceService.communeDansPerimetre(codeCommune, codeRegion, codeDepartement)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "La commune ne correspond pas au périmètre région / département.");
                return;
            }

            List<LicenceExportRow> rows = eluLicenceService.exporterLicencesDetail(
                    codeFederation, codeRegion, codeDepartement, codeCommune);

            String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            String filename = "licences_" + codeFederation + "_" + date + ".csv";
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
            resp.setContentType("text/csv; charset=UTF-8");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            try (Writer out = new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8)) {
                CsvExportUtil.writeUtf8Bom(out);
                writeDataTableCsv(out, rows, codeRegion, codeDepartement, codeCommune, codeFederation);
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données.", e);
        }
    }

    private void writeDataTableCsv(Writer out, List<LicenceExportRow> rows,
            String codeRegion, String codeDepartement, String codeCommune, String codeFederation) throws IOException {

        String dateStr = LocalDate.now().format(DATE_FR);
        String fedLabel = federationLabel(rows, codeFederation);
        String perimetre = perimetreLabel(rows, codeRegion, codeDepartement, codeCommune);

        int sumTotal = 0;
        int sumF = 0;
        int sumH = 0;
        for (LicenceExportRow row : rows) {
            sumTotal += row.getTotalLicencies();
            sumF += row.getLicenciesFemmes();
            sumH += row.getLicenciesHommes();
        }

        CsvExportUtil.writeLine(out,
                "Date export",
                "Fédération",
                "Périmètre",
                "Région",
                "Département",
                "Commune",
                "Population",
                "Total licenciés",
                "Licenciées femme",
                "Licenciés homme");

        if (rows.isEmpty()) {
            CsvExportUtil.writeLine(out,
                    dateStr,
                    fedLabel,
                    perimetre,
                    "",
                    "",
                    "",
                    "Aucune donnée",
                    "",
                    "",
                    "");
            return;
        }

        for (LicenceExportRow row : rows) {
            CsvExportUtil.writeLine(out,
                    dateStr,
                    fedLabel,
                    perimetre,
                    nullToEmpty(row.getNomRegion()),
                    nullToEmpty(row.getNomDepartement()),
                    nullToEmpty(row.getNomCommune()),
                    formatPopulation(row.getPopulation()),
                    String.valueOf(row.getTotalLicencies()),
                    String.valueOf(row.getLicenciesFemmes()),
                    String.valueOf(row.getLicenciesHommes()));
        }

        if (rows.size() > 1) {
            CsvExportUtil.writeLine(out,
                    dateStr,
                    fedLabel,
                    perimetre,
                    "",
                    "",
                    "SYNTHÈSE",
                    "",
                    String.valueOf(sumTotal),
                    String.valueOf(sumF),
                    String.valueOf(sumH));
        }
    }

    private static String federationLabel(List<LicenceExportRow> rows, String codeFederation) {
        if (!rows.isEmpty() && rows.get(0).getNomFederation() != null && !rows.get(0).getNomFederation().isBlank()) {
            return rows.get(0).getNomFederation();
        }
        return codeFederation;
    }

    private static String formatPopulation(int population) {
        return population > 0 ? String.valueOf(population) : "";
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private static String perimetreLabel(List<LicenceExportRow> rows, String region, String dept, String commune) {
        if (!commune.isEmpty()) {
            if (!rows.isEmpty() && rows.get(0).getNomCommune() != null && !rows.get(0).getNomCommune().isBlank()) {
                return rows.get(0).getNomCommune();
            }
            return "Commune";
        }
        if (!dept.isEmpty()) {
            if (!rows.isEmpty() && rows.get(0).getNomDepartement() != null && !rows.get(0).getNomDepartement().isBlank()) {
                return rows.get(0).getNomDepartement();
            }
            return "Département";
        }
        if (!region.isEmpty()) {
            if (!rows.isEmpty() && rows.get(0).getNomRegion() != null && !rows.get(0).getNomRegion().isBlank()) {
                return rows.get(0).getNomRegion();
            }
            return "Région";
        }
        return "France entière";
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
