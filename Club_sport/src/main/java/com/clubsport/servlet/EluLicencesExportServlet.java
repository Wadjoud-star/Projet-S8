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
 * Export CSV : une seule ligne d'en-têtes, métadonnées répétées sur chaque ligne de données.
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
        String genre = safe(req.getParameter("genre"));
        if (genre.isEmpty()) {
            genre = "TOTAL";
        }

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
                    codeFederation, genre, codeRegion, codeDepartement, codeCommune);

            String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            String filename = "licences_" + codeFederation + "_" + date + ".csv";
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
            resp.setContentType("text/csv; charset=UTF-8");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            try (Writer out = new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8)) {
                CsvExportUtil.writeUtf8Bom(out);
                writeDataTableCsv(out, rows, genre, codeRegion, codeDepartement, codeCommune, codeFederation);
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données.", e);
        }
    }

    private void writeDataTableCsv(Writer out, List<LicenceExportRow> rows, String genre,
            String codeRegion, String codeDepartement, String codeCommune, String codeFederation) throws IOException {

        String dateStr = LocalDate.now().format(DATE_FR);
        String fedLabel = federationLabel(rows, codeFederation);
        String perimetre = perimetreLabel(rows, codeRegion, codeDepartement, codeCommune);
        String genreStr = genreLabel(genre);
        boolean filterGenre = "F".equalsIgnoreCase(genre) || "H".equalsIgnoreCase(genre);

        int sumTotal = 0;
        int sumF = 0;
        int sumH = 0;
        for (LicenceExportRow row : rows) {
            sumTotal += row.getTotalLicencies();
            sumF += row.getLicenciesFemmes();
            sumH += row.getLicenciesHommes();
        }

        // Une seule ligne d'en-têtes
        if (filterGenre) {
            CsvExportUtil.writeLine(out,
                    "Date export",
                    "Fédération",
                    "Genre filtre",
                    "Périmètre",
                    "Code région",
                    "Région",
                    "Code département",
                    "Département",
                    "Code commune (INSEE)",
                    "Commune",
                    "Population",
                    "Total licenciés",
                    "Licenciées",
                    "Licenciés",
                    "Valeur (filtre genre)");
        } else {
            CsvExportUtil.writeLine(out,
                    "Date export",
                    "Fédération",
                    "Genre filtre",
                    "Périmètre",
                    "Code région",
                    "Région",
                    "Code département",
                    "Département",
                    "Code commune (INSEE)",
                    "Commune",
                    "Population",
                    "Total licenciés",
                    "Licenciées",
                    "Licenciés");
        }

        if (rows.isEmpty()) {
            CsvExportUtil.writeLine(out,
                    dateStr,
                    fedLabel,
                    genreStr,
                    perimetre,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Aucune donnée",
                    "",
                    "",
                    "",
                    filterGenre ? "" : "");
            return;
        }

        for (LicenceExportRow row : rows) {
            if (filterGenre) {
                CsvExportUtil.writeLine(out,
                        dateStr,
                        fedLabel,
                        genreStr,
                        perimetre,
                        row.getCodeRegion(),
                        row.getNomRegion(),
                        row.getCodeDepartement(),
                        row.getNomDepartement(),
                        row.getCodeCommune(),
                        row.getNomCommune(),
                        formatPopulation(row.getPopulation()),
                        String.valueOf(row.getTotalLicencies()),
                        String.valueOf(row.getLicenciesFemmes()),
                        String.valueOf(row.getLicenciesHommes()),
                        String.valueOf(valeurSelonGenre(genre, row)));
            } else {
                CsvExportUtil.writeLine(out,
                        dateStr,
                        fedLabel,
                        genreStr,
                        perimetre,
                        row.getCodeRegion(),
                        row.getNomRegion(),
                        row.getCodeDepartement(),
                        row.getNomDepartement(),
                        row.getCodeCommune(),
                        row.getNomCommune(),
                        formatPopulation(row.getPopulation()),
                        String.valueOf(row.getTotalLicencies()),
                        String.valueOf(row.getLicenciesFemmes()),
                        String.valueOf(row.getLicenciesHommes()));
            }
        }

        // Synthèse utile seulement si plusieurs communes (évite la ligne dupliquée)
        if (rows.size() > 1) {
            if (filterGenre) {
                CsvExportUtil.writeLine(out,
                        dateStr,
                        fedLabel,
                        genreStr,
                        perimetre,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "SYNTHÈSE",
                        "",
                        String.valueOf(sumTotal),
                        String.valueOf(sumF),
                        String.valueOf(sumH),
                        String.valueOf(valeurSelonGenre(genre, sumTotal, sumF, sumH)));
            } else {
                CsvExportUtil.writeLine(out,
                        dateStr,
                        fedLabel,
                        genreStr,
                        perimetre,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "SYNTHÈSE",
                        "",
                        String.valueOf(sumTotal),
                        String.valueOf(sumF),
                        String.valueOf(sumH));
            }
        }
    }

    private static String federationLabel(List<LicenceExportRow> rows, String codeFederation) {
        if (!rows.isEmpty()) {
            LicenceExportRow r = rows.get(0);
            return r.getNomFederation() + " (" + r.getCodeFederation() + ")";
        }
        return codeFederation;
    }

    private static String formatPopulation(int population) {
        return population > 0 ? String.valueOf(population) : "";
    }

    private static int valeurSelonGenre(String genre, LicenceExportRow row) {
        return valeurSelonGenre(genre, row.getTotalLicencies(), row.getLicenciesFemmes(), row.getLicenciesHommes());
    }

    private static int valeurSelonGenre(String genre, int total, int femmes, int hommes) {
        if ("F".equalsIgnoreCase(genre)) {
            return femmes;
        }
        if ("H".equalsIgnoreCase(genre)) {
            return hommes;
        }
        return total;
    }

    private static String genreLabel(String genre) {
        if ("F".equalsIgnoreCase(genre)) {
            return "Femmes";
        }
        if ("H".equalsIgnoreCase(genre)) {
            return "Hommes";
        }
        return "Total";
    }

    private static String perimetreLabel(List<LicenceExportRow> rows, String region, String dept, String commune) {
        if (!commune.isEmpty()) {
            if (!rows.isEmpty()) {
                LicenceExportRow r = rows.get(0);
                return r.getNomCommune() + " (" + r.getCodeCommune() + ")";
            }
            return "INSEE " + commune;
        }
        if (!dept.isEmpty()) {
            if (!rows.isEmpty() && rows.get(0).getNomDepartement() != null && !rows.get(0).getNomDepartement().isBlank()) {
                return rows.get(0).getNomDepartement() + " (" + dept + ")";
            }
            return "Département " + dept;
        }
        if (!region.isEmpty()) {
            if (!rows.isEmpty() && rows.get(0).getNomRegion() != null && !rows.get(0).getNomRegion().isBlank()) {
                return rows.get(0).getNomRegion() + " (" + region + ")";
            }
            return "Région " + region;
        }
        return "France entière";
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
