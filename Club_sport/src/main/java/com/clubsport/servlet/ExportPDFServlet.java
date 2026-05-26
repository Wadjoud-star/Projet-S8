package com.clubsport.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.clubsport.dao.EluVisualisationDAO;
import com.clubsport.model.ClassementCommune;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/elu/export-pdf")
public class ExportPDFServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EluVisualisationDAO dao =
            new EluVisualisationDAO();

    // IMPORTANT :
    // le bouton export envoie un POST
    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        doGet(req, resp);
    }

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        String region =
                req.getParameter("region");

        String codeFederation =
                req.getParameter("codeFederation");

        String nomCommune =
                req.getParameter("nomCommune");

        resp.setContentType("application/pdf");

        resp.setHeader(
                "Content-Disposition",
                "attachment; filename=\"export-licencies.pdf\""
        );

        try {

            List<ClassementCommune> classement =
                    dao.getClassementCommunes(
                            region,
                            codeFederation,
                            nomCommune
                    );

            Document document =
                    new Document();

            PdfWriter.getInstance(
                    document,
                    resp.getOutputStream()
            );

            document.open();

            document.add(
                    new Paragraph(
                            "Export des donnees - Classement des communes"
                    )
            );

            document.add(
                    new Paragraph(
                            "Region : "
                            + (
                                    region == null
                                    || region.isBlank()
                                    ? "Toutes"
                                    : region
                            )
                    )
            );

            document.add(
                    new Paragraph(
                            "Federation : "
                            + getNomFederation(codeFederation)
                    )
            );

            document.add(
                    new Paragraph(
                            "Commune : "
                            + (
                                    nomCommune == null
                                    || nomCommune.isBlank()
                                    ? "Toutes"
                                    : nomCommune
                            )
                    )
            );

            document.add(new Paragraph(" "));

            PdfPTable table =
                    new PdfPTable(4);

            table.setWidthPercentage(100);

            table.addCell(
                    new PdfPCell(
                            new Phrase("Rang")
                    )
            );

            table.addCell(
                    new PdfPCell(
                            new Phrase("Commune")
                    )
            );

            table.addCell(
                    new PdfPCell(
                            new Phrase("Licencies")
                    )
            );

            table.addCell(
                    new PdfPCell(
                            new Phrase("Taux (%)")
                    )
            );

            for (int i = 0;
                 i < classement.size();
                 i++) {

                ClassementCommune cc =
                        classement.get(i);

                table.addCell(
                        String.valueOf(i + 1)
                );

                table.addCell(
                        cc.getNomCommune()
                );

                table.addCell(
                        String.format(
                                "%,d",
                                cc.getTotalLicencies()
                        ).replace(",", " ")
                );

                table.addCell(
                        String.format(
                                "%.2f",
                                cc.getTauxLicencies()
                        ) + " %"
                );
            }

            document.add(table);

            document.close();

        } catch (SQLException | DocumentException e) {

            throw new ServletException(
                    "Erreur pendant la generation du PDF",
                    e
            );
        }
    }

    private String getNomFederation(
            String codeFederation
    ) throws SQLException {

        if (codeFederation == null
                || codeFederation.isBlank()) {

            return "Toutes";
        }

        List<String> federations =
                dao.listerFederations();

        for (String f : federations) {

            if (
                f.startsWith(codeFederation + " — ")
                || f.startsWith(codeFederation + " - ")
            ) {

                return f;
            }
        }

        return codeFederation;
    }
}