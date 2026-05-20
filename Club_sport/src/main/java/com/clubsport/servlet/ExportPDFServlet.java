package com.clubsport.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import com.clubsport.dao.EluVisualisationDAO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/elu/export-pdf")
public class ExportPDFServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EluVisualisationDAO dao = new EluVisualisationDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String region = req.getParameter("region");
        String codeFederation = req.getParameter("codeFederation");
        String codeCommune = req.getParameter("codeCommune");

        String chartHF = req.getParameter("chartHF");
        String chartClassement = req.getParameter("chartClassement");

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=\"export-graphiques.pdf\"");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, resp.getOutputStream());

            document.open();

            document.add(new Paragraph("Export des graphiques"));
            document.add(new Paragraph("Region : " + (region == null || region.isBlank() ? "Toutes" : region)));
            document.add(new Paragraph("Federation : " + getNomFederation(codeFederation)));
            document.add(new Paragraph("Code commune : " + (codeCommune == null || codeCommune.isBlank() ? "Toutes" : codeCommune)));
            document.add(new Paragraph(" "));

            if (chartHF != null && !chartHF.isBlank()) {
                document.add(new Paragraph("Repartition Hommes / Femmes"));
                document.add(convertBase64ToImage(chartHF, 450, 350));
                document.add(new Paragraph(" "));
            }

            if (chartClassement != null && !chartClassement.isBlank()) {
                document.add(new Paragraph("Nombre de licencies par commune"));
                document.add(convertBase64ToImage(chartClassement, 520, 350));
            }

            document.close();

        } catch (SQLException | DocumentException e) {
            throw new ServletException("Erreur pendant la generation du PDF", e);
        }
    }

    private Image convertBase64ToImage(String base64Data, float maxWidth, float maxHeight)
            throws IOException, DocumentException {

        String base64Image = base64Data;

        if (base64Data.contains(",")) {
            base64Image = base64Data.split(",")[1];
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        Image image = Image.getInstance(imageBytes);
        image.scaleToFit(maxWidth, maxHeight);

        return image;
    }

    private String getNomFederation(String codeFederation) throws SQLException {
        if (codeFederation == null || codeFederation.isBlank()) {
            return "Toutes";
        }

        List<String> federations = dao.listerFederations();

        for (String f : federations) {
            if (f.startsWith(codeFederation + " — ") || f.startsWith(codeFederation + " - ")) {
                return f;
            }
        }

        return codeFederation;
    }
}