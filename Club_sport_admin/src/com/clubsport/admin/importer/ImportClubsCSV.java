package com.clubsport.admin.importer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImportClubsCSV {

    // Paramètres de connexion à la base MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/clubs_sportifs";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {

        String cheminFichier = "clubs-data-2019.csv";

        // On ouvre la connexion ET le fichier en même temps
        try (
            
            BufferedReader br = new BufferedReader(new FileReader(cheminFichier))
        ) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion à la base réussie !");

            // Désactiver l'autocommit pour faire un seul commit à la fin
            conn.setAutoCommit(false);

            // Préparer les requêtes SQL une seule fois
            PreparedStatement psRegion = conn.prepareStatement(
                "INSERT IGNORE INTO region (code_region, nom_region) VALUES (?, ?)"
            );
            PreparedStatement psCommune = conn.prepareStatement(
                "INSERT IGNORE INTO commune (code_commune, nom_commune, population, code_region) VALUES (?, ?, NULL, ?)"
            );
            PreparedStatement psFederation = conn.prepareStatement(
                "INSERT IGNORE INTO federation (code_federation, nom_federation) VALUES (?, ?)"
            );
            PreparedStatement psStats = conn.prepareStatement(
                "INSERT INTO statistique_clubs (nb_clubs, nb_etablissements_professionnels, total_structures, code_commune, code_federation) VALUES (?, ?, ?, ?, ?)"
            );

            String ligne;
            boolean premiereLigne = true;
            int compteur = 0;

            while ((ligne = br.readLine()) != null) {

                // Ignorer la ligne d'en-tête
                if (premiereLigne) {
                    premiereLigne = false;
                    continue;
                }

                // Découper la ligne
                String[] col = ligne.split(";");

                // Ignorer les lignes incomplètes ou "NR - Non réparti"
                if (col.length < 11 || col[0].contains("NR")) {
                    continue;
                }

                // Extraire les valeurs utiles
                String codeCommune    = col[0].replace("\"", "").trim();
                String nomCommune     = col[1].replace("\"", "").trim();
                String nomRegion      = col[5].replace("\"", "").trim();
                String codeFederation = col[7].replace("\"", "").trim();
                String nomFederation  = col[8].replace("\"", "").trim();

                // Convertir les chiffres (mettre 0 si la valeur est vide)
                int clubs = col[9].isBlank() ? 0 : Integer.parseInt(col[9].replace("\"", "").trim());
                int ep    = col[10].isBlank() ? 0 : Integer.parseInt(col[10].replace("\"", "").trim());
                int total = col.length > 11 && !col[11].isBlank() ? Integer.parseInt(col[11].replace("\"", "").trim()) : 0;

                // Insérer la région
                psRegion.setString(1, nomRegion);
                psRegion.setString(2, nomRegion);
                psRegion.executeUpdate();

                // Insérer la commune
                psCommune.setString(1, codeCommune);
                psCommune.setString(2, nomCommune);
                psCommune.setString(3, nomRegion);
                psCommune.executeUpdate();

                // Insérer la fédération
                psFederation.setString(1, codeFederation);
                psFederation.setString(2, nomFederation);
                psFederation.executeUpdate();

                // Insérer les statistiques clubs
                psStats.setInt(1, clubs);
                psStats.setInt(2, ep);
                psStats.setInt(3, total);
                psStats.setString(4, codeCommune);
                psStats.setString(5, codeFederation);
                psStats.executeUpdate();

                compteur++;
                if (compteur % 1000 == 0) {
                    System.out.println(compteur + " lignes insérées...");
                }
            }

            // Valider toutes les insertions d'un seul coup
            conn.commit();
            System.out.println("Import terminé ! Total : " + compteur + " lignes.");

       } catch (IOException | SQLException | ClassNotFoundException e) {
    e.printStackTrace();
       }
    }
}