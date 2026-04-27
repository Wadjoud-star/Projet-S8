package com.clubsport.admin.importer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImportLicencesCSV {

    private static final String URL = "jdbc:mysql://localhost:3306/clubs_sportifs";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        String cheminFichier = "lic-data-2019.csv";

        try (
            BufferedReader br = new BufferedReader(new FileReader(cheminFichier))
        ) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Connexion à la base réussie !");
            conn.setAutoCommit(false);

            PreparedStatement psRegion = conn.prepareStatement(
                "INSERT IGNORE INTO region (code_region, nom_region) VALUES (?, ?)"
            );

            PreparedStatement psCommune = conn.prepareStatement(
                "INSERT IGNORE INTO commune (code_commune, nom_commune, population, code_region) VALUES (?, ?, NULL, ?)"
            );

            PreparedStatement psFederation = conn.prepareStatement(
                "INSERT IGNORE INTO federation (code_federation, nom_federation) VALUES (?, ?)"
            );

            PreparedStatement psLicences = conn.prepareStatement(
                "INSERT INTO statistique_licencies (total_licencies, licencies_femmes, licencies_hommes, code_commune, code_federation) VALUES (?, ?, ?, ?, ?)"
            );

            String ligne;
            boolean premiereLigne = true;
            int compteur = 0;

            while ((ligne = br.readLine()) != null) {

                if (premiereLigne) {
                    premiereLigne = false;
                    continue;
                }

                String[] col = ligne.split(";");

                if (col.length < 8 || col[0].contains("NR")) {
                    continue;
                }

                String codeCommune = col[0].replace("\"", "").trim();
                String nomCommune = col[1].replace("\"", "").trim();
                String nomRegion = col[2].replace("\"", "").trim();
                String codeFederation = col[3].replace("\"", "").trim();
                String nomFederation = col[4].replace("\"", "").trim();

                int totalLicencies = col[5].isBlank() ? 0 : Integer.parseInt(col[5].replace("\"", "").trim());
                int licencesFemmes = col[6].isBlank() ? 0 : Integer.parseInt(col[6].replace("\"", "").trim());
                int licencesHommes = col[7].isBlank() ? 0 : Integer.parseInt(col[7].replace("\"", "").trim());

                psRegion.setString(1, nomRegion);
                psRegion.setString(2, nomRegion);
                psRegion.executeUpdate();

                psCommune.setString(1, codeCommune);
                psCommune.setString(2, nomCommune);
                psCommune.setString(3, nomRegion);
                psCommune.executeUpdate();

                psFederation.setString(1, codeFederation);
                psFederation.setString(2, nomFederation);
                psFederation.executeUpdate();

                psLicences.setInt(1, totalLicencies);
                psLicences.setInt(2, licencesFemmes);
                psLicences.setInt(3, licencesHommes);
                psLicences.setString(4, codeCommune);
                psLicences.setString(5, codeFederation);
                psLicences.executeUpdate();

                compteur++;

                if (compteur % 1000 == 0) {
                    System.out.println(compteur + " lignes insérées...");
                }
            }

            conn.commit();
            System.out.println("Import licences terminé ! Total : " + compteur + " lignes.");

            conn.close();

        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}